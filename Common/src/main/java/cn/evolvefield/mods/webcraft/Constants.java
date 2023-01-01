package cn.evolvefield.mods.webcraft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

    public static final String MOD_ID = "webcraft";
    public static final String MOD_NAME = "webcraft";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    public static String VERSION = "NULL";

    public static OS RUNTIME_OS;


    public enum OS {
        WINDOWS, MAC, LINUX
    }
}
