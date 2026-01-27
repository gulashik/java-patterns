package org.gulash.decorator.impl;

import org.gulash.decorator.Notifier;

/**
 * Базовая реализация компонента, которая выполняет основную работу.
 */
public class EmailNotifier implements Notifier {
    private final String email;

    public EmailNotifier(String email) {
        this.email = email;
    }

    @Override
    public void send(String message) {
        System.out.println("[Email] Отправка сообщения на " + email + ": " + message);
    }
}
