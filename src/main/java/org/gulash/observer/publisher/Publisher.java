package org.gulash.observer.publisher;

import org.gulash.observer.subscriber.Subscriber;

/**
 * Интерфейс Субъекта (Издателя).
 * Предоставляет методы для управления списком наблюдателей.
 */
public interface Publisher {
    /**
     * Подписывает наблюдателя на обновления.
     */
    void subscribe(Subscriber subscriber);

    /**
     * Отписывает наблюдателя.
     */
    void unsubscribe(Subscriber subscriber);

    /**
     * Уведомляет всех подписанных наблюдателей.
     */
    void notifySubscribers(String message);
}
