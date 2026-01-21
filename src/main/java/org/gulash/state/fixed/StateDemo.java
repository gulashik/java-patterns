package org.gulash.state.fixed;

import org.gulash.state.fixed.context.OrderContext;

public class StateDemo {
    public static void main(String[] args) {
        OrderContext order = new OrderContext();
        System.out.println(order.getState());

        order.pay();
        System.out.println(order.getState());

        order.ship();
        System.out.println(order.getState());
    }
}
