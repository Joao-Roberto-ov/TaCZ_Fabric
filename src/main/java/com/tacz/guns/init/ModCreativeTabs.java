package com.tacz.guns.init;

import com.tacz.guns.GunMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {

    public static final CreativeModeTab GUN_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "weapons"),
            FabricItemGroup.builder()
                    .title(Component.translatable("itemGroup.tacz.weapons"))
                    .icon(() -> new ItemStack(ModItems.MODERN_KINETIC_GUN)) // Ícone
                    .displayItems((context, entries) -> {
                        entries.accept(ModItems.MODERN_KINETIC_GUN);
                        entries.accept(ModItems.AMMO);
                        entries.accept(ModItems.AMMO_BOX);
                        entries.accept(ModItems.ATTACHMENT);
                        entries.accept(ModItems.GUN_SMITH_TABLE);
                    })
                    .build()
    );

    public static void init() {
        GunMod.LOGGER.info("Registering Tabs");
    }
}