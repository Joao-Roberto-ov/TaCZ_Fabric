package com.tacz.guns.api.event.common;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;


/**
 * 生物射击时触发的事件。与 {@link GunFireEvent}不同的是，扣动一次扳机只会触发一次这个事件，但可能多次触发 {@link GunFireEvent}（如枪械处于 Burst 模式）
 */
public class GunShootEvent {
    private final LivingEntity shooter;
    private final ItemStack gunItemStack;

    public GunShootEvent(LivingEntity shooter, ItemStack gunItemStack) {
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
