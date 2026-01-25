package org.gulash.chain.link.handler.impl;

import org.gulash.chain.link.handler.Handler;
import org.gulash.chain.link.model.Request;

/**
 * Обработчик валидации.
 * Проверяет корректность тела запроса.
 */
public class ValidationHandler extends Handler {
    /**
     * Выполняет валидацию данных.
     * Проверяет, что тело запроса не является пустым или null.
     *
     * @param request объект запроса
     * @return true, если данные валидны и последующие обработчики вернули true; false в противном случае
     */
    @Override
    public boolean handle(Request request) {
        if (request.body() == null || request.body().isEmpty()) {
            System.out.println("ValidationHandler: Тело запроса пусто.");
            return false;
        }
        System.out.println("ValidationHandler: Валидация успешна.");
        return handleNext(request);
    }
}
