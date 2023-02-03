package cn.evolvefield.mods.webcraft.ultralight.pages;

import cn.evolvefield.mods.webcraft.Constants;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/10 1:13
 * Description:
 */
public class Page {
    public String name;
    public static String viewableFile;
    boolean exists;
    private final WatchService watcher;
    public Page(@NotNull String name) {
        this.name = name;
        File pageFolder = new File(PageManager.guisFolder, name);
        viewableFile = String.format("file:///%s", new File(pageFolder, "index.html").getAbsolutePath());
        exists = pageFolder.exists();

        var path = pageFolder.toPath();
        WatchService watchService = null;
        try {
            watchService = path.getFileSystem().newWatchService();
            path.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.watcher = watchService;
    }


    public static Page of(String name) {
        return new Page(name);
    }


    public boolean hasUpdate() {
        var watchKey = watcher.poll();
        boolean shouldUpdate;
        if (watchKey != null && watchKey.pollEvents() != null) {
            shouldUpdate = !watchKey.pollEvents().isEmpty();
            watchKey.reset();
            return shouldUpdate;

        } else return false;
    }

    public void close() {
        try {
            watcher.close();
        } catch (IOException e) {
            Constants.logger.error(e.getMessage());
        }

    }

    @Override
    public String toString() {
        return String.format("Page(%s, %s)", name, viewableFile);
    }


}
