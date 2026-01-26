package org.gulash.proxy;

// ============================================
// 1. Базовый интерфейс для объекта и прокси
// ============================================

/**
 * Единый интерфейс для реального объекта и прокси.
 * Это ключевой принцип паттерна - клиент не должен знать,
 * работает он с прокси или с реальным объектом.
 */
interface DatabaseService {
    void connect();
    String executeQuery(String query);
    void disconnect();
}

// ============================================
// 2. Реальный объект (Subject)
// ============================================

/**
 * Реальный сервис базы данных.
 * Содержит "тяжёлую" логику, которую мы хотим контролировать.
 */
class RealDatabaseService implements DatabaseService {
    private final String connectionString;
    private boolean connected = false;

    public RealDatabaseService(String connectionString) {
        this.connectionString = connectionString;
        // Симулируем долгую инициализацию
        System.out.println("⏳ Инициализация подключения к БД...");
        try {
            Thread.sleep(2000); // Эмуляция долгого подключения
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Реальное подключение создано: " + connectionString);
    }

    @Override
    public void connect() {
        if (!connected) {
            System.out.println("Подключение к БД установлено");
            connected = true;
        } else {
            System.out.println("Уже подключено");
        }
    }

    @Override
    public String executeQuery(String query) {
        if (!connected) {
            throw new IllegalStateException("Не подключено к БД!");
        }
        System.out.println("🔍 Выполнение запроса: " + query);
        // Эмуляция выполнения запроса
        return "Результат для: " + query;
    }

    @Override
    public void disconnect() {
        if (connected) {
            System.out.println("Отключение от БД");
            connected = false;
        }
    }
}

// ============================================
// 3. Virtual Proxy - Ленивая инициализация
// ============================================

/**
 * Virtual Proxy откладывает создание "тяжёлого" объекта
 * до первого реального использования.
 *
 * Use case: когда создание объекта затратно (БД, большие файлы),
 * но объект может не понадобиться.
 */
class LazyDatabaseProxy implements DatabaseService {
    private RealDatabaseService realService;
    private final String connectionString;

    public LazyDatabaseProxy(String connectionString) {
        this.connectionString = connectionString;
        System.out.println("Прокси создан (реальное подключение ещё не инициализировано)");
    }

    /**
     * Ленивая инициализация - создаём объект только при первом обращении
     */
    private RealDatabaseService getRealService() {
        if (realService == null) {
            System.out.println("Первое обращение - создаём реальный объект");
            realService = new RealDatabaseService(connectionString);
        }
        return realService;
    }

    @Override
    public void connect() {
        getRealService().connect();
    }

    @Override
    public String executeQuery(String query) {
        return getRealService().executeQuery(query);
    }

    @Override
    public void disconnect() {
        if (realService != null) {
            realService.disconnect();
        }
    }
}

// ============================================
// 4. Protection Proxy - Контроль доступа
// ============================================

/**
 * Роли пользователей для демонстрации контроля доступа
 */
enum UserRole {
    ADMIN, USER, GUEST
}

/**
 * Protection Proxy проверяет права доступа перед выполнением операций.
 *
 * Use case: разграничение прав доступа, аутентификация, авторизация.
 */
class ProtectedDatabaseProxy implements DatabaseService {
    private final DatabaseService realService;
    private final UserRole userRole;

    public ProtectedDatabaseProxy(DatabaseService realService, UserRole userRole) {
        this.realService = realService;
        this.userRole = userRole;
        System.out.println("🛡️ Защищённый прокси создан для роли: " + userRole);
    }

    @Override
    public void connect() {
        // Все могут подключаться
        realService.connect();
    }

    @Override
    public String executeQuery(String query) {
        // Проверяем права доступа
        if (!hasQueryPermission(query)) {
            String error = "ОТКАЗАНО: У роли " + userRole +
                " нет прав на выполнение запроса: " + query;
            System.out.println(error);
            throw new SecurityException(error);
        }

        System.out.println("Проверка прав пройдена для роли: " + userRole);
        return realService.executeQuery(query);
    }

    @Override
    public void disconnect() {
        realService.disconnect();
    }

    /**
     * Бизнес-логика проверки прав
     */
    private boolean hasQueryPermission(String query) {
        String upperQuery = query.toUpperCase();

        // GUEST может только SELECT
        if (userRole == UserRole.GUEST) {
            return upperQuery.startsWith("SELECT");
        }

        // USER может SELECT и INSERT
        if (userRole == UserRole.USER) {
            return upperQuery.startsWith("SELECT") ||
                upperQuery.startsWith("INSERT");
        }

        // ADMIN может всё
        return userRole == UserRole.ADMIN;
    }
}

// ============================================
// 5. Logging Proxy - Логирование
// ============================================

/**
 * Logging Proxy добавляет логирование всех операций.
 *
 * Use case: аудит, отладка, мониторинг производительности.
 */
class LoggingDatabaseProxy implements DatabaseService {
    private final DatabaseService realService;
    private int queryCount = 0;

    public LoggingDatabaseProxy(DatabaseService realService) {
        this.realService = realService;
        System.out.println("📝 Логирующий прокси создан");
    }

    @Override
    public void connect() {
        long startTime = System.currentTimeMillis();
        log("connect() - START");

        try {
            realService.connect();
            log("connect() - SUCCESS (" +
                (System.currentTimeMillis() - startTime) + "ms)");
        } catch (Exception e) {
            log("connect() - ERROR: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public String executeQuery(String query) {
        long startTime = System.currentTimeMillis();
        queryCount++;
        log("executeQuery() - START [Query #" + queryCount + "]");

        try {
            String result = realService.executeQuery(query);
            log("executeQuery() - SUCCESS (" +
                (System.currentTimeMillis() - startTime) + "ms)");
            return result;
        } catch (Exception e) {
            log("executeQuery() - ERROR: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void disconnect() {
        log("disconnect() - Total queries executed: " + queryCount);
        realService.disconnect();
    }

    private void log(String message) {
        System.out.println("📋 [LOG] " +
            java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
            ) + " - " + message);
    }
}

// ============================================
// 6. Caching Proxy - Кеширование
// ============================================

/**
 * Caching Proxy кеширует результаты для повышения производительности.
 *
 * Use case: когда одни и те же запросы выполняются многократно,
 * а данные меняются редко.
 */
class CachingDatabaseProxy implements DatabaseService {
    private final DatabaseService realService;
    private final java.util.Map<String, String> cache = new java.util.HashMap<>();
    private final java.util.Map<String, Long> cacheTimestamps = new java.util.HashMap<>();
    private final long cacheTTL; // Time To Live в миллисекундах

    public CachingDatabaseProxy(DatabaseService realService, long cacheTTLSeconds) {
        this.realService = realService;
        this.cacheTTL = cacheTTLSeconds * 1000;
        System.out.println("Кеширующий прокси создан (TTL: " +
            cacheTTLSeconds + "s)");
    }

    @Override
    public void connect() {
        realService.connect();
    }

    @Override
    public String executeQuery(String query) {
        // Проверяем, есть ли результат в кеше и не устарел ли он
        if (cache.containsKey(query)) {
            long cacheAge = System.currentTimeMillis() - cacheTimestamps.get(query);

            if (cacheAge < cacheTTL) {
                System.out.println("⚡ CACHE HIT для: " + query +
                    " (возраст: " + cacheAge + "ms)");
                return cache.get(query);
            } else {
                System.out.println("Кеш устарел для: " + query);
                cache.remove(query);
                cacheTimestamps.remove(query);
            }
        }

        System.out.println("CACHE MISS - выполняем реальный запрос");
        String result = realService.executeQuery(query);

        // Сохраняем в кеш
        cache.put(query, result);
        cacheTimestamps.put(query, System.currentTimeMillis());

        return result;
    }

    @Override
    public void disconnect() {
        System.out.println("Статистика кеша: " +
            cache.size() + " записей");
        realService.disconnect();
    }

    public void clearCache() {
        cache.clear();
        cacheTimestamps.clear();
        System.out.println("Кеш очищен");
    }
}

// ============================================
// 7. Composite Proxy - Цепочка прокси
// ============================================

/**
 * Демонстрирует композицию нескольких прокси.
 * Это мощный паттерн, позволяющий комбинировать функциональность.
 */
class ProxyChainBuilder {

    /**
     * Создаёт цепочку прокси: Logging -> Caching -> Protection -> Lazy -> Real
     */
    public static DatabaseService buildFullChain(String connectionString,
                                                 UserRole userRole) {
        System.out.println("\nПостроение цепочки прокси...\n");

        // 1. Самый внутренний - ленивая инициализация
        DatabaseService service = new LazyDatabaseProxy(connectionString);

        // 2. Добавляем защиту
        service = new ProtectedDatabaseProxy(service, userRole);

        // 3. Добавляем кеширование
        service = new CachingDatabaseProxy(service, 5); // 5 секунд TTL

        // 4. Самый внешний - логирование (будет видеть все вызовы)
        service = new LoggingDatabaseProxy(service);

        System.out.println("Цепочка прокси построена\n");
        return service;
    }
}

// ============================================
// 8. Демонстрация и Best Practices
// ============================================

public class ProxyPatternDemo {

    public static void main(String[] args) {
        demonstrateLazyProxy();
        demonstrateProtectionProxy();
        demonstrateCachingProxy();
        demonstrateProxyChain();
        demonstrateBestPractices();
    }

    /**
     * Демонстрация 1: Virtual Proxy (Ленивая инициализация)
     */
    private static void demonstrateLazyProxy() {
        printSection("VIRTUAL PROXY - Ленивая инициализация");

        // Создание прокси почти мгновенно
        DatabaseService db = new LazyDatabaseProxy("jdbc:mysql://localhost:3306/mydb");

        System.out.println("\n➡️ Прокси создан, но реальное подключение ещё НЕ создано");
        System.out.println("➡️ Можем создать множество прокси без затрат ресурсов\n");

        // Реальный объект создаётся только при первом обращении
        db.connect();
        db.executeQuery("SELECT * FROM users");
        db.disconnect();
    }

    /**
     * Демонстрация 2: Protection Proxy (Контроль доступа)
     */
    private static void demonstrateProtectionProxy() {
        printSection("PROTECTION PROXY - Контроль доступа");

        DatabaseService realDb = new LazyDatabaseProxy("jdbc:mysql://localhost:3306/mydb");

        // Тестируем разные роли
        testRole(realDb, UserRole.GUEST);
        testRole(realDb, UserRole.USER);
        testRole(realDb, UserRole.ADMIN);
    }

    private static void testRole(DatabaseService realDb, UserRole role) {
        System.out.println("\n--- Тестирование роли: " + role + " ---");
        DatabaseService db = new ProtectedDatabaseProxy(realDb, role);
        db.connect();

        tryQuery(db, "SELECT * FROM users");
        tryQuery(db, "INSERT INTO users VALUES (1, 'John')");
        tryQuery(db, "DELETE FROM users WHERE id = 1");

        db.disconnect();
    }

    private static void tryQuery(DatabaseService db, String query) {
        try {
            db.executeQuery(query);
        } catch (SecurityException e) {
            // Ожидаемое исключение при отсутствии прав
        }
    }

    /**
     * Демонстрация 3: Caching Proxy
     */
    private static void demonstrateCachingProxy() {
        printSection("CACHING PROXY - Кеширование результатов");

        DatabaseService realDb = new RealDatabaseService("jdbc:mysql://localhost:3306/mydb");
        CachingDatabaseProxy db = new CachingDatabaseProxy(realDb, 5);

        db.connect();

        // Первый запрос - идёт в БД
        System.out.println("\n🔹 Запрос 1:");
        db.executeQuery("SELECT * FROM products");

        // Второй такой же запрос - берётся из кеша
        System.out.println("\n🔹 Запрос 2 (тот же):");
        db.executeQuery("SELECT * FROM products");

        // Другой запрос - снова в БД
        System.out.println("\n🔹 Запрос 3 (другой):");
        db.executeQuery("SELECT * FROM orders");

        // Повтор первого - из кеша
        System.out.println("\n🔹 Запрос 4 (повтор первого):");
        db.executeQuery("SELECT * FROM products");

        db.disconnect();
    }

    /**
     * Демонстрация 4: Цепочка прокси (Best Practice)
     */
    private static void demonstrateProxyChain() {
        printSection("PROXY CHAIN - Композиция прокси");

        DatabaseService db = ProxyChainBuilder.buildFullChain(
            "jdbc:mysql://localhost:3306/mydb",
            UserRole.ADMIN
        );

        System.out.println("➡️ Первый запрос:");
        db.connect();
        db.executeQuery("SELECT * FROM users");

        System.out.println("\n➡️ Повторный запрос (будет закеширован):");
        db.executeQuery("SELECT * FROM users");

        db.disconnect();
    }

    /**
     * Best Practices и подводные камни
     */
    private static void demonstrateBestPractices() {
        printSection("BEST PRACTICES и подводные камни");

        System.out.println("""
            ✅ BEST PRACTICES:
            
            1. Единый интерфейс
               - Прокси и реальный объект должны реализовывать один интерфейс
               - Клиент не должен знать, работает он с прокси или реальным объектом
            
            2. Прозрачность
               - Прокси не должен менять поведение реального объекта
               - Только добавляет функциональность, не изменяет основную логику
            
            3. Композиция прокси
               - Можно создавать цепочки: Logging -> Caching -> Protection -> Real
               - Порядок важен! Логирование снаружи видит все операции
            
            4. Ленивая инициализация
               - Используйте для "тяжёлых" объектов
               - Создавайте реальный объект только при первом обращении
            
            5. Потокобезопасность
               - Для многопоточных приложений используйте синхронизацию
               - Особенно важно для кеширующих и ленивых прокси
            
            ПОДВОДНЫЕ КАМНИ:
            
            1. Избыточность
               - Не используйте прокси, если он не даёт реальной пользы
               - Каждый уровень прокси добавляет overhead
            
            2. Утечки абстракций
               - Прокси не должен раскрывать детали реализации
               - Исключения должны быть согласованы с интерфейсом
            
            3. Кеширование
               - Важно учитывать invalidation кеша
               - Установите разумный TTL
               - Помните о памяти - кеш может расти
            
            4. Циклические зависимости
               - Осторожно с прокси, которые создают другие прокси
               - Может привести к бесконечной рекурсии
            
            5. Тестирование
               - Тестируйте прокси отдельно от реального объекта
               - Используйте моки для изоляции тестов
            
            6. Производительность
               - Измеряйте overhead от прокси
               - Для высоконагруженных систем критично
            """);
    }

    private static void printSection(String title) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  " + title);
        System.out.println("=".repeat(60) + "\n");
    }
}
