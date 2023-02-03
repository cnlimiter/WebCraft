package cn.evolvefield.mods.webcraft.mixins;



import cn.evolvefield.mods.webcraft.eventhub.EventManager;
import cn.evolvefield.mods.webcraft.eventhub.EventListener;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(method = "render", at = @At("HEAD"))
    public void onGameRender(CallbackInfo ci) {
        EventManager.eventBus.postConsumer(EventListener::onGameRender);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Overlay;render(Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V"))
    public void onScreenRender(Overlay screen, PoseStack stack, int mouseX, int mouseY, float delta) {
        screen.render(stack, mouseX, mouseY, delta);
        EventManager.eventBus.postConsumer((listener) -> listener.onScreenRender(screen, stack, mouseX, mouseY, delta));
    }
}
