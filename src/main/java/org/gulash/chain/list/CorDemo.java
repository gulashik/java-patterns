package org.gulash.chain.list;


import org.gulash.chain.list.model.Chain;
import org.gulash.chain.list.model.handler.impl.AuthHandler;
import org.gulash.chain.list.model.handler.impl.RoleCheckHandler;
import org.gulash.chain.list.model.handler.impl.ValidationHandler;
import org.gulash.chain.list.model.Request;

/**
 * Демонстрация работы паттерна Chain of Responsibility (Цепочка обязанностей).
 */
public class CorDemo {

    /**
     * Точка входа в демонстрационную программу.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        // 1. Создаем цепочку обработчиков

        Chain handlerChain = Chain.builder()
            .register(new AuthHandler())
            .register(new RoleCheckHandler())
            .register(new ValidationHandler())
            .build();

        Chain handlerChainEmpty = Chain.builder().build();

        // 2. Тестируем различные сценарии

        System.out.println("--- Сценарий 1: Успешный запрос ---");
        Request successRequest = new Request("/admin", "Update Data", "Bearer valid-token", true);
        System.out.println("------");
        process(handlerChain, successRequest);
        System.out.println("------");
        process(handlerChainEmpty, successRequest);

        System.out.println("\n--- Сценарий 2: Ошибка авторизации ---");
        Request failAuth = new Request("/admin", "Update Data", null, true);
        System.out.println("------");
        process(handlerChain, failAuth);
        System.out.println("------");
        process(handlerChainEmpty, failAuth);

        System.out.println("\n--- Сценарий 3: Ошибка прав доступа ---");
        Request failRole = new Request("/admin", "Update Data", "Bearer valid-token", false);
        System.out.println("------");
        process(handlerChain, failRole);
        System.out.println("------");
        process(handlerChainEmpty, failRole);

        System.out.println("\n--- Сценарий 4: Ошибка валидации ---");
        Request failValidation = new Request("/user", "", "Bearer valid-token", false);
        System.out.println("------");
        process(handlerChain, failValidation);
        System.out.println("------");
        process(handlerChainEmpty, failValidation);
    }

    /**
     * Запускает процесс обработки запроса через цепочку.
     *
     * @param chain   голова цепочки обработчиков
     * @param request объект запроса
     */
    private static void process(Chain chain, Request request) {
        if (chain.handleAll(request)) {
            System.out.println("РЕЗУЛЬТАТ: Запрос успешно обработан.");
        } else {
            System.out.println("РЕЗУЛЬТАТ: Запрос отклонен.");
        }
    }
}
