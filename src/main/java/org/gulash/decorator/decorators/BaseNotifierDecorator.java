package org.gulash.decorator.decorators;

import org.gulash.decorator.Notifier;

/**
 * Базовый класс декоратора.
 * Он следует тому же интерфейсу, что и другие компоненты.
 * Основная цель этого класса
 * <ul>
 *     <li>DRY принцип (общая логика)</li>
 *     <li>Частичное переопределение в конкретных декораторах</li>
 * <ul/>
 */
public abstract class BaseNotifierDecorator implements Notifier {
    protected final Notifier wrapper;

    protected BaseNotifierDecorator(Notifier wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public void send(String message) {
        // По умолчанию базовый декоратор просто делегирует работу обернутому объекту.
        wrapper.send(message);
    }
}
