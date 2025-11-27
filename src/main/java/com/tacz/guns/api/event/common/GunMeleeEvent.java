package com.tacz.guns.api.event.common;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;


/**
 * 用枪近战时触发
 */
public class GunMeleeEvent {
    private final LivingEntity shooter;
    private final ItemStack gunItemStack;

    public GunMeleeEvent(LivingEntity shooter, ItemStack gunItemStack) {
        this.shooter = shooter;
        this.gunItemStack = gunItemStack;

        postEventToKubeJS(this);
    }

    public LivingEntity getShooter() {
        return shooter;
    }

    public ItemStack getGunItemStack() {
        return gunItemStack;
    }
}
