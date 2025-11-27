package com.tacz.guns.config.client;

import com.tacz.guns.client.renderer.crosshair.CrosshairType;

public class RenderConfig {
    // Classe auxiliar para enganar o código antigo
    public static class ConfigValue<T> {
        private T value;
        public ConfigValue(T val) { this.value = val; }
        public T get() { return value; }
        public void set(T val) { this.value = val; }
    }

    public static ConfigValue<Boolean> ENABLE_LASER_FADE_OUT = new ConfigValue<>(true);
    public static ConfigValue<Integer> GUN_LOD_RENDER_DISTANCE = new ConfigValue<>(64);
    public static ConfigValue<Integer> BULLET_HOLE_PARTICLE_LIFE = new ConfigValue<>(400);
    public static ConfigValue<Double> BULLET_HOLE_PARTICLE_FADE_THRESHOLD = new ConfigValue<>(0.5);
    public static ConfigValue<CrosshairType> CROSSHAIR_TYPE = new ConfigValue<>(CrosshairType.SQUARE_1); // Valor padrão
    public static ConfigValue<Double> HIT_MARKET_START_POSITION = new ConfigValue<>(4.0);
    public static ConfigValue<Boolean> HEAD_SHOT_DEBUG_HITBOX = new ConfigValue<>(false);
    public static ConfigValue<Boolean> GUN_HUD_ENABLE = new ConfigValue<>(true);
    public static ConfigValue<Boolean> KILL_AMOUNT_ENABLE = new ConfigValue<>(true);
    public static ConfigValue<Double> KILL_AMOUNT_DURATION_SECOND = new ConfigValue<>(3.0);
    public static ConfigValue<Integer> TARGET_RENDER_DISTANCE = new ConfigValue<>(64);
    public static ConfigValue<Boolean> FIRST_PERSON_BULLET_TRACER_ENABLE = new ConfigValue<>(true);
    public static ConfigValue<Boolean> DISABLE_INTERACT_HUD_TEXT = new ConfigValue<>(false);
    public static ConfigValue<Integer> DAMAGE_COUNTER_RESET_TIME = new ConfigValue<>(40);
    public static ConfigValue<Boolean> DISABLE_MOVEMENT_ATTRIBUTE_FOV = new ConfigValue<>(false);
    public static ConfigValue<Boolean> ENABLE_TACZ_ID_IN_TOOLTIP = new ConfigValue<>(true);

    public static void init() {} // Método vazio apenas para compatibilidade
}