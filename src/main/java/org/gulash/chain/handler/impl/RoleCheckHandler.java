package org.gulash.chain.handler.impl;

import org.gulash.chain.handler.Handler;
import org.gulash.chain.model.Request;

/**
 * Обработчик проверки ролей.
 * Проверяет наличие прав администратора для защищенных URL.
 */
public class RoleCheckHandler extends Handler {
    /**
     * Проверяет права доступа.
     *
     * @param request объект запроса
     * @return true, если доступ разрешен и последующие обработчики вернули true
     */
    @Override
    public boolean handle(Request request) {
        if ("/admin".equals(request.url()) && !request.isAdmin()) {
            System.out.println("RoleCheckHandler: Доступ запрещен. Требуются права администратора.");
            return false;
        }
        System.out.println("RoleCheckHandler: Проверка ролей пройдена.");
        return handleNext(request);
    }
}
