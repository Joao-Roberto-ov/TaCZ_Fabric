package com.tacz.guns.config.client;

public class ZoomConfig {
    public static class ConfigValue<T> {
        private T value;
        public ConfigValue(T val) { this.value = val; }
        public T get() { return value; }
    }

    public static ConfigValue<Double> SCREEN_DISTANCE_COEFFICIENT = new ConfigValue<>(1.0);
    public static ConfigValue<Double> ZOOM_SENSITIVITY_BASE_MULTIPLIER = new ConfigValue<>(1.0);

    public static void init() {}
}