package cn.evolvefield.mods.webcraft.mixins;

import cn.evolvefield.mods.webcraft.eventhub.EventManager;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class MixinKeyboard {
    /**
     * Hook key event
     */
    @Inject(method = "keyPress", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;", shift = At.Shift.BEFORE, ordinal = 0))
    public void onKeyboardKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        EventManager.eventBus.postConsumer((listener) -> listener.onKeyboardKey(window, key, scancode, action, modifiers));
    }

    /**
     * Hook char event
     */
    @Inject(method = "charTyped", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;", shift = At.Shift.BEFORE))
    public void onKeyboardChar(long window, int codePoint, int modifiers, CallbackInfo ci) {
        EventManager.eventBus.postConsumer((listener) -> listener.onKeyboardChar(window, codePoint));
    }
}
