package org.gulash.state.dynamic.state.impl;


import org.gulash.state.dynamic.state.contract.OrderState;

public class NewOrderState implements OrderState {
    @Override
    public OrderState next() {
        return new PaidOrderState();
    }

    @Override
    public String name() {
        return "new order created";
    }
}
