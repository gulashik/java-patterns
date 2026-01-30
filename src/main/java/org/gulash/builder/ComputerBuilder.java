package org.gulash.builder;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * Сложный объект, создание которого требует множества параметров.
 * В данном примере мы используем классический подход реализации Builder в Java через статический вложенный класс.
 */
public class ComputerBuilder {
    // Обязательные параметры
    @Getter
    private final String cpu;
    @Getter
    private final String ram;

    // Необязательные параметры
    @Getter
    private final String storage;
    @Getter
    private final String gpu;
    @Getter
    private final boolean hasBluetooth;
    @Getter
    private final List<String> peripherals;

    private ComputerBuilder(Builder builder) {
        this.cpu = builder.cpu;
        this.ram = builder.ram;
        this.storage = builder.storage;
        this.gpu = builder.gpu;
        this.hasBluetooth = builder.hasBluetooth;
        this.peripherals = builder.peripherals;
    }

    @Override
    public String toString() {
        return "Computer{" +
                "cpu='" + cpu + '\'' +
                ", ram='" + ram + '\'' +
                ", storage='" + storage + '\'' +
                ", gpu='" + gpu + '\'' +
                ", hasBluetooth=" + hasBluetooth +
                ", peripherals=" + peripherals +
                '}';
    }

    /**
     * Статический вложенный класс Builder.
     */
    public static class Builder {
        // Те же поля, что и в основном классе
        // Обязательные поля
        private final String cpu;
        private final String ram;

        // Значение по умолчанию
        private String storage = "256GB SSD";
        private String gpu = "Integrated";
        private boolean hasBluetooth = false;
        private List<String> peripherals = Collections.emptyList();

        /**
         * Конструктор билдера принимает только обязательные параметры.
         */
        public Builder(String cpu, String ram) {
            if (cpu == null || ram == null) {
                throw new IllegalArgumentException("CPU and RAM are required");
            }
            this.cpu = cpu;
            this.ram = ram;
        }

        public Builder setStorage(String storage) {
            this.storage = storage;
            return this; // Возвращаем текущий объект для цепочки вызовов
        }

        public Builder setGpu(String gpu) {
            this.gpu = gpu;
            return this;
        }

        public Builder setBluetooth(boolean hasBluetooth) {
            this.hasBluetooth = hasBluetooth;
            return this;
        }

        public Builder setPeripherals(List<String> peripherals) {
            this.peripherals = peripherals != null ? List.copyOf(peripherals) : Collections.emptyList();
            return this;
        }

        /**
         * Финальный метод создания объекта.
         */
        public ComputerBuilder build() {
            // Здесь можно добавить дополнительную валидацию согласованности полей
            return new ComputerBuilder(this);
        }
    }
}
