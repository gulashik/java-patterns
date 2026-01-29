package org.gulash.observer.publisher.impl;

import org.gulash.observer.publisher.Publisher;
import org.gulash.observer.subscriber.Subscriber;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Конкретная реализация Издателя - Новостное Агентство.
 * Хранит список подписчиков и уведомляет их о свежих новостях.
 */
public class NewsPublisher implements Publisher {
    private final List<WeakReference<Subscriber>> subscribers = new CopyOnWriteArrayList<>();
    private String lastNews;

    @Override
    public void subscribe(Subscriber subscriber) {
        Objects.requireNonNull(subscriber, "subscriber must not be null");

        boolean alreadySubscribed = subscribers.stream()
            .map(Reference::get)
            .anyMatch(s -> s == subscriber);

        if (!alreadySubscribed) {
            subscribers.add(new WeakReference<>(subscriber));
        }
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        if (subscriber == null) {
            return;
        }
        subscribers.removeIf(ref -> ref.get() == subscriber);
    }

    @Override
    public void notifySubscribers(String message) {
        // Важно: в реальных проектах стоит подумать о потокобезопасности
        // и порядке уведомления.

        // Очистка мертвых ссылок можно перед, а можно в цикле
        //subscribers.removeIf(ref -> ref.get() == null);
        for (WeakReference<Subscriber> ref : subscribers) {
            Subscriber subscriber = ref.get();
            if (subscriber != null) {
                subscriber.update(message);
            } else {
                subscribers.remove(ref); // объекта уже нет
            }
        }
    }

    /**
     * Бизнес-логика: публикация новости.
     */
    public void publishNews(String news) {
        Objects.requireNonNull(news, "news must not be null");

        this.lastNews = news;
        System.out.println("NewsAgency published: " + news);
        notifySubscribers(news);
    }

    public String getLastNews() {
        return lastNews;
    }
}
