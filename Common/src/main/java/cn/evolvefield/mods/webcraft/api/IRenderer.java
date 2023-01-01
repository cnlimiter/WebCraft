package cn.evolvefield.mods.webcraft.api;

import com.mojang.blaze3d.vertex.PoseStack;

public interface IRenderer {
    void render(PoseStack stack, int mouseX, int mouseY, float pTicks);
}
