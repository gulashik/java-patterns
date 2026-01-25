package org.gulash.chain.list.model.handler;

import org.gulash.chain.list.model.Request;

@FunctionalInterface
public interface Handler {
    boolean handle(Request request);
}
