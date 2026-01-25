package org.gulash.chain.list.model;

import org.gulash.chain.list.model.handler.Handler;
import org.gulash.chain.list.model.handler.impl.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Базовый абстрактный класс для всех обработчиков в цепочке обязанностей.
 * <p>
 * Паттерн Chain of Responsibility позволяет передавать запросы по цепочке обработчиков.
 * Каждый обработчик решает, может ли он обработать запрос сам или должен передать его следующему.
 */
public class Chain {

    private final List<Handler> handlerChain;

    private Chain(List<Handler> handlerChain) {
        this.handlerChain = handlerChain;
    }

    public boolean handleAll(Request request) {
        for (Handler handler : handlerChain) {
            if (!handler.handle(request)) {
                return false;
            }
        }
        return true;
    }

    public static ChainBuilder builder() {
        return new ChainBuilder();
    }

    /**
     * Вспомогательный класс для удобного построения цепочки обработчиков.
     */
    public static class ChainBuilder {
        private final List<Handler> chain = new ArrayList<>();

        public ChainBuilder register(Handler handler) {
            chain.add(handler);
            return this;
        }

        public Chain build() {
            chain.add(new DefaultHandler());
            return new Chain(chain);
        }
    }
}
