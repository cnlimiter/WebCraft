package cn.evolvefield.mods.webcraft.mixins;

import cn.evolvefield.mods.webcraft.eventhub.EventManager;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MixinMouse {
    /**
     * Hook mouse button event
     */
    @Inject(method = "onPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getOverlay()Lnet/minecraft/client/gui/screens/Overlay;", shift = At.Shift.BEFORE))
    public void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        EventManager.eventBus.postConsumer((listener) -> listener.onMouseButton(window, button, action, mods));
    }

    /**
     * Hook mouse scroll event
     */
    @Inject(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getOverlay()Lnet/minecraft/client/gui/screens/Overlay;", shift = At.Shift.BEFORE))
    public void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        EventManager.eventBus.postConsumer((listener) -> listener.onMouseScroll(window, horizontal, vertical));
    }

    /**
     * Hook mouse cursor event
     */
    @Inject(method = "onMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getOverlay()Lnet/minecraft/client/gui/screens/Overlay;", shift = At.Shift.BEFORE))
    public void onMouseCursor(long window, double x, double y, CallbackInfo ci) {
        EventManager.eventBus.postConsumer((listener) -> listener.onMouseCursor(window, x, y));
    }
}
