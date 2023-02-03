package cn.evolvefield.mods.webcraft.mixins;


import cn.evolvefield.mods.webcraft.eventhub.EventManager;
import cn.evolvefield.mods.webcraft.eventhub.EventListener;
import cn.evolvefield.mods.webcraft.ultralight.UltralightEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Minecraft.class)
public class MixinMinecraftClient {
    @Shadow @Nullable public LocalPlayer player;

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    public void onSetScreen(Screen screen, CallbackInfo ci) {
        if (EventManager.eventBus.post((listener) -> listener.onSetScreen(screen)).stream().anyMatch(eventListener -> eventListener.onSetScreen(screen))) {
            ci.cancel();
        }
    }

    @Inject(method = "destroy", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;close()V", shift = At.Shift.BEFORE))
    public void onClose(CallbackInfo ci) {
        EventManager.eventBus.postConsumer(EventListener::onClose);
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;resizeDisplay()V"))
    public void onStartGame(GameConfig $$0, CallbackInfo ci) {
        UltralightEngine.ENGINE.init();
    }

}
