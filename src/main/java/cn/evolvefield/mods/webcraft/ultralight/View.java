package cn.evolvefield.mods.webcraft.ultralight;

import cn.evolvefield.mods.webcraft.Constants;
import cn.evolvefield.mods.webcraft.ultralight.glfw.RenderLayer;
import cn.evolvefield.mods.webcraft.ultralight.js.UltralightJsContext;
import cn.evolvefield.mods.webcraft.ultralight.js.bindings.EventListenerJs;
import cn.evolvefield.mods.webcraft.ultralight.listener.ViewListener;
import cn.evolvefield.mods.webcraft.ultralight.listener.ViewLoadListener;
import cn.evolvefield.mods.webcraft.ultralight.pages.Page;
import cn.evolvefield.mods.webcraft.ultralight.renderer.ViewRenderer;
import cn.evolvefield.mods.webcraft.util.ThreadLock;
import com.labymedia.ultralight.UltralightView;
import com.labymedia.ultralight.config.UltralightViewConfig;
import com.labymedia.ultralight.input.UltralightKeyEvent;
import com.labymedia.ultralight.input.UltralightMouseEvent;
import com.labymedia.ultralight.input.UltralightScrollEvent;
import com.mojang.blaze3d.vertex.PoseStack;

import static cn.evolvefield.mods.webcraft.Constants.mc;
import static cn.evolvefield.mods.webcraft.eventhub.EventManager.eventBus;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/10 19:07
 * Description:
 */
public class View {

    public RenderLayer renderLayer;
    private final ViewRenderer viewRenderer;
    public ThreadLock<UltralightView> ultralightView = new ThreadLock<UltralightView>();

    public UltralightJsContext context;

    Page viewingPage = null;

    private long jsGarbageCollected = 0L;


    public View(RenderLayer layer, ViewRenderer viewRenderer) {
        this.renderLayer = layer;
        this.viewRenderer = viewRenderer;
        // Setup view
        var width = mc.getWindow().getWidth();
        var height = mc.getWindow().getHeight();
        var viewConfig = new UltralightViewConfig()
                .isTransparent(true)
                .initialDeviceScale(1.0);

        // Make sure renderer setups config correctly
        viewRenderer.setupConfig(viewConfig);

        ultralightView.lock(UltralightEngine.ENGINE.renderer.get().createView(width, height, viewConfig));
        ultralightView.get().setViewListener(new ViewListener());
        ultralightView.get().setLoadListener(new ViewLoadListener(this));

        // Setup JS bindings
        context = new UltralightJsContext(this, ultralightView);

        Constants.logger.debug("Successfully created new view");
    }


    /**
     * Loads the specified [page]
     */
    public void loadPage(Page page) {
        // Unregister listeners
        eventBus.unregisterAll(eventListener -> eventListener instanceof EventListenerJs);

        if (viewingPage != page && viewingPage != null) {
            page.close();
        }

        ultralightView.get().loadURL(page.viewableFile);
        viewingPage = page;
        Constants.logger.debug("Successfully loaded page {} from {}", page.name, page.viewableFile);
    }

    /**
     * Loads the specified [page]
     */
    public void loadUrl(String url) {
        // Unregister listeners
        eventBus.unregisterAll(eventListener -> eventListener instanceof EventListenerJs);


        ultralightView.get().loadURL(url);
        Constants.logger.debug("Successfully loaded page {}", url);
    }

    /**
     * Update view
     */
    public void update() {
        // Check if page has new update
        var page = viewingPage;

        if (page != null && page.hasUpdate() == true) {
            loadPage(page);
        }

        // Collect JS garbage
        collectGarbage();
    }

    /**
     * Render view
     */
    public void render(PoseStack matrices) {
        viewRenderer.render(ultralightView.get(), matrices);
    }

    /**
     * Resizes web view to [width] and [height]
     */
    public void resize(long width, long height) {
        ultralightView.get().resize(width, height);
        Constants.logger.debug("Successfully resized to $width:$height");
    }

    /**
     * Garbage collect JS engine
     */
    private void collectGarbage() {
        if (jsGarbageCollected == 0L) {
            jsGarbageCollected = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - jsGarbageCollected > 1000) {
            Constants.logger.debug("Garbage collecting Ultralight Javascript...");
            ultralightView.get().lockJavascriptContext().getContext().garbageCollect();
            jsGarbageCollected = System.currentTimeMillis();
        }
    }

    /**
     * Free view
     */
    public void free() {
        ultralightView.get().unfocus();
        ultralightView.get().stop();
        if (viewingPage != null) viewingPage.close();
        viewRenderer.delete();
        eventBus.unregisterAll(eventListener -> eventListener instanceof EventListenerJs);

    }

    public void focus() {
        ultralightView.get().focus();
    }

    public void unfocus() {
        ultralightView.get().unfocus();
    }

    public void fireScrollEvent(UltralightScrollEvent event) {
        ultralightView.get().fireScrollEvent(event);
    }

    public void fireMouseEvent(UltralightMouseEvent event) {
        ultralightView.get().fireMouseEvent(event);
    }

    public void fireKeyEvent(UltralightKeyEvent event) {
        ultralightView.get().fireKeyEvent(event);
    }
}
