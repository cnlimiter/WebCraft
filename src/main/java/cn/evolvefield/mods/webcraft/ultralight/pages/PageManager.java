package cn.evolvefield.mods.webcraft.ultralight.pages;

import cn.evolvefield.mods.webcraft.Constants;
import cn.evolvefield.mods.webcraft.eventhub.EventListener;
import cn.evolvefield.mods.webcraft.eventhub.EventManager;
import cn.evolvefield.mods.webcraft.ultralight.UltralightResources;
import cn.evolvefield.mods.webcraft.util.CopyDirVisitor;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/10 1:14
 * Description:
 */
public class PageManager implements EventListener {
    private File tmpFolder;
    public static File guisFolder;


    public PageManager() {
        try {
            tmpFolder = new File(UltralightResources.ultralightRoot, "tmp");
            Files.createDirectories(tmpFolder.toPath());
            guisFolder = new File(tmpFolder, "guis");
            Files.createDirectories(guisFolder.toPath());
        } catch (IOException e) {
            Constants.logger.error("创建缓存文件夹失败");
        }
        EventManager.eventBus.register(this);

    }


    public void registerModPages() {
        //cleanup();
        try {
            for (var mod : FabricLoader.getInstance().getAllMods()){
                var path = mod.findPath("webcraft");
                if (path.isPresent()) {
                    Constants.logger.info("Registering ultralight mod: {}", mod.getMetadata().getId());
                    var targetDir = new File(guisFolder, mod.getMetadata().getId()).toPath();
                    Files.createDirectories(targetDir);
                    Files.walkFileTree(path.get(), new CopyDirVisitor(path.get(), targetDir));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void cleanup() {
        try {
            FileUtils.deleteDirectory(tmpFolder);
        }
        catch (IOException e){
            Constants.logger.error(e.getMessage());
        }
    }

    @Override
    public void onClose() {
        cleanup();
    }
}
