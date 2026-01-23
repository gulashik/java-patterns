package org.gulash.chain.handler.impl;

import org.gulash.chain.handler.Handler;
import org.gulash.chain.model.Request;

/**
 * Обработчик валидации.
 * Проверяет корректность тела запроса.
 */
public class ValidationHandler extends Handler {
    /**
     * Выполняет валидацию данных.
     *
     * @param request объект запроса
     * @return true, если тело запроса не пустое и последующие обработчики вернули true
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
