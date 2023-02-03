package cn.evolvefield.mods.webcraft.ultralight.renderer;

import com.labymedia.ultralight.UltralightView;
import com.labymedia.ultralight.config.UltralightViewConfig;
import com.mojang.blaze3d.vertex.PoseStack;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/10 19:59
 * Description:
 */
public interface ViewRenderer {
    /**
     * Setup [viewConfig]
     */
    void setupConfig(UltralightViewConfig viewConfig);

    /**
     * Render view
     */
    void render(UltralightView view, PoseStack matrices);

    /**
     * Delete
     */
    void delete();
}
