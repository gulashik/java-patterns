package org.gulash.chain.link.model;

/**
 * Модель HTTP-запроса.
 *
 * @param url     адрес запроса
 * @param body    тело запроса
 * @param token   токен авторизации
 * @param isAdmin флаг наличия прав администратора
 */
public record Request(String url, String body, String token, boolean isAdmin) {
}
