package org.gulash.objectpool.factory;


import org.gulash.objectpool.factory.item.DatabaseConnection;
import org.gulash.objectpool.pool.ItemPool;

/**
 * Фабрика для создания соединений с базой данных.
 */
public class DatabaseConnectionFactory implements ItemFactory<DatabaseConnection> {
    private final String host;
    private final int port;

    public DatabaseConnectionFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public DatabaseConnection create(ItemPool<DatabaseConnection> objectPool) {
        // Фабрика делегирует создание самому объекту
        return new DatabaseConnection(host, port, objectPool);
    }

    @Override
    public boolean validate(DatabaseConnection connection) {
        // Проверка, живо ли еще соединение
        return connection.isValid();
    }

    @Override
    public void reset(DatabaseConnection connection) {
        // Сброс транзакций и параметров перед возвратом в пул
        connection.reset();
    }

    @Override
    public void destroy(DatabaseConnection connection) {
        // Окончательное закрытие ресурсов
        connection.destroy();
    }
}