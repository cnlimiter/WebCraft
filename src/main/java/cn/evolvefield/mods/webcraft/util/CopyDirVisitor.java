package cn.evolvefield.mods.webcraft.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Project: WebCraft-Fabric
 * Author: cnlimiter
 * Date: 2023/1/13 13:39
 * Description:
 */
public class CopyDirVisitor extends SimpleFileVisitor<Path> {
    private final Path sourceDir;
    private final Path targetDir;

    public CopyDirVisitor(Path sourcePath, Path targetPath){
        this.sourceDir = sourcePath;
        this.targetDir = targetPath;
    }


    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        var targetFile = targetDir.resolve(sourceDir.relativize(file));
        Files.copy(file, targetFile);

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        var newDir = targetDir.resolve(sourceDir.relativize(dir));
        Files.createDirectories(newDir);

        return FileVisitResult.CONTINUE;
    }
}
