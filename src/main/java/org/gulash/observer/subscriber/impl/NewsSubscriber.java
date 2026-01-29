package org.gulash.observer.subscriber.impl;

import org.gulash.observer.subscriber.Subscriber;

/**
 * Конкретная реализация Наблюдателя - Новостной Канал.
 */
public class NewsSubscriber implements Subscriber {
    private final String channelName;

    public NewsSubscriber(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public void update(String message) {
        System.out.println("[" + channelName + "] Received news: " + message);
    }
}
