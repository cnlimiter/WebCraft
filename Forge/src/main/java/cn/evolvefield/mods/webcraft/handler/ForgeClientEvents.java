package cn.evolvefield.mods.webcraft.handler;

import cn.evolvefield.mods.webcraft.Constants;
import cn.evolvefield.mods.webcraft.client.NativeLibEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Consumer;

/**
 * Project: WebCraft-MultiLoader
 * Author: cnlimiter
 * Date: 2023/1/1 23:52
 * Description:
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeClientEvents {
    private static Consumer<ScreenEvent.Opening> consumer;

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        consumer = ForgeClientEvents::onGuiOpen;
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, consumer);//最高优先级
    }

    public static void onGuiOpen(final ScreenEvent.Opening event) {
        MinecraftForge.EVENT_BUS.unregister(consumer);
        NativeLibEvent.onGuiOpen();
    }

}
