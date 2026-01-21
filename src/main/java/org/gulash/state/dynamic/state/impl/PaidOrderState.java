package org.gulash.state.dynamic.state.impl;

import org.gulash.state.dynamic.state.contract.OrderState;

public class PaidOrderState implements OrderState {
    @Override
    public OrderState next() {
        return new ShippedOrderState();
    }

    @Override
    public String name() {
        return "order paid";
    }
}
