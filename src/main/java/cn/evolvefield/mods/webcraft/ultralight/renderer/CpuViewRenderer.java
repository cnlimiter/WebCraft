package cn.evolvefield.mods.webcraft.ultralight.renderer;

import com.labymedia.ultralight.UltralightView;
import com.labymedia.ultralight.bitmap.UltralightBitmapSurface;
import com.labymedia.ultralight.config.UltralightViewConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
import org.lwjgl.opengl.GL11;

import static cn.evolvefield.mods.webcraft.Constants.mc;
import static com.mojang.blaze3d.platform.GlConst.GL_CLAMP_TO_EDGE;
import static com.mojang.blaze3d.platform.GlConst.GL_NEAREST;
import static com.mojang.blaze3d.platform.GlConst.GL_RGBA8;
import static com.mojang.blaze3d.platform.GlConst.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL12.*;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/11 1:28
 * Description:
 */
public class CpuViewRenderer implements ViewRenderer {
    private int glTexture = -1;

    @Override
    public void setupConfig(UltralightViewConfig viewConfig) {

    }

    @Override
    public void render(UltralightView view, PoseStack matrices) {
        if (glTexture == -1) {
            createGlTexture();
        }

        // As we are using the CPU renderer, draw with a bitmap (we did not set a custom surface)
        UltralightBitmapSurface surface = (UltralightBitmapSurface) view.surface();
        var bitmap = surface.bitmap();
        var width = Long.valueOf(view.width()).intValue();
        var height = Long.valueOf(view.height()).intValue();

        // Prepare OpenGL for 2D textures and bind our texture
        RenderSystem.enableTexture();
        RenderSystem.bindTexture(glTexture);

        var dirtyBounds = surface.dirtyBounds();

        if (dirtyBounds.isValid()) {
            var imageData = bitmap.lockPixels();

            GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, 0);
            GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, 0);
            GL11.glPixelStorei(GL_UNPACK_SKIP_IMAGES, 0);
            GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, Long.valueOf(bitmap.rowBytes()).intValue() / 4);

            if (dirtyBounds.width() == width && dirtyBounds.height() == height) {
                // Update full image
                GL11.glTexImage2D(
                        GL_TEXTURE_2D,
                        0,
                        GL_RGBA8,
                        width,
                        height,
                        0,
                        GL_BGRA,
                        GL_UNSIGNED_INT_8_8_8_8_REV,
                        imageData
                );
            } else {
                // Update partial image
                var x = dirtyBounds.x();
                var y = dirtyBounds.y();
                var dirtyWidth = dirtyBounds.width();
                var dirtyHeight = dirtyBounds.height();
                var startOffset = Long.valueOf(y * bitmap.rowBytes() + x * 4L).intValue();

                GL11.glTexSubImage2D(
                        GL_TEXTURE_2D,
                        0,
                        x, y, dirtyWidth, dirtyHeight,
                        GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV,
                        imageData.position(startOffset)
                );
            }
            GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, 0);

            bitmap.unlockPixels();
            surface.clearDirtyBounds();
        }

        var tesselator = Tesselator.getInstance();
        var bufferBuilder = tesselator.getBuilder();
        var scaleFactor = Double.valueOf(mc.getWindow().getGuiScale()).floatValue();

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, glTexture);
        RenderSystem.enableBlend();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        bufferBuilder
                .vertex(0.0f, height, 0.0f)
                .uv(0f, scaleFactor)
                .color(255, 255, 255, 255)
                .endVertex();
        bufferBuilder
                .vertex( width, height, 0.0f)
                .uv(scaleFactor, scaleFactor)
                .color(255, 255, 255, 255)
                .endVertex();
        bufferBuilder
                .vertex( width, 0.0f, 0.0f)
                .uv(scaleFactor, 0.0f)
                .color(255, 255, 255, 255)
                .endVertex();

        bufferBuilder
                .vertex( 0.0f, 0.0f, 0.0f)
                .uv(0.0f, 0.0f)
                .color(255, 255, 255, 255)
                .endVertex();

        BufferUploader.drawWithShader(bufferBuilder.end());
        //tesselator.end();
        RenderSystem.disableBlend();

    }

    @Override
    public void delete() {
        GL11.glDeleteTextures(glTexture);
        glTexture = -1;
    }

    /**
     * Sets up the OpenGL texture for rendering
     */
    private void createGlTexture() {
        glTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL_TEXTURE_2D, glTexture);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        GL11.glBindTexture(GL_TEXTURE_2D, 0);
    }
}
