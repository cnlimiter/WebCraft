package cn.evolvefield.mods.webcraft.ultralight.js;

import cn.evolvefield.mods.webcraft.Constants;
import cn.evolvefield.mods.webcraft.eventhub.EventManager;
import cn.evolvefield.mods.webcraft.mixins.callbacks.SetupContextCallback;
import cn.evolvefield.mods.webcraft.ultralight.UltralightEngine;
import cn.evolvefield.mods.webcraft.ultralight.View;
import cn.evolvefield.mods.webcraft.ultralight.js.bindings.UltralightJsUi;
import cn.evolvefield.mods.webcraft.ultralight.js.bindings.UltralightJsUtils;
import cn.evolvefield.mods.webcraft.util.ThreadLock;
import com.labymedia.ultralight.UltralightView;
import com.labymedia.ultralight.databind.Databind;
import com.labymedia.ultralight.databind.DatabindConfiguration;
import com.labymedia.ultralight.javascript.JavascriptContext;
import com.labymedia.ultralight.javascript.JavascriptObject;

import static cn.evolvefield.mods.webcraft.Constants.mc;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/11 13:14
 * Description:
 */
public class UltralightJsContext {
    ViewContextProvider contextProvider;
    Databind databind;
    private final View view;

    public UltralightJsContext(View view, ThreadLock<UltralightView> ulView) {
        this.view = view;
        this.contextProvider = new ViewContextProvider(ulView);
        this.databind = new Databind(
                DatabindConfiguration
                        .builder()
                        .contextProviderFactory(new ViewContextProvider.Factory(ulView))
                        .build()
        );


    }


    public void setupContext(View view, JavascriptContext context) {
        var globalContext = context.getGlobalContext();
        var globalObject = globalContext.getGlobalObject();

        globalObject.setProperty(
                "engine",
                databind.getConversionUtils().toJavascript(context, UltralightEngine.ENGINE),
                0
        );

        globalObject.setProperty(
                "view",
                databind.getConversionUtils().toJavascript(context, view),
                0
        );

        // todo: minecraft has to be remapped
        globalObject.setProperty(
                "minecraft",
                databind.getConversionUtils().toJavascript(context, mc),
                0
        );

        globalObject.setProperty(
                "ui",
                databind.getConversionUtils().toJavascript(context, UltralightJsUi.INSTANCE),
                0
        );

        globalObject.setProperty(
                "utils",
                databind.getConversionUtils().toJavascript(context, UltralightJsUtils.INSTANCE),
                0
        );

        globalObject.setProperty(
                "eventBus",
                databind.getConversionUtils().toJavascript(context, EventManager.eventBus),
                0
        );

        globalObject.setProperty(
                "logger",
                databind.getConversionUtils().toJavascript(context, Constants.logger),
                0
        );

        if (view instanceof UltralightEngine.ScreenView screenView) {
            globalObject.setProperty(
                    "screen",
                    databind.getConversionUtils().toJavascript(context, screenView.screen),
                    0
            );

            var parentScreen = screenView.parentScreen;

            if (parentScreen != null) {
                globalObject.setProperty(
                        "parentScreen",
                        databind.getConversionUtils().toJavascript(context, screenView.parentScreen),
                        0
                );
            }
        }

        //SetupContextCallback.EVENT.invoker().setupContext(context, globalObject, databind);
    }




}
