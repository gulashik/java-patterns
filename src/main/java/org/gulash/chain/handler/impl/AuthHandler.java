package org.gulash.chain.handler.impl;

import org.gulash.chain.handler.Handler;
import org.gulash.chain.model.Request;

/**
 * Обработчик авторизации.
 * Проверяет наличие и корректность JWT токена в запросе.
 */
public class AuthHandler extends Handler {
    /**
     * Проверяет авторизацию.
     *
     * @param request объект запроса
     * @return true, если токен валиден и последующие обработчики вернули true
     */
    @Override
    public boolean handle(Request request) {
        if (request.token() == null || !request.token().startsWith("Bearer ")) {
            System.out.println("AuthHandler: Ошибка авторизации. Токен отсутствует или невалиден.");
            return false;
        }
        System.out.println("AuthHandler: Авторизация успешна.");
        return handleNext(request);
    }
}
