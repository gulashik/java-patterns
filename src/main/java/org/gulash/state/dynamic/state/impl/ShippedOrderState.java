package org.gulash.state.dynamic.state.impl;

import org.gulash.state.dynamic.state.contract.OrderState;

public class ShippedOrderState implements OrderState {
    @Override
    public OrderState next() {
        return null; // already shipped and paid have no next state
    }

    @Override
    public String name() {
        return "order shipped to the customer";
    }
}
