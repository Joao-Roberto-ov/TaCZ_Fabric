package com.tacz.guns.init;

import com.tacz.guns.GunModFabric;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.item.AmmoItem;
import com.tacz.guns.item.AttachmentItem;
import com.tacz.guns.item.ModernKineticGunItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class ModItems {
    // Mapas para guardar as referências
    public static final Map<String, AttachmentItem> ATTACHMENT_ITEMS = new HashMap<>();

    // Itens Principais
    public static final ModernKineticGunItem MODERN_KINETIC_GUN = register("modern_kinetic_gun", new ModernKineticGunItem());
    public static final AmmoItem AMMO = register("ammo", new AmmoItem());

    // Itens de Acessórios (Um para cada tipo)
    static {
        for (AttachmentType type : AttachmentType.values()) {
            if (type != AttachmentType.NONE) {
                String id = "attachment_" + type.name().toLowerCase();
                ATTACHMENT_ITEMS.put(id, register(id, new AttachmentItem(type)));
            }
        }
    }

    // Método auxiliar de registro
    private static <T extends Item> T register(String name, T item) {
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(GunModFabric.MOD_ID, name), item);
    }

    public static void init() {
        // O Java carrega a classe e executa os registros estáticos acima
    }
}