package cn.evolvefield.mods.webcraft.ultralight.renderer;

import com.labymedia.ultralight.UltralightView;
import com.labymedia.ultralight.config.UltralightViewConfig;
import com.labymedia.ultralight.gpu.UltralightOpenGLGPUDriverNative;
import com.mojang.blaze3d.vertex.PoseStack;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static cn.evolvefield.mods.webcraft.Constants.mc;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/11 1:46
 * Description:
 */
public class GpuViewRenderer implements ViewRenderer {

    long window = 0L;
    UltralightOpenGLGPUDriverNative driver;

    @Override
    public void setupConfig(UltralightViewConfig viewConfig) {
        viewConfig.isAccelerated(true);

        // todo: might use alternative context
        window = mc.getWindow().getWindow();
        driver = new UltralightOpenGLGPUDriverNative(window, true);
    }

    @Override
    public void render(UltralightView view, PoseStack matrices) {
        driver.setActiveWindow(window);
        GLFW.glfwMakeContextCurrent(window);
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_TRANSFORM_BIT);

        if (driver.hasCommandsPending()) {
            // GLFW.glfwMakeContextCurrent(this.window);
            driver.drawCommandList();
            // GLFW.glfwSwapBuffers(this.window);
        }

        GL11.glPopAttrib();

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);

        this.renderHtmlTexture(view, window);
        GLFW.glfwMakeContextCurrent(window);
    }

    @Override
    public void delete() {

    }


    private void renderHtmlTexture(UltralightView view, long window) {
        driver.setActiveWindow(window);
        var text = view.renderTarget().getTextureId();
        var width = Long.valueOf(view.width()).intValue();
        var height = Long.valueOf(view.height()).intValue();
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // Set up the OpenGL state for rendering of a fullscreen quad
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_TRANSFORM_BIT);
        driver.bindTexture(0, text);
        GL20.glUseProgram(0);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, Long.valueOf(view.width()).doubleValue(), Long.valueOf(view.height()).doubleValue(), 0.0, -1.0, 1.0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        // Disable lighting and scissoring, they could mess up th renderer
        GL11.glLoadIdentity();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Make sure we draw with a neutral color
        // (so we don't mess with the color channels of the image)
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glBegin(GL11.GL_QUADS);

        // Lower left corner, 0/0 on the screen space, and 0/0 of the image UV
        GL11.glTexCoord2f(0f, 0f);
        GL11.glVertex2f(0f, 0f);

        // Upper left corner
        GL11.glTexCoord2f(0f, 1f);
        GL11.glVertex2i(0, height);

        // Upper right corner
        GL11.glTexCoord2f(1f, 1f);
        GL11.glVertex2i(width, height);

        // Lower right corner
        GL11.glTexCoord2f(1f, 0f);
        GL11.glVertex2i(width, 0);
        GL11.glEnd();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        // Restore OpenGL state
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopAttrib();
    }
}
