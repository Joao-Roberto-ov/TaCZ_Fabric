package com.tacz.guns.config.common;

public class OtherConfig {
    public static class ConfigValue<T> {
        private T value;
        public ConfigValue(T val) { this.value = val; }
        public T get() { return value; }
    }

    public static ConfigValue<Boolean> DEFAULT_PACK_DEBUG = new ConfigValue<>(false);
    public static ConfigValue<Integer> TARGET_SOUND_DISTANCE = new ConfigValue<>(16);
    public static ConfigValue<Double> SERVER_HITBOX_OFFSET = new ConfigValue<>(0.0);
    public static ConfigValue<Boolean> SERVER_HITBOX_LATENCY_FIX = new ConfigValue<>(false);
    public static ConfigValue<Double> SERVER_HITBOX_LATENCY_MAX_SAVE_MS = new ConfigValue<>(200.0);

    public static void init() {}
}