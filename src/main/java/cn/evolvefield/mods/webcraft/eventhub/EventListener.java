package cn.evolvefield.mods.webcraft.eventhub;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/10 1:03
 * Description:
 */
public interface EventListener {
    default void onGameRender() {
    }


    default void onScreenRender(Screen screen, PoseStack stack, int mouseX, int mouseY, float partialTick) {
    }


    default void onRenderOverlay(PoseStack matrices, float partialTick) {
    }

    default void onWindowResize(Long window, int width, int height) {
    }

    default void onWindowFocus(Long window, boolean focused) {
    }

    default void onMouseButton(Long window, int button, int action, int mods) {
    }

    default void onMouseScroll(Long window, double horizontal, double vertical) {
    }

    default void onMouseCursor(Long window, double x, double y) {
    }

    default void onKeyboardKey(Long window, int key, int scancode, int action, int modifiers) {
    }

    default void onKeyboardChar(Long window, int codePoint) {
    }

    default boolean onSetScreen(Screen screen) {
        return false;
    }

    default void onClose() {
    }

}
