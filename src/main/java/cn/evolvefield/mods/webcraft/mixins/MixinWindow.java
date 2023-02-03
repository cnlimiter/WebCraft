package cn.evolvefield.mods.webcraft.mixins;

import cn.evolvefield.mods.webcraft.eventhub.EventManager;
import com.mojang.blaze3d.platform.Window;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Window.class)
public class MixinWindow {
    @Shadow
    @Final
    private long window;

    /**
     * Hook window resize
     */
    @Inject(method = "onResize", at = @At("HEAD"))
    public void onResizeWindow(long w, int width, int height, CallbackInfo ci) {
        if (window == w)
            EventManager.eventBus.postConsumer((listener) -> listener.onWindowResize(window, width, height));
    }

    /**
     * Hook window focus
     */
    @Inject(method = "onFocus", at = @At(value = "FIELD", target = "Lcom/mojang/blaze3d/platform/Window;eventHandler:Lcom/mojang/blaze3d/platform/WindowEventHandler;"))
    public void hookFocus(long window, boolean focused, CallbackInfo callbackInfo) {
        EventManager.eventBus.postConsumer((listener) -> listener.onWindowFocus(window, focused));
    }
}
