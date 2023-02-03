package cn.evolvefield.mods.webcraft.client;

import cn.evolvefield.mods.webcraft.ultralight.UltralightEngine;
import cn.evolvefield.mods.webcraft.ultralight.View;
import cn.evolvefield.mods.webcraft.ultralight.pages.Page;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import static cn.evolvefield.mods.webcraft.Constants.mc;

/**
 * Project: WebCraft-Fabric
 * Author: cnlimiter
 * Date: 2023/1/25 23:11
 * Description:
 */
public class WebOverlay extends Overlay {
    private boolean firstRender = true;
    private final Page page;
    private final Screen parent;
    private View view;

    public WebOverlay(Page page){
        this(page, mc.screen);
    }

    public WebOverlay(Page page, Screen parent) {
        super();
        this.page = page;
        this.parent = parent;
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        if (firstRender) {
            view = UltralightEngine.ENGINE.newOverlayView();
            view.loadPage(page);
            firstRender = false;
        }
    }

}
