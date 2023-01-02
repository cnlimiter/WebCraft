package cn.evolvefield.mods.webcraft.api;

import cn.evolvefield.mods.webcraft.client.UltralightWindow;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL33;

import java.nio.IntBuffer;

public class WebRenderer {
    public static final WebRenderer INSTANCE = new WebRenderer();

    private final long rendererPointer;

    private WebRenderer() {
        rendererPointer = createRenderer();
    }

    /**
     * 生成一个view，不推荐直接使用本方法（除非你不得不用它来实现某些功能）
     * 请使用cafe.qwq.webcraft.api.View#View来创建view
     *
     * @return 使用Ultralight创建的view的地址
     */
    public long createView(int width, int height, boolean transparent, View view) {
        return ncreateView(rendererPointer, width, height, transparent, view);
    }

    /**
     * @return Renderer的地址
     */
    public long getNativeRendererPointer() {
        return rendererPointer;
    }

    /**
     * 让Renderer释放内存
     */
    public void purgeMemory() {
        npurgeMemory(rendererPointer);
    }

    /**
     * 逻辑更新
     */
    public void logicUpdate() {
        update(rendererPointer);
    }

    /**
     * 离屏渲染
     */
    public void offscreenRender() {
        IntBuffer id = BufferUtils.createIntBuffer(1);
        GL33.glGetIntegerv(GL20.GL_CURRENT_PROGRAM, id);
        UltralightWindow.getInstance().makeCurrent();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL33.glDisable(GL33.GL_LIGHTING);
        logicUpdate();
        render(rendererPointer);
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, 0);
        GL33.glFinish();
        UltralightWindow.getInstance().unmakeCurrent();
        GL33.glUseProgram(id.get());
    }

    private static native long createRenderer();

    private native long ncreateView(long pointer, int width, int height, boolean transparent, View view);

    private native void npurgeMemory(long pointer);

    private native void update(long pointer);

    private native void render(long pointer);
}
