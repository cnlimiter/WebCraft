package cn.evolvefield.mods.webcraft.ultralight.listener;

import cn.evolvefield.mods.webcraft.Constants;
import cn.evolvefield.mods.webcraft.ultralight.UltralightEngine;
import com.labymedia.ultralight.UltralightRenderer;
import com.labymedia.ultralight.UltralightView;
import com.labymedia.ultralight.config.UltralightViewConfig;
import com.labymedia.ultralight.input.UltralightCursor;
import com.labymedia.ultralight.math.IntRect;
import com.labymedia.ultralight.plugin.view.MessageLevel;
import com.labymedia.ultralight.plugin.view.MessageSource;
import com.labymedia.ultralight.plugin.view.UltralightViewListener;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/11 13:35
 * Description:
 */
public class ViewListener implements UltralightViewListener {
    /**
     * Called when the page title changes.
     *
     * @param title The new page title
     */
    @Override
    public void onChangeTitle(String title) {

    }

    /**
     * Called when the page URL changes.
     *
     * @param url The new page url
     */
    @Override
    public void onChangeURL(String url) {
        Constants.logger.debug("View url has changed: {}", url);
    }

    /**
     * Called when the tooltip changes (usually as result of a mouse hover).
     *
     * @param tooltip The new page tooltip
     */
    @Override
    public void onChangeTooltip(String tooltip) {

    }

    /**
     * Called when the mouse cursor changes.
     *
     * @param cursor The new page cursor
     */
    @Override
    public void onChangeCursor(UltralightCursor cursor) {
        UltralightEngine.ENGINE.cursorAdapter.notifyCursorUpdated(cursor);

    }

    /**
     * Called when a message is added to the console (useful for errors / debug).
     *
     * @param source       The source the message originated from
     * @param level        The severity of the message
     * @param message      The message itself
     * @param lineNumber   The line the message originated from
     * @param columnNumber The column the message originated from
     * @param sourceId     The id of the source
     */
    @Override
    public void onAddConsoleMessage(MessageSource source, MessageLevel level, String message, long lineNumber, long columnNumber, String sourceId) {
        Constants.logger.info("View message: [{}/{}] {}:{}}:{}}: {}}", source.name(), level.name(), sourceId, lineNumber, columnNumber, message);

    }

    /**
     * Called when the page wants to create a new View.
     * <p>
     * This is usually the result of a user clicking a link with target="_blank" or by JavaScript calling
     * window.open(url).
     * <p>
     * To allow creation of these new Views, you should create a new View in this callback (eg, {@link
     * UltralightRenderer#createView(long, long, UltralightViewConfig)} )}), resize it to your container, and
     * return it. You are responsible for displaying the returned View.
     *
     * @param openerUrl The URL of the page that initiated this request
     * @param targetUrl The URL that the new View will navigate to
     * @param isPopup   Whether or not this was triggered by window.open()
     * @param popupRect Popups can optionally request certain dimensions and coordinates via window.open(). You can
     *                  choose to respect these or not by resizing/moving the View to this rect.
     * @return Returns a {@link UltralightView} to use to satisfy the the request (or return {@code null} if you want to
     * block the action).
     */
    @Override
    public UltralightView onCreateChildView(String openerUrl, String targetUrl, boolean isPopup, IntRect popupRect) {
        return null;
    }
}
