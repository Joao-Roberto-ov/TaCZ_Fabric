package com.tacz.guns.init;

import com.tacz.guns.GunModFabric;
import com.tacz.guns.entity.EntityKineticBullet;
import com.tacz.guns.entity.TargetMinecart;
import net.minecraft.world.entity.EntityType;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, GunModFabric.MOD_ID);

    public static RegistryObject<EntityType<EntityKineticBullet>> BULLET = ENTITY_TYPES.register("bullet", () -> EntityKineticBullet.TYPE);
    public static RegistryObject<EntityType<TargetMinecart>> TARGET_MINECART = ENTITY_TYPES.register("target_minecart", () -> TargetMinecart.TYPE);
}