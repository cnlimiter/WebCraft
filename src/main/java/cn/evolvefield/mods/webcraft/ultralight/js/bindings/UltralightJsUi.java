package cn.evolvefield.mods.webcraft.ultralight.js.bindings;

import cn.evolvefield.mods.webcraft.ultralight.UltralightEngine;
import cn.evolvefield.mods.webcraft.ultralight.View;
import cn.evolvefield.mods.webcraft.ultralight.pages.Page;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import static cn.evolvefield.mods.webcraft.Constants.mc;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/10 1:10
 * Description:
 */
public class UltralightJsUi {
    public static UltralightJsUi INSTANCE = new UltralightJsUi();


    public Screen get(String name) {
        return new UltralightScreen(Page.of(name));
    }

    public void open(String name, Screen parent) {
        mc.setScreen(get(name));
    }


    private static class UltralightScreen extends Screen {
        private final Page page;
        private final Screen parentScreen;

        boolean firstRender = true;
        View view;
        UltralightEngine engine;

        public UltralightScreen(Page page) {
            this(page, mc.screen);
        }

        protected UltralightScreen(Page page, Screen parent) {
            super(Component.literal(page.name));
            this.page = page;
            this.parentScreen = parent;
            this.engine = UltralightEngine.ENGINE;
        }

        @Override
        public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
            if (firstRender) {
                view = engine.newScreenView(this, parentScreen);
                view.loadPage(page);

                firstRender = false;
            }
        }

        @Override
        public void onClose() {
            engine.removeView(view);
            super.onClose();
        }
    }

}
