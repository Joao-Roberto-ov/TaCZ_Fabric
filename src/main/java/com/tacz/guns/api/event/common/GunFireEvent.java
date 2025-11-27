package com.tacz.guns.api.event.common;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class GunFireEvent {
    private final LivingEntity shooter;
    private final ItemStack gunItemStack;

    public GunFireEvent(LivingEntity shooter, ItemStack gunItemStack) { // Construtor limpo
        this.shooter = shooter;
        this.gunItemStack = gunItemStack;
    }

    public LivingEntity getShooter() { return shooter; }
    public ItemStack getGunItemStack() { return gunItemStack; }
}