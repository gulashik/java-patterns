package org.gulash.chain.link.handler.impl;

import org.gulash.chain.link.handler.Handler;
import org.gulash.chain.link.model.Request;

/**
 * Обработчик проверки ролей.
 * Проверяет наличие прав администратора для защищенных URL.
 */
public class RoleCheckHandler extends Handler {
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
        return handleNext(request);
    }
}
