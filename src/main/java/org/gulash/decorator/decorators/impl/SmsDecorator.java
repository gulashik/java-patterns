package org.gulash.decorator.decorators.impl;

import org.gulash.decorator.Notifier;
import org.gulash.decorator.decorators.BaseNotifierDecorator;

/**
 * Конкретный декоратор добавляет новую функциональность (SMS) к базовому объекту.
 */
public class SmsDecorator extends BaseNotifierDecorator {
    private final String phoneNumber;

    public SmsDecorator(Notifier wrapper, String phoneNumber) {
        super(wrapper);
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void send(String message) {
        // Сначала вызываем базовое поведение обернутого объекта
        super.send(message);
        // Затем выполняем добавочное действие
        sendSms(message);
    }

    private void sendSms(String message) {
        System.out.println("[SMS] Отправка SMS на номер " + phoneNumber + ": " + message);
    }
}
