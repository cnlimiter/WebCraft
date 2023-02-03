package cn.evolvefield.mods.webcraft.ultralight.js.bindings;

import com.labymedia.ultralight.os.OperatingSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/12 0:58
 * Description:
 */
public class UltralightJsUtils {

    public static UltralightJsUtils INSTANCE = new UltralightJsUtils();
    static final Logger LOGGER = LoggerFactory.getLogger(UltralightJsUtils.class);

    private void open(String url) {

        //getOperatingSystem().open(url);

    }

    private static OperatingSystem getOperatingSystem() {
        String string = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (string.contains("win")) {
            return OperatingSystem.WINDOWS;
        } else if (string.contains("mac")) {
            return OperatingSystem.MAC_OS;
        } else if (string.contains("linux")) {
            return OperatingSystem.LINUX;
        } else {
            return string.contains("unix") ? OperatingSystem.LINUX : OperatingSystem.UNKNOWN;
        }
    }


}
