package com.tacz.guns.init;

import com.tacz.guns.GunModFabric;
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
            ResourceLocation.fromNamespaceAndPath(GunModFabric.MOD_ID, "gun_tab"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.MODERN_KINETIC_GUN))
                    .title(Component.translatable("itemGroup.tacz.gun_tab"))
                    .displayItems((context, entries) -> {
                        // Adiciona o item base da arma
                        entries.accept(ModItems.MODERN_KINETIC_GUN);
                        entries.accept(ModItems.AMMO);

                        // Adiciona os acessórios
                        ModItems.ATTACHMENT_ITEMS.values().forEach(entries::accept);

                        // TODO: Futuramente, adicionar aqui as armas pré-configuradas (AK47, M4, etc)
                        // iterando sobre o CommonGunIndex e criando stacks com DataComponents.
                    })
                    .build()
    );

    public static void init() {
        // Apenas carrega a classe
    }
}