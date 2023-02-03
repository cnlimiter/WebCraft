package cn.evolvefield.mods.webcraft.ultralight;

import cn.evolvefield.mods.webcraft.Constants;
import cn.evolvefield.mods.webcraft.util.FileUtil;
import cn.evolvefield.mods.webcraft.util.InfoUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

import static cn.evolvefield.mods.webcraft.Constants.DOWNLOAD_URL;
import static cn.evolvefield.mods.webcraft.Constants.mc;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/10 1:16
 * Description:
 */
public class UltralightResources {
    public static File ultralightRoot;
    private static final File nativesRoot;
    private static final String LIBRARY_VERSION;

    static {
        LIBRARY_VERSION = "0.4.6";
        ultralightRoot = new File(mc.gameDirectory, "config/webcraft");
        nativesRoot = new File(ultralightRoot, "natives");
        nativesRoot.mkdirs();
    }


    File binRoot = new File(nativesRoot, "bin");
    File cacheRoot = new File(nativesRoot, "cache");
    private final File resourcesRoot = new File(nativesRoot, "resources");

    public void downloadResources() {
        try {
            var versionsFile = new File(nativesRoot, "VERSION");

            // Check if library version is matching the resources version
            if (versionsFile.exists()) {
                List<String> allLines = Files.readAllLines(versionsFile.toPath(), StandardCharsets.UTF_8);
                if (Objects.equals(allLines.get(0), LIBRARY_VERSION))
                    return;
            }

            // Make sure the old natives are being deleted
            if (binRoot.exists()) {
                FileUtil.delDir(binRoot);
            }

            if (resourcesRoot.exists()) {
                FileUtil.delDir(resourcesRoot);
            }

            String os = "";
            // Translate os to path
            if (Constants.system.IS_WINDOWS) os = "win";
            else if (Constants.system.IS_MAC) os = "mac";
            else if (Constants.system.IS_UNIX) os = "linux";
            else InfoUtil.error("unsupported operating system");


            Constants.LOG.info("Downloading v{} resources... (os: {})", LIBRARY_VERSION, os);
            var nativeUrl = String.format("%s/cn/evolvefield/native/ultralight_resources/%s/%s-x64.zip", DOWNLOAD_URL, LIBRARY_VERSION, os);

            // Download resources
            ultralightRoot.mkdir();
            var pkgNatives = new File(nativesRoot, "resources.zip");
            pkgNatives.createNewFile();
            FileUtil.downloadFile(new URL(nativeUrl), pkgNatives, null);


            // Extract resources from zip archive
            Constants.LOG.info("Extracting resources...");
            FileUtil.upzipIfNeeded(pkgNatives, nativesRoot);
            versionsFile.createNewFile();
            FileUtils.writeStringToFile(versionsFile, LIBRARY_VERSION, Charset.defaultCharset());
            // Make sure to delete zip archive to save space
            Constants.LOG.debug("Deleting resources bundle...");
            pkgNatives.delete();

            Constants.LOG.info("Successfully loaded resources.");

        } catch (Throwable e) {
            Constants.LOG.error("Unable to download resources, Error: {}", e.getMessage());
            System.exit(-1);
            throw new RuntimeException("System.exit returned normally, while it was supposed to halt JVM.");
        }
    }


}
