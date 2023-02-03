package cn.evolvefield.mods.webcraft.ultralight.listener;

import cn.evolvefield.mods.webcraft.Constants;
import cn.evolvefield.mods.webcraft.ultralight.View;
import com.labymedia.ultralight.javascript.JavascriptContextLock;
import com.labymedia.ultralight.plugin.loading.UltralightLoadListener;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/11 13:38
 * Description:
 */
public class ViewLoadListener implements UltralightLoadListener {

    private final View view;

    public ViewLoadListener(View view) {
        this.view = view;
    }

    private String frameName(long frameId, boolean isMainFrame, String url) {
        String msg;
        if (isMainFrame) msg = "MainFrame";
        else msg = "Frame";
        return String.format("[%s %s %s]:", msg, frameId, url);
    }

    /**
     * Called when the page begins loading a new URL into the main frame.
     *
     * @param frameId     The id of the frame that has begun loading
     * @param isMainFrame Whether the frame is the main frame
     * @param url         The url that the frame started to load
     */
    @Override
    public void onBeginLoading(long frameId, boolean isMainFrame, String url) {
        Constants.logger.debug("{} The view is about to load", frameName(frameId, isMainFrame, url));
    }

    /**
     * Called when the page finishes loading a URL into the main frame.
     *
     * @param frameId     The id of the frame that finished loading
     * @param isMainFrame Whether the frame is the main frame
     * @param url         The url the frame has loaded
     */
    @Override
    public void onFinishLoading(long frameId, boolean isMainFrame, String url) {
        Constants.logger.debug("{} The view finished loading", frameName(frameId, isMainFrame, url));

    }

    /**
     * Called when an error occurs while loading a URL into the main frame.
     *
     * @param frameId     The id of the frame that failed to load
     * @param isMainFrame Whether the frame is the main frame
     * @param url         The url that failed to load
     * @param description A description of the error
     * @param errorDomain The domain that failed to load
     * @param errorCode   An error code indicating the error reason
     */
    @Override
    public void onFailLoading(long frameId, boolean isMainFrame, String url, String description, String errorDomain, int errorCode) {
        Constants.logger.error("{} Failed to load {}, {} ({})", frameName(frameId, isMainFrame, url), errorDomain, errorCode, description);

    }

    /**
     * Called when the session history (back/forward state) is modified.
     */
    @Override
    public void onUpdateHistory() {

    }

    /**
     * Called when the JavaScript window object is reset for a new page load. This is called before any scripts are
     * executed on the page and is the best time to setup any initial JavaScript state or bindings.
     * <p>
     * The document is not guaranteed to be loaded/parsed at this point. If you need to make any JavaScript calls that
     * are dependent on DOM elements or scripts on the page, use OnDOMReady instead.
     *
     * @param frameId     The id of the frame that the object became ready in
     * @param isMainFrame Whether the frame is the main frame
     * @param url         The url that the frame currently contains
     */
    @Override
    public void onWindowObjectReady(long frameId, boolean isMainFrame, String url) {
        try (JavascriptContextLock lock = view.ultralightView.get().lockJavascriptContext()){
            var context = lock.getContext();
            view.context.setupContext(view, context);
        }

    }

    /**
     * Called when all JavaScript has been parsed and the document is ready.
     * <p>
     * This is the best time to make any JavaScript calls that are dependent on DOM elements or scripts on the page.
     *
     * @param frameId     The id of the frame that the DOM became ready in
     * @param isMainFrame Whether the frame is the main frame
     * @param url         The url that the frame currently contains
     */
    @Override
    public void onDOMReady(long frameId, boolean isMainFrame, String url) {

    }
}
