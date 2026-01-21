package org.gulash.state.fixed.state.impl;

import org.gulash.state.fixed.context.OrderContext;
import org.gulash.state.fixed.state.contract.OrderState;

public class NewOrderState implements OrderState {


    @Override
    public void pay(OrderContext context) {
        context.setState(new PaidOrderState());
        System.out.println("Order paid");
    }


    @Override
    public void ship(OrderContext context) {
        throw new IllegalStateException("Cannot ship unpaid order");
    }


    @Override
    public String name() {
        return "NEW";
    }
}
