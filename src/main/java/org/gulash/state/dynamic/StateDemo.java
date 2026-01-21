package org.gulash.state.dynamic;

import org.gulash.state.dynamic.context.OrderContext;

public class StateDemo {
    public static void main(String[] args) {
        OrderContext order = new OrderContext();
        System.out.println("Order state: " + order.getState());

        order.goToNextState();
        System.out.println("Order state: " + order.getState());

        order.goToNextState();
        System.out.println("Order state: " + order.getState());
    }
}
