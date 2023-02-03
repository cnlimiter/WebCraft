package cn.evolvefield.mods.webcraft.mixins.callbacks;

import com.labymedia.ultralight.databind.Databind;
import com.labymedia.ultralight.javascript.JavascriptContext;
import com.labymedia.ultralight.javascript.JavascriptObject;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;

/**
 * Project: WebCraft-Fabric
 * Author: cnlimiter
 * Date: 2023/2/2 19:15
 * Description:
 */
public class SetupContextCallback {
    public static final Event<SetupContextCallback.Callback> EVENT = EventFactory.createArrayBacked(SetupContextCallback.Callback.class, callbacks -> (context, globalObject, databind) -> {
        for (SetupContextCallback.Callback callback : callbacks) {
            var result = callback.setupContext(context, globalObject, databind);

            if (result != InteractionResult.SUCCESS) {
                return result;
            }
        }
        return InteractionResult.SUCCESS;
    });

    @FunctionalInterface
    public interface Callback {
        InteractionResult setupContext(JavascriptContext context, JavascriptObject javascriptObject, Databind databind);
    }
}
