package org.gulash.decorator;

/**
 * Интерфейс компонента определяет операции, которые могут быть изменены декораторами.
 */
public interface Notifier {
    void send(String message);
}
