package com.tacz.guns.api.event.common;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * 生物切换枪械开火模式时触发的事件
 */
public class GunFireSelectEvent {
    private final LivingEntity shooter;
    private final ItemStack gunItemStack;

    public GunFireSelectEvent(LivingEntity shooter, ItemStack gunItemStack) {
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
