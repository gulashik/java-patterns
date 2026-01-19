package org.gulash.objectpool;


import org.gulash.objectpool.factory.item.DatabaseConnection;
import org.gulash.objectpool.factory.DatabaseConnectionFactory;
import org.gulash.objectpool.pool.BlockingItemPool;
import org.gulash.objectpool.pool.ItemPool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Демонстрация использования пула объектов.
 * Показывает преимущества пула в многопоточной среде.
 */
public class PoolInvoker {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Object Pool Pattern Demo ===\n");

        // Демонстрация 1: Базовое использование
        basicUsageDemo();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // Демонстрация 2: Многопоточное использование
        concurrentUsageDemo();

        System.out.println("\n" + "=".repeat(50) + "\n");

    }

    /**
     * Базовое использование пула объектов.
     */
    private static void basicUsageDemo() throws Exception {
        System.out.println("ДЕМО 1: Базовое использование");
        System.out.println("-".repeat(50));
        System.out.println("Сценарий: Пул инициализируется с 2 объектами (minSize=2).");
        System.out.println("Мы берем один объект, используем его и возвращаем.");

        DatabaseConnectionFactory factory = new DatabaseConnectionFactory("localhost", 5432);

        // Создаем пул: минимум 2, максимум 5 соединений
        try (ItemPool<DatabaseConnection> pool = new BlockingItemPool<>(factory, 2, 5)) {

            System.out.println("\n1. Начальное состояние пула (создано 2 объекта):");
            System.out.println(pool.getStatistics());

            // Получаем соединение через try-with-resources
            // Благодаря тому, что DatabaseConnection реализует AutoCloseable, 
            // метод close() вызовется автоматически и вернет объект в пул.
            System.out.println("\n2. Запрашиваем соединение из пула...");
            try (DatabaseConnection conn = pool.acquire()) {
                System.out.println("\n3. Используем соединение:");
                String result = conn.executeQuery("SELECT * FROM users");
                System.out.println(result);

                System.out.println("\n4. Состояние пула (1 в использовании, 1 свободно):");
                System.out.println(pool.getStatistics());
            }
            // Здесь conn.close() уже вызвался автоматически

            System.out.println("\n5. Состояние после возврата объекта (снова 2 свободно):");
            System.out.println(pool.getStatistics());
        }

        System.out.println("\n6. Пул закрыт, все физические соединения разорваны.");
    }

    /**
     * Многопоточное использование пула.
     */
    private static void concurrentUsageDemo() throws Exception {
        System.out.println("ДЕМО 2: Конкурентное использование");
        System.out.println("-".repeat(50));
        System.out.println("Сценарий: 20 потоков пытаются одновременно получить доступ к пулу.");
        System.out.println("Максимальный размер пула - 10. Часть потоков будет ждать.");

        DatabaseConnectionFactory factory = new DatabaseConnectionFactory("localhost", 5432);

        try (ItemPool<DatabaseConnection> pool = new BlockingItemPool<>(factory, 3, 10)) {

            ExecutorService executor = Executors.newFixedThreadPool(20);
            CountDownLatch latch = new CountDownLatch(20);

            System.out.println("\nЗапускаем 20 конкурентных задач...\n");

            for (int i = 0; i < 20; i++) {
                final int taskId = i + 1;
                executor.submit(() -> {
                    try {
                        // Пытаемся получить соединение, ждем до 2 секунд
                        DatabaseConnection conn = pool.acquire(2000);

                        if (conn != null) {
                            try {
                                System.out.printf("Задача %2d: Получено %s%n", taskId, conn);
                                conn.executeQuery("SELECT data FROM table_" + taskId);
                                Thread.sleep(50 + (int) (Math.random() * 100)); // Симуляция работы
                            } finally {
                                pool.release(conn);
                                System.out.printf("Задача %2d: Соединение возвращено%n", taskId);
                            }
                        } else {
                            System.out.printf("Задача %2d: ТАЙМ-АУТ - не удалось дождаться свободного объекта%n", taskId);
                        }

                    } catch (Exception e) {
                        System.err.printf("Задача %2d: ОШИБКА - %s%n", taskId, e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
            executor.shutdown();

            System.out.println("\n" + "=".repeat(50));
            System.out.println("ФИНАЛЬНАЯ СТАТИСТИКА:");
            System.out.println(pool.getStatistics());
            System.out.println("=".repeat(50));
        }
    }
}
