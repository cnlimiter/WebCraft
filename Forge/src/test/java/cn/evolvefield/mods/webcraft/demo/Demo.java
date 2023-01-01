package cn.evolvefield.mods.webcraft.demo;


import cn.evolvefield.mods.webcraft.Constants;
import cn.evolvefield.mods.webcraft.api.View;
import cn.evolvefield.mods.webcraft.api.WebScreen;
import cn.evolvefield.mods.webcraft.api.math.Vec4i;
import com.google.gson.JsonParser;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod("demo")
@Mod.EventBusSubscriber
public class Demo {
    public Demo() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onGUiOpen(final ScreenEvent.Opening event) {
        if (event.getCurrentScreen() instanceof OptionsScreen screen1) {
            Constants.LOGGER.info("1");
            WebScreen screen = new WebScreen(Component.literal("test"));
            View view = new View();
            view.setResizeCallback(vec -> new Vec4i(0, 0, vec.x, vec.y));
            try {
                view.loadHTML(new ResourceLocation("demo:test.html"));
                view.addJSFuncWithCallback("qwqwq", obj -> JsonParser.parseString("{\"qwq\": \"老子宇宙第一可爱\"}"));
                view.addDOMReadyListener(() -> {
                    /*System.out.println(*/
                    view.evaluteJS("qwq()");//.toString());
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            //view.loadURL("https://qwq.cafe");
//            view.loadHTML("<p>我好可爱啊喵</p>");
//            view.loadHTML("<p style=\"font-family: 微软雅黑;color:#aaa;\">■■■</p>" +
//                    "<p style=\"font-family: 微软雅黑;color:#555;\">草草草</p>" +
//                    "<p style=\"font-family: 微软雅黑;color:#333;\">喵喵喵</p>" +
//                    "<p style=\"font-family: 微软雅黑;color:#777;\">■■■</p>");
            screen.addView(view).addPreRenderer((stack, mouseX, mouseY, pTicks) -> screen.renderBackground(stack));
            screen.setShouldCloseOnEsc(true);
            event.setNewScreen(screen);
        }
    }
}
