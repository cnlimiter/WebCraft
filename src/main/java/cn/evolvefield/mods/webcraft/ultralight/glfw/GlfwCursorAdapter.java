package cn.evolvefield.mods.webcraft.ultralight.glfw;

import cn.evolvefield.mods.webcraft.ultralight.UltralightEngine;
import com.labymedia.ultralight.input.UltralightCursor;
import org.lwjgl.glfw.GLFW;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/10 18:40
 * Description:
 */
public class GlfwCursorAdapter {

    private final long beamCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR);
    private final long crosshairCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_CROSSHAIR_CURSOR);
    private final long handCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
    private final long hresizeCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HRESIZE_CURSOR);
    private final long vresizeCursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_VRESIZE_CURSOR);

    /**
     * Signals this [GlfwCursorAdapter] that the cursor has been updated and needs to be updated on the GLFW side
     * too.
     *
     * @param cursor The new [UltralightCursor] to display
     */
    public void notifyCursorUpdated(UltralightCursor cursor) {

        switch (cursor) {
            case CROSS -> GLFW.glfwSetCursor(UltralightEngine.window, crosshairCursor);
            case HAND -> GLFW.glfwSetCursor(UltralightEngine.window, handCursor);
            case I_BEAM -> GLFW.glfwSetCursor(UltralightEngine.window, beamCursor);
            case EAST_WEST_RESIZE -> GLFW.glfwSetCursor(UltralightEngine.window, hresizeCursor);
            case NORTH_SOUTH_RESIZE -> GLFW.glfwSetCursor(UltralightEngine.window, vresizeCursor);
            default -> GLFW.glfwSetCursor(UltralightEngine.window, 0);
        }
    }

    /**
     * Frees GLFW resources allocated by this [GlfwCursorAdapter].
     */
    public void cleanup() {
        GLFW.glfwDestroyCursor(vresizeCursor);
        GLFW.glfwDestroyCursor(hresizeCursor);
        GLFW.glfwDestroyCursor(handCursor);
        GLFW.glfwDestroyCursor(crosshairCursor);
        GLFW.glfwDestroyCursor(beamCursor);
    }

    public void unfocus() {
        GLFW.glfwSetCursor(UltralightEngine.window, 0);
    }
}
