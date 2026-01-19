package org.gulash.adapter;

import org.gulash.adapter.adaptee.LegacyBankApi;
import org.gulash.adapter.adapter.LegacyBankAdapter;
import org.gulash.adapter.client.PaymentService;
import org.gulash.adapter.adapter.target.PaymentProcessor;

import java.math.BigDecimal;

public class AdapterDemo {

    public static void main(String[] args) {
        LegacyBankApi legacyApi = new LegacyBankApi();
        PaymentProcessor adapter = new LegacyBankAdapter(legacyApi);

        PaymentService service = new PaymentService(adapter);
        service.processOrder(new BigDecimal("99.99"));
    }
}

