package cn.evolvefield.mods.webcraft.util;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/10 18:30
 * Description:
 */
public class ThreadLock<T> {
    private Thread lockedOnThread;
    private T content = null;

    public T get() {
        if (Thread.currentThread() != lockedOnThread) {
            InfoUtil.error("thread-locked content accessed by other thread");
        }

        return content;
    }

    public void lock(T t) {
        lockedOnThread = Thread.currentThread();
        content = t;
    }
}
