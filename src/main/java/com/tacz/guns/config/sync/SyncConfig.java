package com.tacz.guns.config.sync;

import java.util.ArrayList;
import java.util.List;

public class SyncConfig {
    public static class ConfigValue<T> {
        private T value;
        public ConfigValue(T val) { this.value = val; }
        public T get() { return value; }
    }

    public static ConfigValue<Double> DAMAGE_BASE_MULTIPLIER = new ConfigValue<>(1.0);
    public static ConfigValue<Double> ARMOR_IGNORE_BASE_MULTIPLIER = new ConfigValue<>(1.0);
    public static ConfigValue<Double> HEAD_SHOT_BASE_MULTIPLIER = new ConfigValue<>(1.0);
    public static ConfigValue<Double> WEIGHT_SPEED_MULTIPLIER = new ConfigValue<>(1.0);

    public static ConfigValue<Boolean> ENABLE_CRAWL = new ConfigValue<>(true);
    public static ConfigValue<Integer> AMMO_BOX_STACK_SIZE = new ConfigValue<>(64);

    // Listas vazias para evitar NullPointerException
    public static ConfigValue<List<String>> HEAD_SHOT_AABB = new ConfigValue<>(new ArrayList<>());
    public static ConfigValue<List<String>> INTERACT_KEY_WHITELIST_BLOCKS = new ConfigValue<>(new ArrayList<>());
    public static ConfigValue<List<String>> INTERACT_KEY_WHITELIST_ENTITIES = new ConfigValue<>(new ArrayList<>());
    public static ConfigValue<List<String>> INTERACT_KEY_BLACKLIST_BLOCKS = new ConfigValue<>(new ArrayList<>());
    public static ConfigValue<List<String>> INTERACT_KEY_BLACKLIST_ENTITIES = new ConfigValue<>(new ArrayList<>());
    public static ConfigValue<List<List<String>>> CLIENT_GUN_PACK_DOWNLOAD_URLS = new ConfigValue<>(new ArrayList<>());

    public static ConfigValue<Boolean> ENABLE_TABLE_FILTER = new ConfigValue<>(true);
    public static ConfigValue<Boolean> SERVER_SHOOT_NETWORK_V = new ConfigValue<>(false);
    public static ConfigValue<Boolean> SERVER_SHOOT_COOLDOWN_V = new ConfigValue<>(false);

    public static void init() {}
}