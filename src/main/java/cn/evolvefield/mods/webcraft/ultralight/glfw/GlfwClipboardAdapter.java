package cn.evolvefield.mods.webcraft.ultralight.glfw;

import com.labymedia.ultralight.plugin.clipboard.UltralightClipboard;
import org.lwjgl.glfw.GLFW;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/10 18:39
 * Description:
 */
public class GlfwClipboardAdapter implements UltralightClipboard {
    @Override
    public void clear() {
        GLFW.glfwSetClipboardString(0, "");
    }

    @Override
    public String readPlainText() {
        return GLFW.glfwGetClipboardString(0);
    }

    @Override
    public void writePlainText(String text) {
        GLFW.glfwSetClipboardString(0, text);
    }
}
