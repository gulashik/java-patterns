package org.gulash.observer.subscriber;

/**
 * Интерфейс Наблюдателя.
 * Определяет метод обновления, который вызывается Издателем (Subject).
 */
public interface Subscriber {
    /**
     * Вызывается Издателем при изменении состояния.
     * @param message Данные об обновлении (в данном примере - строка)
     */
    void update(String message);
}
