package cn.evolvefield.mods.webcraft;

import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

public class Constants {

    public static final String MOD_ID = "webcraft";
    public static final String MOD_NAME = "WebCraft";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static final Logger logger = LoggerFactory.getLogger("Ultralight");

    public static final String DOWNLOAD_URL = "https://maven.nova-committee.cn/releases";
    public static final Minecraft mc = Minecraft.getInstance();


    public static class system {
        public static String OS = System.getProperty("os.name").toLowerCase();
        public static boolean IS_WINDOWS = OS.contains("win");
        public static boolean IS_MAC = OS.contains("mac") || OS.contains("darwin");
        public static boolean IS_UNIX = OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0;
    }

}
