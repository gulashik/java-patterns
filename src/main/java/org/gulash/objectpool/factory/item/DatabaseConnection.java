package org.gulash.objectpool.factory.item;


import org.gulash.objectpool.pool.ItemPool;

/**
 * Пример дорогостоящего объекта - соединение с базой данных.
 * <p>
 * Почему это "дорогой" объект?
 * 1. Создание требует времени (TCP handshake, аутентификация, аллокация памяти в БД).
 * 2. Количество одновременных соединений в БД ограничено (max_connections).
 * <p>
 * DatabaseConnection реализует AutoCloseable для удобного использования в try-with-resources,
 * но вместо закрытия физического соединения оно возвращает себя в пул.
 */
public class DatabaseConnection implements AutoCloseable {
    private final String id;
    private final String host;
    private final int port;
    private final ItemPool<DatabaseConnection> parentPool;
    private boolean connected;
    private int queryCount;

    public DatabaseConnection(String host, int port, ItemPool<DatabaseConnection> parentPool) {
        this.id = generateId();
        this.host = host;
        this.port = port;
        this.parentPool = parentPool;

        // Симулируем дорогостоящее создание соединения (100-500ms).
        // В реальной жизни здесь была бы сетевая задержка и тяжелые операции.
        try {
            Thread.sleep(100 + (int) (Math.random() * 400));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.connected = true;
        this.queryCount = 0;

        System.out.println("✓ Физическое создание нового соединения: " + id);
    }

    /**
     * Симуляция полезной работы объекта.
     */
    public String executeQuery(String sql) {
        if (!connected) {
            throw new IllegalStateException("Соединение закрыто!");
        }

        queryCount++;

        // Симулируем выполнение запроса
        try {
            Thread.sleep(10 + (int) (Math.random() * 20));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "Результат от " + id + " для запроса: " + sql;
    }

    /**
     * Валидация объекта. Пул вызывает этот метод, чтобы проверить,
     * можно ли еще использовать это соединение или оно "протухло".
     */
    public boolean isValid() {
        // Например, мы ограничиваем жизнь соединения 100 запросами
        return connected && queryCount < 100;
    }

    /**
     * Сброс состояния перед возвратом в пул.
     */
    public void reset() {
        System.out.println("↻ Очистка состояния соединения (сброс транзакций и т.д.): " + id);
    }

    /**
     * Метод close() здесь НЕ закрывает физическое соединение,
     * а возвращает объект в пул. Это ключевая особенность реализации пула,
     * прозрачной для клиента.
     */
    @Override
    public void close() {
        parentPool.release(this);

        System.out.println("← Соединение " + id + " возвращено в пул клиентом");
    }

    /**
     * Действительное закрытие физического соединения.
     * Вызывается только фабрикой/пулом при уничтожении объекта.
     */
    public void destroy() {
        connected = false;
        System.out.println("✗ Физическое закрытие соединения: " + id);
    }

    private String generateId() {
        return "DB-" + System.currentTimeMillis() + "-" + (int) (Math.random() * 1000);
    }

    @Override
    public String toString() {
        return String.format("DatabaseConnection{id='%s', host='%s', port=%d, queries=%d}",
            id, host, port, queryCount);
    }
}
