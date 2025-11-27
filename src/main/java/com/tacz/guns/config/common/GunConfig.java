package com.tacz.guns.config.common;

public class GunConfig {
    public static class ConfigValue<T> {
        private T value;
        public ConfigValue(T val) { this.value = val; }
        public T get() { return value; }
    }

    public static ConfigValue<Integer> DEFAULT_GUN_FIRE_SOUND_DISTANCE = new ConfigValue<>(64);
    public static ConfigValue<Integer> DEFAULT_GUN_SILENCE_SOUND_DISTANCE = new ConfigValue<>(32);
    public static ConfigValue<Integer> DEFAULT_GUN_OTHER_SOUND_DISTANCE = new ConfigValue<>(16);
    public static ConfigValue<Boolean> CREATIVE_PLAYER_CONSUME_AMMO = new ConfigValue<>(false);
    public static ConfigValue<Boolean> AUTO_RELOAD_WHEN_RESPAWN = new ConfigValue<>(true);

    public static void init() {}
}