package org.gulash.state.fixed.state.contract;

import org.gulash.state.fixed.context.OrderContext;

/**
 * Базовый интерфейс всех состояний.
 * Определяет все возможные действия, которые могут быть выполнены
 * в различных состояниях автомата.
 */
public interface OrderState {
    void pay(OrderContext context);
    void ship(OrderContext context);
    String name();
}
