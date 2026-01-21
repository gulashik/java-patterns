package org.gulash.state.dynamic.context;

import org.gulash.state.dynamic.state.contract.OrderState;
import org.gulash.state.dynamic.state.impl.NewOrderState;

/**
 * Контекст - это класс, поведение которого меняется в зависимости от состояния.
 * Он хранит ссылку на объект состояния и делегирует ему всю работу.
 */
public class OrderContext {
    private OrderState currentState;

    public OrderContext() {
        this.currentState = new NewOrderState();
    }
    public void goToNextState() {
        currentState = currentState.next();
    }
    public String getState() {
        return currentState.name();
    }
}
