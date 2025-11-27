package com.tacz.guns.api.event.common;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * 生物开始更换枪械弹药时触发的事件。
 */
public class GunDrawEvent {
    private final LivingEntity entity;
    private final ItemStack previousGunItem;
    private final ItemStack currentGunItem;


    public GunDrawEvent(LivingEntity entity, ItemStack previousGunItem, ItemStack currentGunItem) {
        this.entity = entity;
        this.previousGunItem = previousGunItem;
        this.currentGunItem = currentGunItem;

        postEventToKubeJS(this);
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public ItemStack getPreviousGunItem() {
        return previousGunItem;
    }

    public ItemStack getCurrentGunItem() {
        return currentGunItem;
    }
}
