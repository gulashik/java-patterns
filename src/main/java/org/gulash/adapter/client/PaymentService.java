package org.gulash.adapter.client;

import org.gulash.adapter.adapter.target.PaymentProcessor;

import java.math.BigDecimal;

public class PaymentService {

    private final PaymentProcessor processor;

    public PaymentService(PaymentProcessor processor) {
        this.processor = processor;
    }

    public void processOrder(BigDecimal price) {
        if (!processor.pay(price)) {
            throw new IllegalStateException("Payment failed");
        }
        System.out.println("Order paid successfully");
    }
}

