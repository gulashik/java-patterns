package org.gulash.adapter.adapter.target;

import java.math.BigDecimal;

public interface PaymentProcessor {
    boolean pay(BigDecimal amount);
}
