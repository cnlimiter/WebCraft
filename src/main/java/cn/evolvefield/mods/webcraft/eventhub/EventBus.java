package cn.evolvefield.mods.webcraft.eventhub;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/11 13:48
 * Description:
 */
public class EventBus<T> {
    private final ConcurrentLinkedDeque<T> listeners = new ConcurrentLinkedDeque<T>();


    public void register(T listener) {
        listeners.add(listener);
    }

    public void unregister(T listener) {
        listeners.remove(listener);
    }

    public void unregisterAll(Predicate<T> predicate) {
        listeners.removeIf(predicate::test);

    }

    public List<T> post(Consumer<T> r) {
        var returns = new ArrayList<T>();
        for (var listener : listeners) {
            r.accept(listener);
            returns.add(listener);
        }
        return returns;
    }

    public void postConsumer(Consumer<T> consumer) {
        for (var listener1 : listeners) consumer.accept(listener1);
    }
}
