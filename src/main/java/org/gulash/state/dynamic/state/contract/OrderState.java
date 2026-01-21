package org.gulash.state.dynamic.state.contract;

/**
 * Базовый интерфейс всех состояний.
 * Определяет все возможные действия, которые могут быть выполнены
 * в различных состояниях автомата.
 */
public interface OrderState {
    OrderState next();
    String name();
}
