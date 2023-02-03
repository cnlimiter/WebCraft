package cn.evolvefield.mods.webcraft.mixins;


import cn.evolvefield.mods.webcraft.eventhub.EventManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinInGameHud {
    /**
     * Hook render hud event at the top layer
     */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderEffects(Lcom/mojang/blaze3d/vertex/PoseStack;)V", shift = At.Shift.AFTER))
    public void onHudRender(PoseStack matrices, float tickDelta, CallbackInfo ci) {
        EventManager.eventBus.postConsumer((listener) -> listener.onRenderOverlay(matrices, tickDelta));
    }

}
