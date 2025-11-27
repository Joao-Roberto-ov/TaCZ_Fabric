package com.tacz.guns.config.common;

import java.util.List;
import java.util.ArrayList;

public class AmmoConfig {
    public static class ConfigValue<T> {
        private T value;
        public ConfigValue(T val) { this.value = val; }
        public T get() { return value; }
    }

    public static ConfigValue<Boolean> EXPLOSIVE_AMMO_DESTROYS_BLOCK = new ConfigValue<>(true);
    public static ConfigValue<Boolean> EXPLOSIVE_AMMO_FIRE = new ConfigValue<>(false);
    public static ConfigValue<Boolean> EXPLOSIVE_AMMO_KNOCK_BACK = new ConfigValue<>(true);
    public static ConfigValue<Integer> EXPLOSIVE_AMMO_VISIBLE_DISTANCE = new ConfigValue<>(64);
    public static ConfigValue<List<String>> PASS_THROUGH_BLOCKS = new ConfigValue<>(new ArrayList<>());
    public static ConfigValue<Boolean> DESTROY_GLASS = new ConfigValue<>(true);
    public static ConfigValue<Boolean> IGNITE_BLOCK = new ConfigValue<>(true);
    public static ConfigValue<Boolean> IGNITE_ENTITY = new ConfigValue<>(true);
    public static ConfigValue<Double> GLOBAL_BULLET_SPEED_MODIFIER = new ConfigValue<>(1.0);

    public static void init() {}
}