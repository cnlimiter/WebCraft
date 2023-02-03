package cn.evolvefield.mods.webcraft.ultralight.hook;

import cn.evolvefield.mods.webcraft.eventhub.EventListener;
import cn.evolvefield.mods.webcraft.ultralight.UltralightEngine;
import cn.evolvefield.mods.webcraft.ultralight.glfw.RenderLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Overlay;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/10 1:05
 * Description:
 */
public class UltralightIntegrationHook implements EventListener {


    @Override
    public void onGameRender() {
        UltralightEngine.ENGINE.update();
    }

    @Override
    public void onScreenRender(Overlay screen, PoseStack matrices, int mouseX, int mouseY, float delta) {
        UltralightEngine.ENGINE.render(RenderLayer.SCREEN_LAYER, matrices);
    }

    @Override
    public void onRenderOverlay(PoseStack matrices, float tickDelta) {
        UltralightEngine.ENGINE.render(RenderLayer.OVERLAY_LAYER, matrices);

    }

    @Override
    public void onWindowResize(Long window, int width, int height) {
        UltralightEngine.ENGINE.resize(Integer.valueOf(width).longValue(), Integer.valueOf(height).longValue());

    }

    @Override
    public void onWindowFocus(Long window, boolean focused) {
        UltralightEngine.ENGINE.inputAdapter.focusCallback(window, focused);

    }

    @Override
    public void onMouseButton(Long window, int button, int action, int mods) {
        UltralightEngine.ENGINE.inputAdapter.mouseButtonCallback(window, button, action, mods);

    }

    @Override
    public void onMouseScroll(Long window, double horizontal, double vertical) {
        UltralightEngine.ENGINE.inputAdapter.scrollCallback(window, horizontal, vertical);

    }

    @Override
    public void onMouseCursor(Long window, double x, double y) {
        UltralightEngine.ENGINE.inputAdapter.cursorPosCallback(window, x, y);

    }

    @Override
    public void onKeyboardKey(Long window, int key, int scancode, int action, int modifiers) {
        UltralightEngine.ENGINE.inputAdapter.keyCallback(window, key, scancode, action, modifiers);

    }

    @Override
    public void onKeyboardChar(Long window, int codePoint) {
        UltralightEngine.ENGINE.inputAdapter.charCallback(window, codePoint);

    }


}
