package com.tacz.guns.api.item.gun;

import com.google.common.collect.Maps;
import net.minecraft.world.item.Item;
import java.util.Collection;
import java.util.Map;

public class GunItemManager {
    // Mapa guarda o Item diretamente
    private static final Map<String, Item> GUN_ITEM_MAP = Maps.newHashMap();

    public static void registerGunItem(String name, Item item) {
        if (item instanceof AbstractGunItem) {
            GUN_ITEM_MAP.put(name, item);
        }
    }

    public static Item getGunItem(String key) {
        return GUN_ITEM_MAP.get(key);
    }

    public static Collection<Item> getAllGunItems() {
        return GUN_ITEM_MAP.values();
    }
}