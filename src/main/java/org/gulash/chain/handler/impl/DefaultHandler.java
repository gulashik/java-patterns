package org.gulash.chain.handler.impl;

import org.gulash.chain.handler.Handler;
import org.gulash.chain.model.Request;


public class DefaultHandler extends Handler {
    @Override
    public boolean handle(Request request) {
        /* есть проверка в методе на null в следующем элементе и этот Handler особо не нужен*/
        System.out.println("DefaultHandler: Шаг успешен успешна.");
        return true;
    }
}
