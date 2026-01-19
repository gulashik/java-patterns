package org.gulash.adapter.adapter;

import org.gulash.adapter.adaptee.LegacyBankApi;
import org.gulash.adapter.adapter.target.PaymentProcessor;

import java.math.BigDecimal;

public class LegacyBankAdapter implements PaymentProcessor {

    private final LegacyBankApi legacyBankApi;

    public LegacyBankAdapter(LegacyBankApi legacyBankApi) {
        this.legacyBankApi = legacyBankApi;
    }

    @Override
    public boolean pay(BigDecimal amount) {
        double dollars = amount.doubleValue();
        int result = legacyBankApi.makePayment(dollars);
        return result == 0;
    }
}

