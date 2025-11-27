package com.tacz.guns.config.client;

public class KeyConfig {
    public static class ConfigValue<T> {
        private T value;
        public ConfigValue(T val) { this.value = val; }
        public T get() { return value; }
    }

    public static ConfigValue<Boolean> HOLD_TO_AIM = new ConfigValue<>(false);
    public static ConfigValue<Boolean> HOLD_TO_CRAWL = new ConfigValue<>(false);
    public static ConfigValue<Boolean> AUTO_RELOAD = new ConfigValue<>(true);

    public static void init() {}
}