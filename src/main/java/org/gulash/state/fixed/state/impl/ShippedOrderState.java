package org.gulash.state.fixed.state.impl;

import org.gulash.state.fixed.context.OrderContext;
import org.gulash.state.fixed.state.contract.OrderState;

public class ShippedOrderState implements OrderState {

    @Override
    public void pay(OrderContext context) {
        throw new IllegalStateException("Cannot pay shipped order");
    }

    @Override
    public void ship(OrderContext context) {
        throw new IllegalStateException("Order already shipped");
    }

    @Override
    public String name() {
        return "SHIPPED";
    }
}
