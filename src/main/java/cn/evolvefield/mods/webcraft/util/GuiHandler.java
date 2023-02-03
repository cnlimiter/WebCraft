package cn.evolvefield.mods.webcraft.util;

import net.minecraft.client.gui.screens.Screen;

/**
 * Project: WebCraft-Fabric
 * Author: cnlimiter
 * Date: 2023/1/25 23:07
 * Description:
 */
public class GuiHandler {
    public static Screen gui = null;

    public static void displayGui(Screen gui){
        GuiHandler.gui = gui;
    }

}
