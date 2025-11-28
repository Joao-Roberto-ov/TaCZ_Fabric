package com.tacz.guns.api.item.builder;

import com.tacz.guns.api.item.gun.AbstractGunItem;
import net.minecraft.world.item.Item;
import java.util.function.Supplier;

public class GunItemBuilder {
    private String name;
    private String model;
    private String texture;
    private Supplier<? extends Item> itemSupplier;

    public static GunItemBuilder create() {
        return new GunItemBuilder();
    }

    public GunItemBuilder name(String name) {
        this.name = name;
        return this;
    }

    public GunItemBuilder model(String model) {
        this.model = model;
        return this;
    }

    public GunItemBuilder texture(String texture) {
        this.texture = texture;
        return this;
    }

    // Alterado de RegistryObject para Supplier<Item> ou Item direto
    public GunItemBuilder item(Supplier<? extends Item> itemSupplier) {
        this.itemSupplier = itemSupplier;
        return this;
    }

    public void build() {
        // Lógica de construção simplificada para Fabric
    }
}