package com.tacz.guns.api.event.common;

import net.minecraft.world.item.ItemStack;

/**
 * 生物结束更换枪械弹药时触发的事件。
 */
public class GunFinishReloadEvent {
    private final ItemStack gunItemStack;


    public GunFinishReloadEvent(ItemStack gunItemStack) {
        this.gunItemStack = gunItemStack;

        postEventToKubeJS(this);
    }

    public ItemStack getGunItemStack() {
        return gunItemStack;
    }
}
