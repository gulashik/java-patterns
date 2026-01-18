package org.gulash.objectpool.pool;

import org.gulash.objectpool.factory.*;
import org.gulash.objectpool.pool.util.PoolStatistics;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * Thread-safe реализация пула объектов с использованием BlockingQueue.
 * <p>
 * ОСОБЕННОСТИ:
 * - Потокобезопасная
 * - Поддержка тайм-аутов
 * - Автоматическое создание объектов при необходимости
 * - Валидация объектов
 * - Сбор статистики
 * <p>
 * BEST PRACTICES:
 * - Используйте try-with-resources или явный release в finally
 * - Настройте минимальный и максимальный размер пула
 * - Установите разумный тайм-аут для acquire
 * - Мониторьте статистику для выявления проблем
 */
public class BlockingItemPool<T> implements ItemPool<T> {

    /**
     * Фабрика для создания и управления объектами пула
     */
    private final ItemFactory<T> factory;

    /**
     * Очередь доступных для использования объектов (thread-safe)
     */
    private final BlockingQueue<T> availableObjects;

    /**
     * Объекты, находящиеся в использовании (ключ - объект, значение - время взятия из пула)
     */
    private final ConcurrentHashMap<T, Long> inUseObjects;

    /**
     * Минимальное количество объектов, которое должно быть создано при инициализации пула
     */
    private final int minSize;

    /**
     * Максимальное количество объектов, которое может существовать одновременно
     */
    private final int maxSize;

    /**
     * Текущее общее количество созданных объектов (свободные + используемые)
     */
    private final AtomicInteger currentSize;

    /**
     * Флаг состояния пула (false - работает, true - закрыт)
     */
    private final AtomicBoolean closed;

    // Статистика использования пула

    /**
     * Общее количество успешных запросов объектов из пула
     */
    private final AtomicLong totalAcquires;

    /**
     * Общее количество возвратов объектов в пул
     */
    private final AtomicLong totalReleases;

    /**
     * Количество случаев, когда объект не был получен из-за истечения времени ожидания
     */
    private final AtomicLong timeoutCount;

    /**
     * Суммарное время ожидания получения объектов (в миллисекундах)
     */
    private final AtomicLong totalWaitTime;

    /**
     * Создать пул с заданными параметрами.
     *
     * @param factory фабрика для создания объектов
     * @param minSize минимальное количество объектов (eager initialization)
     * @param maxSize максимальное количество объектов
     */
    public BlockingItemPool(ItemFactory<T> factory, int minSize, int maxSize) {
        if (minSize < 0 || maxSize < minSize) {
            throw new IllegalArgumentException("Invalid pool size: min=" + minSize + ", max=" + maxSize);
        }

        this.factory = factory;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.currentSize = new AtomicInteger(0);
        this.closed = new AtomicBoolean(false);
        this.availableObjects = new LinkedBlockingQueue<>();

        this.totalAcquires = new AtomicLong(0);
        this.totalReleases = new AtomicLong(0);
        this.timeoutCount = new AtomicLong(0);
        this.inUseObjects = new ConcurrentHashMap<>();
        this.totalWaitTime = new AtomicLong(0);

        // Eager initialization - создаем минимальное количество объектов
        initializeMinObjects();
    }

    private void initializeMinObjects() {
        for (int i = 0; i < minSize; i++) {
            try {
                T object = factory.create(this);
                availableObjects.offer(object);
                currentSize.incrementAndGet();
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize pool", e);
            }
        }
    }

    @Override
    public T acquire(long timeoutMs) throws InterruptedException {
        if (closed.get()) {
            throw new IllegalStateException("Пул закрыт");
        }

        long startTime = System.currentTimeMillis();
        totalAcquires.incrementAndGet();

        // 1. Пытаемся взять готовый объект из очереди
        T object = availableObjects.poll(timeoutMs, TimeUnit.MILLISECONDS);

        // 2. Если объектов нет, но мы еще не достигли лимита - пробуем создать новый
        if (object == null && currentSize.get() < maxSize) {
            synchronized (this) {
                if (currentSize.get() < maxSize) {
                    object = factory.create(this);
                    currentSize.incrementAndGet();
                    finalizeAcquire(object, startTime);
                    return object;
                }
            }
        }

        // 3. Если мы здесь, значит либо получили объект из очереди, либо очередь пуста и лимит достигнут
        if (object == null) {
            // Если вышли по тайм-ауту (вторичное ожидание не требуется, poll уже подождал timeoutMs)
            timeoutCount.incrementAndGet();
            return null;
        }

        // 4. Валидация объекта перед выдачей
        if (!factory.validate(object)) {
            destroyObject(object);
            // Пытаемся получить другой объект, уменьшив оставшееся время
            long elapsed = System.currentTimeMillis() - startTime;
            return acquire(Math.max(0, timeoutMs - elapsed));
        }

        finalizeAcquire(object, startTime);
        return object;
    }

    private void finalizeAcquire(T object, long startTime) {
        long waitTime = System.currentTimeMillis() - startTime;
        totalWaitTime.addAndGet(waitTime);
        inUseObjects.put(object, System.currentTimeMillis());
    }

    private void destroyObject(T object) {
        try {
            factory.destroy(object);
        } catch (Exception e) {
            System.err.println("Ошибка при уничтожении объекта: " + e.getMessage());
        } finally {
            currentSize.decrementAndGet();
            inUseObjects.remove(object);
        }
    }

    @Override
    public void release(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Нельзя вернуть null объект");
        }

        // Проверяем, что объект принадлежит этому пулу (был выдан нами)
        if (!inUseObjects.containsKey(object)) {
            throw new IllegalArgumentException("Объект не принадлежит этому пулу");
        }

        inUseObjects.remove(object);
        totalReleases.incrementAndGet();

        try {
            // Сбрасываем состояние объекта для чистого повторного использования
            factory.reset(object);

            if (!closed.get()) {
                // Возвращаем в очередь свободных объектов
                availableObjects.offer(object);
            } else {
                // Пул закрывается, лишние объекты не нужны
                destroyObject(object);
            }
        } catch (Exception e) {
            // Если сброс не удался, лучше уничтожить объект от греха подальше
            destroyObject(object);
        }
    }

    @Override
    public PoolStatistics getStatistics() {
        long avgWaitTime = totalAcquires.get() > 0
            ? totalWaitTime.get() / totalAcquires.get()
            : 0;

        return new PoolStatistics(
            currentSize.get(),
            availableObjects.size(),
            inUseObjects.size(),
            totalAcquires.get(),
            totalReleases.get(),
            timeoutCount.get(),
            avgWaitTime
        );
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            // Уничтожаем все доступные объекты
            T object;
            while ((object = availableObjects.poll()) != null) {
                try {
                    factory.destroy(object);
                    currentSize.decrementAndGet();
                } catch (Exception e) {
                    System.err.println("Error destroying object: " + e.getMessage());
                }
            }

            // ВАЖНО: объекты, которые еще используются, будут уничтожены при release
        }
    }
}
