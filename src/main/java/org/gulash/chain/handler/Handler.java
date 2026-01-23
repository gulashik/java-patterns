package org.gulash.chain.handler;

import org.gulash.chain.handler.impl.DefaultHandler;
import org.gulash.chain.model.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Базовый абстрактный класс для всех обработчиков в цепочке обязанностей.
 * <p>
 * Паттерн Chain of Responsibility позволяет передавать запросы по цепочке обработчиков.
 * Каждый обработчик решает, может ли он обработать запрос сам или должен передать его следующему.
 */
public abstract class Handler {
    /**
     * Ссылка на следующий обработчик в цепочке.
     */
    private Handler next;

    /**
     * Статический метод для связывания нескольких обработчиков в одну цепочку.
     *
     * @param first первый обработчик цепочки
     * @param chain последующие обработчики
     * @return первый элемент цепочки (head)
     */
    public static Handler link(Handler first, Handler... chain) {
        Handler head = first;
        for (Handler nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    /**
     * Создает новый экземпляр строителя цепочки.
     *
     * @return {@link HandlerBuilder}
     */
    public static HandlerBuilder builder() {
        return new HandlerBuilder();
    }

    /**
     * Основной метод обработки запроса.
     * Реализации должны определить логику обработки и при необходимости вызвать {@link #handleNext(Request)}.
     *
     * @param request объект запроса для обработки
     * @return true, если запрос успешно прошел через текущий обработчик (и всю цепочку), иначе false
     */
    public abstract boolean handle(Request request);

    /**
     * Передает запрос следующему обработчику в цепочке.
     * Если это последний элемент, возвращает true (успешное завершение цепочки).
     *
     * @param request объект запроса
     * @return результат выполнения следующего обработчика или true, если цепочка окончена
     */
    protected boolean handleNext(Request request) {
        if (next == null) {
            return true;
        }
        return next.handle(request);
    }

    /**
     * Вспомогательный класс для удобного построения цепочки обработчиков.
     */
    public static class HandlerBuilder {
        private final List<Handler> chain = new ArrayList<>();

        /**
         * Регистрирует новый обработчик в списке строителя.
         *
         * @param handler обработчик
         * @return текущий экземпляр строителя
         */
        public HandlerBuilder register(Handler handler) {
            chain.add(handler);
            return this;
        }

        /**
         * Связывает все зарегистрированные обработчики и возвращает голову цепочки.
         *
         * @return первый обработчик в цепочке или null, если список пуст
         */
        public Handler buildChain() {
            chain.add(new DefaultHandler());
            chain.stream().reduce(
                (previous, current) -> {
                    previous.next = current; return current;}
            );
            return chain.getFirst();
        }
    }
}
