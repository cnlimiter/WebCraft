package cn.evolvefield.mods.webcraft;

import cn.evolvefield.mods.webcraft.util.FileUtils;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.Arrays;

import static cn.evolvefield.mods.webcraft.Constants.MOD_ID;


public class CommonWebCraft {
    private static final String[] resourceArray = {"minecraft.ttf",
            "fzxs12.ttf", "minecraft.css", "button/button.png", "button/button_disabled.png",
            "button/button_hover.png"};

    public static void init() {
        Arrays.stream(resourceArray).forEach(resource -> {
            try {
                FileUtils.upzipIfNeeded(new ResourceLocation(MOD_ID, resource));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win"))
            Constants.RUNTIME_OS = Constants.OS.WINDOWS;
        else if (osName.contains("linux"))
            Constants.RUNTIME_OS = Constants.OS.LINUX;
        else
            Constants.RUNTIME_OS = Constants.OS.MAC;

        Config.init();

    }
}
