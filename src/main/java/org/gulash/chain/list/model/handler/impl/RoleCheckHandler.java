package org.gulash.chain.list.model.handler.impl;

import org.gulash.chain.list.model.Request;
import org.gulash.chain.list.model.handler.Handler;

/**
 * Обработчик проверки ролей.
 * Проверяет наличие прав администратора для защищенных URL.
 */
public class RoleCheckHandler implements Handler {
    /**
     * Проверяет права доступа.
     * Если URL начинается с "/admin", проверяет наличие флага isAdmin.
     *
     * @param request объект запроса
     * @return true, если доступ разрешен и последующие обработчики вернули true; false в противном случае
     */
    @Override
    public boolean handle(Request request) {
        if ("/admin".equals(request.url()) && !request.isAdmin()) {
            System.out.println("RoleCheckHandler: Доступ запрещен. Требуются права администратора.");
            return false;
        }
        System.out.println("RoleCheckHandler: Проверка ролей пройдена.");
        return true;
    }
}
