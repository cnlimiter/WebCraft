package cn.evolvefield.mods.webcraft;

import net.minecraftforge.common.util.MavenVersionStringHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ForgeWebCraft {

    public ForgeWebCraft() {
        ModList.get().getMods().stream()
                .filter(info -> info.getModId().equals(Constants.MOD_ID))
                .forEach(info -> Constants.VERSION = MavenVersionStringHelper.artifactVersionToString(info.getVersion()));

        if (Constants.VERSION.equals("NULL")) Constants.LOGGER.warn("WebCraft got version failed !");
        else Constants.LOGGER.info("Welcome to use WebCraft (version:" + Constants.VERSION + ") ! ");

        CommonWebCraft.init();


    }
}
