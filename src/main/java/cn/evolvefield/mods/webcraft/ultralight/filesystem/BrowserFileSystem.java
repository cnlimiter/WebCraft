package cn.evolvefield.mods.webcraft.ultralight.filesystem;

import com.labymedia.ultralight.plugin.filesystem.UltralightFileSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/10 0:37
 * Description:
 */
public class BrowserFileSystem implements UltralightFileSystem {

    private Logger logger = LogManager.getLogger("Ultralight FS");


    private long nextFileHandle = 0;

    private final Map<Long, FileChannel> openFiles = new LinkedHashMap<>();

    /**
     * 再Ultralight启用时用来检查文件是否存在
     * 请注意，Ultralight 可能会传递无效路径，因此请检查它们！
     *
     * @param path 检查的路径
     * @return `true` 文件存在, `false`其他情况
     */
    @Override
    public boolean fileExists(String path) {
        log(false, "Checking if %s exists", path);
        var realPath = getPath(path);
        var exists = realPath != null && Files.exists(realPath);
        if (exists) log(false, "%s %s", path, "exists");
        else log(false, "%s %s", path, "does not exist");
        return exists;
    }


    /**
     * 检索给定位置的文件大小。如果无法检索大小，则返回 -1。
     *
     * @param handle 要获取文件大小的文件位置
     * @return 给定位置文件的大小, 如果无法确定大小返回 `-1`
     */
    @Override
    public long getFileSize(long handle) {
        log(false, "Retrieving file size of handle %d", handle);
        var channel = openFiles.get(handle);

        if (channel == null) {
            // 除非Ultralight启动失败，否则永远不会达到这里
            log(true, "Failed to retrieve file size of handle %d, it was invalid", handle);
            return -1;
        } else {
            try {
                var size = channel.size();
                log(false, "File size of handle %d is %d", handle, size);
                return size;
            } catch (IOException e) {
                log(true, "Exception while retrieving size of handle %d", handle);
                e.printStackTrace();
                return -1;
            }
        }
    }

    @Override
    public String getFileMimeType(String path) {
        log(false, "Retrieving mime type of %s", path);
        var realPath = getPath(path);
        if (realPath == null) {
            // Ultralight 请求了不存在的位置
            log(true, "Failed to retrieve mime type of %s, path was invalid", path);
            return null;
        }
        try {
            // 检测 mime 类型并且记录
            var mimeType = Files.probeContentType(realPath);
            log(false, "Mime type of %s is %s", path, mimeType);
            return mimeType;
        } catch (IOException e) {
            log(true, "Exception while retrieving mime type of %s", path);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long openFile(String path, boolean openForWriting) {
        if (openForWriting) log(false, "Opening file %s for %s", path, "writing");
        else log(false, "Opening file %s for %s", path, "reading");
        var realPath = getPath(path);
        if (realPath == null) {
            log(true, "Failed to open %s, the path is invalid", path);
            return UltralightFileSystem.INVALID_FILE_HANDLE;
        }
        FileChannel channel;
        try {
            // 实际打开操作
            if (openForWriting) channel = FileChannel.open(realPath, StandardOpenOption.WRITE);
            else channel = FileChannel.open(realPath, StandardOpenOption.READ);
        } catch (IOException e) {
            log(true, "Exception while opening %s", path);
            e.printStackTrace();
            return UltralightFileSystem.INVALID_FILE_HANDLE;
        }
        if (nextFileHandle == UltralightFileSystem.INVALID_FILE_HANDLE) {
            // 减少位置数量
            nextFileHandle = UltralightFileSystem.INVALID_FILE_HANDLE + 1;
        }
        var handle = nextFileHandle++;
        openFiles.put(handle, channel);
        log(false, "Opened %s as handle %d", path, handle);
        return handle;
    }

    @Override
    public void closeFile(long handle) {
        log(false, "Closing handle %d", handle);
        var channel = openFiles.get(handle);
        if (channel != null) {
            try {
                channel.close();
                log(false, "Handle %d has been closed", handle);
            } catch (IOException e) {
                log(true, "Exception while closing handle %d", handle);
                e.printStackTrace();
            } finally {
                openFiles.remove(handle);
            }
        } else {
            log(false, "Failed to close handle %d, it was invalid", handle);
        }
    }

    @Override
    public long readFromFile(long handle, ByteBuffer data, long length) {
        log(false, "Trying to read %d bytes from handle %d", length, handle);
        var channel = openFiles.get(handle);
        if (channel == null) {
            log(true, "Failed to read %d bytes from handle %d, it was invalid", length, handle);
            return -1;
        }
        if (length > Integer.MAX_VALUE) {
            log(true, "Failed to read %d bytes from handle %d, the size exceeded the limit", length, handle);
            // Not supported yet, marked as TODO
            // You should not throw Java exceptions into native code, so use it for getting a stacktrace and return -1
            new UnsupportedOperationException().printStackTrace();
            return -1;
        }
        try {
            var read = Long.valueOf(channel.read(data.slice().limit(Long.valueOf(length).intValue())));
            log(false, "Read %d bytes out of %d requested from handle %d", read, length, handle);
            return read;
        } catch (IOException e) {
            log(true, "Exception occurred while reading %d bytes from handle %d", length, handle);
            e.printStackTrace();
            return -1;
        }
    }

    private Path getPath(String path) {
        try {
            return Paths.get(path);
        } catch (InvalidPathException e) {
            return null;
        }
    }

    private void log(boolean error, String fmt, Object... args) {
        var message = String.format(fmt, args);
        if (error) {
            logger.error("[ERROR/FileSystem] $message");
        } else {
            logger.debug("[INFO/FileSystem] $message");
        }
    }

}
