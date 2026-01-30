package org.gulash.builder;

import java.util.List;

/**
 * Демонстрация использования паттерна Строитель (Builder).
 */
public class BuilderDemo {
    public static void main(String[] args) {
        System.out.println("=== Builder Pattern Demo ===");

        // 1. Создание минимальной конфигурации (только обязательные параметры)
        ComputerBuilder basicPc = new ComputerBuilder.Builder("Intel i3", "8GB")
                .build();
        System.out.println("Basic PC: " + basicPc);

        // 2. Создание игровой конфигурации (с использованием цепочки вызовов)
        ComputerBuilder gamingPc = new ComputerBuilder.Builder("AMD Ryzen 9", "32GB")
                .setStorage("2TB NVMe SSD")
                .setGpu("NVIDIA RTX 4090")
                .setBluetooth(true)
                .setPeripherals(List.of("Mechanical Keyboard", "Gaming Mouse", "Webcam"))
                .build();
        System.out.println("Gaming PC: " + gamingPc);

        // 3. Создание офисного ПК (выборочные параметры)
        ComputerBuilder officePc = new ComputerBuilder.Builder("Intel i5", "16GB")
                .setStorage("512GB SSD")
                .build();
        System.out.println("Office PC: " + officePc);

        System.out.println("\n=== Demo Finished ===");
    }
}
