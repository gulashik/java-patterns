package org.gulash.observer;

import org.gulash.observer.subscriber.impl.NewsSubscriber;
import org.gulash.observer.publisher.impl.NewsPublisher;

/**
 * Демонстрация работы паттерна Наблюдатель (Observer).
 */
public class ObserverDemo {
    public static void main(String[] args) {
        System.out.println("=== Observer Pattern Demo ===");

        // 1. Создаем издателя
        NewsPublisher agency = new NewsPublisher();

        // 2. Создаем наблюдателей
        NewsSubscriber bbc = new NewsSubscriber("BBC");
        NewsSubscriber cnn = new NewsSubscriber("CNN");
        NewsSubscriber euronews = new NewsSubscriber("EuroNews");

        // 3. Подписываем их на агентство
        agency.subscribe(bbc);
        agency.subscribe(cnn);
        agency.subscribe(euronews);

        // 4. Публикуем новость
        agency.publishNews("Java 21 is released!");

        System.out.println("\n--- Unsubscribing CNN ---");
        agency.unsubscribe(cnn);

        // 5. Публикуем еще одну новость
        agency.publishNews("New design patterns added to the repository.");

        System.out.println("\n=== Demo Finished ===");
    }
}
