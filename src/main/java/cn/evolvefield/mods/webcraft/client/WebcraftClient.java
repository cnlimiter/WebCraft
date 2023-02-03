package cn.evolvefield.mods.webcraft.client;

import cn.evolvefield.mods.webcraft.Constants;
import cn.evolvefield.mods.webcraft.ultralight.pages.Page;
import cn.evolvefield.mods.webcraft.util.GuiHandler;
import com.mojang.brigadier.Command;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

/**
 * Project: WebCraft-Fabric
 * Author: cnlimiter
 * Date: 2023/1/13 13:00
 * Description:
 */
@Environment(EnvType.CLIENT)
public class WebcraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (GuiHandler.gui != null) {
                client.setScreen(GuiHandler.gui);
                GuiHandler.gui = null;
            }
        });
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                    ClientCommandManager.literal("testscreen").executes(context -> {
                        Constants.LOG.info(Page.of("webcraft/funny").toString());
                        GuiHandler.displayGui(new WebScreen(Page.of("webcraft/funny")));
                        return Command.SINGLE_SUCCESS;
                    })

            );
        });
    }
}
