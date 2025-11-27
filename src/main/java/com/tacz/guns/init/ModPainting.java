package com.tacz.guns.init;

import com.tacz.guns.GunModFabric;
import net.minecraft.world.entity.decoration.PaintingVariant;

public class ModPainting {
    public static final DeferredRegister<PaintingVariant> PAINTINGS = DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, GunModFabric.MOD_ID);

    public static final RegistryObject<PaintingVariant> BLOOD_STRIKE_1 = PAINTINGS.register("blood_strike_1", () -> new PaintingVariant(32, 32));
//    public static final RegistryObject<PaintingVariant> BLOOD_STRIKE_2 = PAINTINGS.register("blood_strike_2", () -> new PaintingVariant(32, 32));
}
