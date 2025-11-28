package com.tacz.guns.init;

import com.tacz.guns.GunMod;
import com.tacz.guns.item.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class ModItems {
    // Itens Principais
    public static final ModernKineticGunItem MODERN_KINETIC_GUN = register("modern_kinetic_gun", new ModernKineticGunItem());
    public static final AmmoItem AMMO = register("ammo", new AmmoItem());
    public static final AmmoBoxItem AMMO_BOX = register("ammo_box", new AmmoBoxItem());
    public static final AttachmentItem ATTACHMENT = register("attachment", new AttachmentItem());

    // Blocos convertidos em Itens
    public static final BlockItem GUN_SMITH_TABLE = register("gun_smith_table", new GunSmithTableItem(ModBlocks.GUN_SMITH_TABLE));
    public static final BlockItem WORKBENCH_111 = register("workbench_111", new BlockItem(ModBlocks.WORKBENCH_111, new Item.Properties())); // Substitua pelos nomes corretos dos blocos se diferir

    // Método de inicialização chamado pela Main
    public static void init() {
        // Apenas carregar a classe faz os registros estáticos rodarem
        GunMod.LOGGER.info("Registering Items");
    }

    // Helper para registrar itens
    private static <T extends Item> T register(String name, T item) {
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, name), item);
    }
}