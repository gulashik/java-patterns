package org.gulash.decorator;

import org.gulash.decorator.decorators.impl.SlackDecorator;
import org.gulash.decorator.decorators.impl.SmsDecorator;
import org.gulash.decorator.impl.EmailNotifier;

/**
 * Класс-демонстрация паттерна Декоратор.
 * 
 * Декоратор — это структурный паттерн проектирования, который позволяет динамически 
 * добавлять объектам новые обязанности, оборачивая их в объекты-обертки.
 */
public class DecoratorDemo {
    public static void main(String[] args) {
        printSection("1. Базовое уведомление (только Email)");
        Notifier simpleEmail = new EmailNotifier("user@example.com");
        simpleEmail.send("Привет! Это твое первое уведомление.");

        printSection("2. Уведомление с SMS (Email + SMS)");
        // Оборачиваем EmailNotifier в SmsDecorator
        Notifier emailAndSms = new SmsDecorator(
                new EmailNotifier("user@example.com"),
                "+7-999-123-45-67"
        );
        emailAndSms.send("На сервере произошел сбой!");

        printSection("3. Полный набор (Email + SMS + Slack)");
        // Оборачиваем уже созданный сэндвич еще в один декоратор
        Notifier fullStack = new SlackDecorator(
                new SmsDecorator(
                        new EmailNotifier("admin@example.com"),
                        "+1-555-010-99"
                ),
                "admin_dev_team"
        );
        fullStack.send("Критическая ошибка: База данных недоступна!");

        printSection("4. Почему это круто?");
        System.out.println("Мы можем комбинировать декораторы в любом порядке и составе в рантайме.");
        System.out.println("Например, только Email + Slack:");
        Notifier emailAndSlack = new SlackDecorator(new EmailNotifier("dev@example.com"), "dev_channel");
        emailAndSlack.send("Деплой завершен успешно.");
    }

    private static void printSection(String title) {
        System.out.println("\n=== " + title + " ===");
    }
}
