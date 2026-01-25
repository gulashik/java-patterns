package org.gulash.chain.link.handler;

import org.gulash.chain.link.handler.impl.DefaultHandler;
import org.gulash.chain.link.model.Request;

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
     * Позволяет удобно выстраивать цепочку: link(h1, h2, h3) -> h1.next=h2, h2.next=h3.
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
     * Создает новый экземпляр строителя цепочки.
     *
     * @return {@link HandlerBuilder}
     */
    public static HandlerBuilder builder() {
        return new HandlerBuilder();
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
         * Добавляет {@link DefaultHandler} в конец цепочки в качестве финального звена.
         *
         * @return первый обработчик в цепочке или {@link DefaultHandler}, если список был пуст
         */
        public Handler buildChain() {
            chain.add(new DefaultHandler());
            chain.stream().reduce(
                (previous, current) -> {
                    previous.next = current; return current;
                }
            );
            return chain.getFirst();

//            или более привычно
//            chain.add(new DefaultHandler());
//            for (int i = 0; i < chain.size() - 1; i++) {
//                chain.get(i).next = chain.get(i + 1);
//            }
//            return chain.getFirst();

//            или красиво
//            chain.add(new DefaultHandler());
//            IntStream.range(1, chain.size())
//                .forEach( i -> { chain.get(i - 1).next = chain.get(i); } );
//            return chain.getFirst();
        }
    }
}
