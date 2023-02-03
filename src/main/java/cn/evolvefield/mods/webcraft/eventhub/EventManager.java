package cn.evolvefield.mods.webcraft.eventhub;

/**
 * Project: WebCraft-1.19.3
 * Author: cnlimiter
 * Date: 2023/1/11 14:02
 * Description:
 */
public class EventManager {

    public static EventBus<EventListener> eventBus = new EventBus<>();

    public EventManager() {

    }
}
