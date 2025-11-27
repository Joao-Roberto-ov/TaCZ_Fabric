package com.tacz.guns.api.event.common;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * 生物开始更换枪械弹药时触发的事件。
 */
public class GunReloadEvent {
    private final LivingEntity entity;
    private final ItemStack gunItemStack;

    public GunReloadEvent(LivingEntity entity, ItemStack gunItemStack) {
        this.entity = entity;
        this.gunItemStack = gunItemStack;

        postEventToKubeJS(this);
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public ItemStack getGunItemStack() {
        return gunItemStack;
    }
}
