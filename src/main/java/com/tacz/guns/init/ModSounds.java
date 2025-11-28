package com.tacz.guns.init;

import com.tacz.guns.GunMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public static final SoundEvent GUN_FIRE = create("gun.fire");
    public static final SoundEvent GUN_DRY_FIRE = create("gun.dry_fire");
    public static final SoundEvent GUN_RELOAD = create("gun.reload");
    public static final SoundEvent GUN_DRAW = create("gun.draw");
    public static final SoundEvent GUN_MELEE = create("gun.melee");
    public static final SoundEvent GUN_MELEE_PUSH = create("gun.melee_push");
    public static final SoundEvent GUN_MELEE_STOCK = create("gun.melee_stock");
    public static final SoundEvent TARGET_HIT = create("target_hit");

    public static void init() {
        GunMod.LOGGER.info("Registering Sounds");
    }

    private static SoundEvent create(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }
}