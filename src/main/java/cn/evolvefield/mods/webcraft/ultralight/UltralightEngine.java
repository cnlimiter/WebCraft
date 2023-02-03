package cn.evolvefield.mods.webcraft.ultralight;

import cn.evolvefield.mods.webcraft.Constants;
import cn.evolvefield.mods.webcraft.eventhub.EventManager;
import cn.evolvefield.mods.webcraft.ultralight.filesystem.BrowserFileSystem;
import cn.evolvefield.mods.webcraft.ultralight.glfw.GlfwClipboardAdapter;
import cn.evolvefield.mods.webcraft.ultralight.glfw.GlfwCursorAdapter;
import cn.evolvefield.mods.webcraft.ultralight.glfw.GlfwInputAdapter;
import cn.evolvefield.mods.webcraft.ultralight.glfw.RenderLayer;
import cn.evolvefield.mods.webcraft.ultralight.hook.UltralightIntegrationHook;
import cn.evolvefield.mods.webcraft.ultralight.pages.PageManager;
import cn.evolvefield.mods.webcraft.ultralight.renderer.CpuViewRenderer;
import cn.evolvefield.mods.webcraft.ultralight.renderer.ViewRenderer;
import cn.evolvefield.mods.webcraft.util.ThreadLock;
import com.labymedia.ultralight.UltralightJava;
import com.labymedia.ultralight.UltralightLoadException;
import com.labymedia.ultralight.UltralightPlatform;
import com.labymedia.ultralight.UltralightRenderer;
import com.labymedia.ultralight.config.FontHinting;
import com.labymedia.ultralight.config.UltralightConfig;
import com.labymedia.ultralight.gpu.UltralightGPUDriverNativeUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;

import static cn.evolvefield.mods.webcraft.Constants.mc;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/10 13:26
 * Description:
 */
public class UltralightEngine {

    public static UltralightEngine ENGINE = new UltralightEngine();

    /**
     * Ultralight window
     * <p>
     * Might be useful in the future for external UI.
     */
    public static long window = mc.getWindow().getWindow();

    /**
     * Ultralight resources
     */
    private final UltralightResources resources = new UltralightResources();

    /**
     * Ultralight platform and renderer
     */
    ThreadLock<UltralightPlatform> platform = new ThreadLock<>();
    ThreadLock<UltralightRenderer> renderer = new ThreadLock<>();

    /**
     * Glfw
     */
    public GlfwClipboardAdapter clipboardAdapter;
    public GlfwCursorAdapter cursorAdapter;
    public GlfwInputAdapter inputAdapter;

    /**
     * Views
     */
    public View activeView = null;
    private static final ArrayList<View> views = new ArrayList<>();

    public View getActiveView() {
        if (views.stream().iterator().hasNext()
                && views.stream().iterator().next() instanceof ScreenView view
                && mc.screen == view.screen
        ){
            return view;
        }
        return activeView;
    }

    public UltralightEngine() {
    }


    /**
     * Initializes the platform
     */
    public void init() {
        var refreshRate = mc.getWindow().getRefreshRate();

        Constants.logger.info("Loading ultralight...");

        // Check resources
        Constants.logger.info("Checking resources...");
        resources.downloadResources();

        // Load natives from native directory inside root folder
        Constants.logger.debug("Loading ultralight natives");
        try {
            UltralightJava.load(resources.binRoot.toPath());
            UltralightGPUDriverNativeUtil.load(resources.binRoot.toPath());
        } catch (UltralightLoadException e) {
            Constants.logger.error(e.getMessage());
        }

        // Setup GLFW adapters
        clipboardAdapter = new GlfwClipboardAdapter();
        cursorAdapter = new GlfwCursorAdapter();
        inputAdapter = new GlfwInputAdapter();

        // Setup platform
        Constants.logger.debug("Setting up ultralight platform");
        platform.lock(UltralightPlatform.instance());
        platform.get().setConfig(
                new UltralightConfig()
                        .animationTimerDelay(1.0 / refreshRate)
                        .scrollTimerDelay(1.0 / refreshRate)
                        .resourcePath(resources.cacheRoot.getAbsolutePath())
                        .cachePath(resources.cacheRoot.getAbsolutePath())
                        .fontHinting(FontHinting.SMOOTH)
        );
        platform.get().usePlatformFontLoader();
        platform.get().setFileSystem(new BrowserFileSystem());
        platform.get().setClipboard(clipboardAdapter);
        platform.get().setLogger((level, message) -> {
            switch (level) {
                case ERROR -> Constants.logger.debug("[Ultralight/ERR] {}", message);
                case WARNING -> Constants.logger.debug("[Ultralight/WARN] {}", message);
                case INFO -> Constants.logger.debug("[Ultralight/INFO] {}", message);
            }
        });


        // Setup renderer
        Constants.logger.debug("Setting up ultralight renderer");
        renderer.lock(UltralightRenderer.create());

        // Setup hooks
        EventManager.eventBus.register(new UltralightIntegrationHook());



        Constants.logger.info("Loading pages...");
        PageManager manager = new PageManager();
        manager.registerModPages();

        Constants.logger.info("Successfully loaded ultralight!");
    }

    public void shutdown() {
        cursorAdapter.cleanup();
    }

    public void update() {
        views.forEach(View::update);
        renderer.get().update();
    }

    public void render(RenderLayer layer, PoseStack matrices) {
        frameLimitedRender();
        views.stream()
                .filter(view -> view.renderLayer == layer)
                .forEach(view -> view.render(matrices));
    }


    private double lastRenderTime = 0.0;
    private void frameLimitedRender() {
        int MAX_FRAME_RATE = 60;
        var frameTime = 1.0 / MAX_FRAME_RATE;
        var time = System.nanoTime() / 1e9;
        var delta = time - lastRenderTime;

        if (delta < frameTime) {
            return;
        }

        renderer.get().render();
        lastRenderTime = time;
    }

    public void resize(long width, long height) {
        views.forEach(view -> view.resize(width, height));
    }

    public View newSplashView() {
        var render = new View(RenderLayer.SPLASH_LAYER, newViewRenderer());
        views.add(render);
        return render;
    }

    public View newOverlayView() {
        var render = new View(RenderLayer.OVERLAY_LAYER, newViewRenderer());
        views.add(render);
        return render;
    }

    public View newScreenView(Screen screen, Screen parentScreen) {
        var screenA = new ScreenView(newViewRenderer(), screen, parentScreen);
        views.add(screenA);
        return screenA;
    }

    public View newScreenView(Screen screen){
        return newScreenView(screen, mc.screen);
    }

    public void removeView(View view) {
        view.free();
        views.remove(view);
    }

    private ViewRenderer newViewRenderer() {
        return new CpuViewRenderer();
    }

    public static class ScreenView extends View {
        public final Screen screen;
        public final Screen parentScreen;

        public ScreenView(ViewRenderer viewRenderer, Screen screen, Screen parentScreen) {
            super(RenderLayer.SCREEN_LAYER, viewRenderer);
            this.screen = screen;
            this.parentScreen = parentScreen;

        }
    }
}
