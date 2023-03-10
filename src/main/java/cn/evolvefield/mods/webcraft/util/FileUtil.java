package cn.evolvefield.mods.webcraft.util;

import cn.evolvefield.mods.webcraft.Constants;
import net.minecraft.resources.ResourceLocation;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

public class FileUtil {
    private static Set<String> unzipSet = new HashSet<>();

    public static void downloadFile(URL url, File outputFile, IDownloadCallback callback) throws IOException {
        URLConnection connection = url.openConnection();
        InputStream input = connection.getInputStream();
        OutputStream output = new FileOutputStream(outputFile);
        float size = connection.getContentLength() / 1024f;
        int length, sum = 0, sum2 = 0;
        byte[] bytes = new byte[1024];
        long lastTime = System.currentTimeMillis();
        String name = outputFile.getName();

        while ((length = input.read(bytes)) != -1) {
            output.write(bytes, 0, length);
            sum += length;
            sum2 += length;
            long time = System.currentTimeMillis();
            if (time - lastTime >= 1000) {
                Constants.LOG.info(String.format("Downloading %s(%.2fKB/%.2fKB %.2fKB/s)...", name, sum / 1024f, size, sum2 / (1.024f * (time - lastTime))));
                if (callback != null) callback.callback(size, sum / 1024f, sum2 / (1.024f * (time - lastTime)));
                sum2 = 0;
                lastTime = time;
            }
        }

        long time = System.currentTimeMillis();
        Constants.LOG.info(String.format("Downloading %s(%.2fKB/%.2fKB %.2fKB/s)...", name, sum / 1024f, size, sum2 / (1.024f * (time - lastTime))));
        if (callback != null) callback.callback(size, sum / 1024f, sum2 / (1.024f * (time - lastTime)));

        input.close();
        output.close();
    }

    public static void upzipIfNeeded(ResourceLocation location) throws IOException {
        upzipIfNeeded("/assets/" + location.getNamespace() + "/web/" + location.getPath());
    }

    public static void upzipIfNeeded(String resourcePath) throws IOException {
        upzipIfNeeded(FileUtil.class, resourcePath);
    }

    public static void upzipIfNeeded(Class<?> c, String resourcePath) throws IOException {
        if (unzipSet.contains(resourcePath)) return;

        File ouputFile = new File("mods/webcraft" + resourcePath);
        if (!ouputFile.getParentFile().exists()) ouputFile.getParentFile().mkdirs();

        InputStream input = c.getResourceAsStream(resourcePath);
        try (OutputStream output = new FileOutputStream(ouputFile)) {

            int length;
            byte[] bytes = new byte[1024];

            if (input != null) {
                while ((length = input.read(bytes)) != -1) {
                    output.write(bytes, 0, length);
                }
            }
        }
    }

    public static void upzipIfNeeded(File resourcePath, File ouputFile) throws IOException {
        if (unzipSet.contains(resourcePath)) return;

        if (!ouputFile.getParentFile().exists()) ouputFile.getParentFile().mkdirs();

        InputStream input = new FileInputStream(resourcePath);
        try (OutputStream output = new FileOutputStream(ouputFile)) {

            int length;
            byte[] bytes = new byte[1024];

            if (input != null) {
                while ((length = input.read(bytes)) != -1) {
                    output.write(bytes, 0, length);
                }
            }
        }
    }

    public interface IDownloadCallback {
        void callback(float size, float downloadSize, float speed);
    }


    public static void delDir(File file) {
        if (file.isFile()) {
            boolean delete = file.delete();
            if (delete) {
                System.out.println(file.getAbsolutePath() + "???????????????..");
            } else {
                System.out.println(file.getAbsolutePath() + "???????????????..");
            }
        } else {
            for (File sonfile : file.listFiles()) {
                delDir(sonfile);
            }
        }
        boolean delete1 = file.delete();
        if (delete1) {
            System.out.println(file.getAbsolutePath() + "???????????????..");
        } else {
            System.out.println(file.getAbsolutePath() + "???????????????..");
        }
    }
}
