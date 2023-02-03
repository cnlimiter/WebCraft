package cn.evolvefield.mods.webcraft.ultralight.js;

import cn.evolvefield.mods.webcraft.util.ThreadLock;
import com.labymedia.ultralight.UltralightView;
import com.labymedia.ultralight.databind.context.ContextProvider;
import com.labymedia.ultralight.databind.context.ContextProviderFactory;
import com.labymedia.ultralight.javascript.JavascriptContextLock;
import com.labymedia.ultralight.javascript.JavascriptValue;

import java.util.function.Consumer;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/11 13:15
 * Description:
 */
public class ViewContextProvider implements ContextProvider {
    ThreadLock<UltralightView> view;

    public ViewContextProvider(ThreadLock<UltralightView> view) {
        this.view = view;
    }

    @Override
    public void syncWithJavascript(Consumer<JavascriptContextLock> callback) {
        var lock = view.get().lockJavascriptContext();
        callback.accept(lock);
    }

    public static class Factory implements ContextProviderFactory {
        private final ThreadLock<UltralightView> view;

        public Factory(ThreadLock<UltralightView> view) {
            this.view = view;
        }

        @Override
        public ContextProvider bindProvider(JavascriptValue value) {
            return new ViewContextProvider(view);
        }
    }
}
