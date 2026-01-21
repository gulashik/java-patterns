package org.gulash.state.fixed.context;

import org.gulash.state.fixed.state.contract.OrderState;
import org.gulash.state.fixed.state.impl.NewOrderState;

/**
 * Контекст - это класс, поведение которого меняется в зависимости от состояния.
 * Он хранит ссылку на объект состояния и делегирует ему всю работу.
 */
public class OrderContext {

    private OrderState state;

    public OrderContext() {
        this.state = new NewOrderState();
    }

    public void pay() {
        state.pay(this);
    }

    public void ship() {
        state.ship(this);
    }

    public String getState() {
        return state.name();
    }

    public void setState(OrderState state) {
        this.state = state;
    }
}
