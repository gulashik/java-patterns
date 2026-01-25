package org.gulash.chain.link.handler.impl;

import org.gulash.chain.link.handler.Handler;
import org.gulash.chain.link.model.Request;

/**
 * Обработчик авторизации.
 * Проверяет наличие и корректность JWT токена в запросе.
 */
public class AuthHandler extends Handler {
    /**
     * Проверяет наличие и формат JWT токена.
     * Ожидается формат "Bearer <token>".
     *
     * @param request объект запроса
     * @return true, если токен валиден и последующие обработчики вернули true; false в противном случае
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
