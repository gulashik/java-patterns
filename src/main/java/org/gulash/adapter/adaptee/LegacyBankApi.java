package org.gulash.adapter.adaptee;

public class LegacyBankApi {

    public int makePayment(double sumInDollars) {
        System.out.println("Legacy bank processing: " + sumInDollars);
        return 0;
    }
}

