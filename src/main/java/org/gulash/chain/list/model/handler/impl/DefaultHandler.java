package org.gulash.chain.list.model.handler.impl;

import org.gulash.chain.list.model.Request;
import org.gulash.chain.list.model.handler.Handler;


/**
 * Обработчик по умолчанию.
 * Обычно является финальным звеном в цепочке.
 * Подтверждает успешное прохождение всех предыдущих этапов.
 */
public class DefaultHandler implements Handler {
    /**
     * Завершает обработку запроса.
     *
     * @param request объект запроса
     * @return всегда true, так как это конечное звено
     */
    @Override
    public boolean handle(Request request) {
        System.out.println("DefaultHandler: Все проверки пройдены успешно.");
        return true;
    }
}
