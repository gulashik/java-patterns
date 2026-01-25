package org.gulash.chain.link;

import org.gulash.chain.link.handler.Handler;
import org.gulash.chain.link.handler.impl.AuthHandler;
import org.gulash.chain.link.handler.impl.RoleCheckHandler;
import org.gulash.chain.link.handler.impl.ValidationHandler;
import org.gulash.chain.link.model.Request;

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
        Handler chain = Handler.link(
                new AuthHandler(),
                new RoleCheckHandler(),
                new ValidationHandler()
        );

        Handler handlerChain = Handler.builder()
            .register(new AuthHandler())
            .register(new RoleCheckHandler())
            .register(new ValidationHandler())
            .buildChain();

        Handler handlerChainEmpty = Handler.builder()
            .buildChain();

        // 2. Тестируем различные сценарии

        System.out.println("--- Сценарий 1: Успешный запрос ---");
        Request successRequest = new Request("/admin", "Update Data", "Bearer valid-token", true);
        System.out.println("------");
        process(chain, successRequest);
        System.out.println("------");
        process(handlerChain, successRequest);
        System.out.println("------");
        process(handlerChainEmpty, successRequest);

        System.out.println("\n--- Сценарий 2: Ошибка авторизации ---");
        Request failAuth = new Request("/admin", "Update Data", null, true);
        System.out.println("------");
        process(chain, failAuth);
        System.out.println("------");
        process(handlerChain, failAuth);
        System.out.println("------");
        process(handlerChainEmpty, failAuth);

        System.out.println("\n--- Сценарий 3: Ошибка прав доступа ---");
        Request failRole = new Request("/admin", "Update Data", "Bearer valid-token", false);
        System.out.println("------");
        process(chain, failRole);
        System.out.println("------");
        process(handlerChain, failRole);
        System.out.println("------");
        process(handlerChainEmpty, failRole);

        System.out.println("\n--- Сценарий 4: Ошибка валидации ---");
        Request failValidation = new Request("/user", "", "Bearer valid-token", false);
        System.out.println("------");
        process(chain, failValidation);
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
    private static void process(Handler chain, Request request) {
        if (chain.handle(request)) {
            System.out.println("РЕЗУЛЬТАТ: Запрос успешно обработан.");
        } else {
            System.out.println("РЕЗУЛЬТАТ: Запрос отклонен.");
        }
    }
}
