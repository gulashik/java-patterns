package org.gulash.state.fixed.state.impl;

import org.gulash.state.fixed.context.OrderContext;
import org.gulash.state.fixed.state.contract.OrderState;

public class PaidOrderState implements OrderState {


    @Override
    public void pay(OrderContext context) {
        throw new IllegalStateException("Order already paid");
    }


    @Override
    public void ship(OrderContext context) {
        context.setState(new ShippedOrderState());
        System.out.println("Order shipped");
    }


    @Override
    public String name() {
        return "PAID";
    }
}
