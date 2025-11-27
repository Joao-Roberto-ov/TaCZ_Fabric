package com.tacz.guns.init;

import com.tacz.guns.GunModFabric;
import com.tacz.guns.inventory.GunSmithTableMenu;
import net.minecraft.world.inventory.MenuType;

public class ModContainer {
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPE = DeferredRegister.create(ForgeRegistries.MENU_TYPES, GunModFabric.MOD_ID);

    public static final RegistryObject<MenuType<GunSmithTableMenu>> GUN_SMITH_TABLE_MENU = CONTAINER_TYPE.register("gun_smith_table_menu", () -> GunSmithTableMenu.TYPE);
}
