package org.gulash.chain.handler.impl;

import org.gulash.chain.handler.Handler;
import org.gulash.chain.model.Request;


/**
 * Обработчик по умолчанию.
 * Обычно является финальным звеном в цепочке.
 * Подтверждает успешное прохождение всех предыдущих этапов.
 */
public class DefaultHandler extends Handler {
    /**
     * Завершает обработку запроса.
     *
     * @param request объект запроса
     * @return всегда true, так как это конечное звено
     */
    @Override
    public boolean handle(Request request) {
        /* есть проверка в методе на null в следующем элементе и этот Handler особо не нужен*/
        System.out.println("DefaultHandler: Все проверки пройдены успешно.");
        return true;
    }
}
