package cn.evolvefield.mods.webcraft.handler;

import cn.evolvefield.mods.webcraft.api.WebScreen;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;

/**
 * Project: WebCraft-MultiLoader
 * Author: cnlimiter
 * Date: 2023/1/3 0:50
 * Description:
 */
public class ForgeWebScreen extends WebScreen {

    public ForgeWebScreen(Component component) {
        super(component);
    }

    @Override
    public void setEventCallBack(PoseStack stack) {
        MinecraftForge.EVENT_BUS.post(new ScreenEvent.BackgroundRendered(this, stack));
    }

}
