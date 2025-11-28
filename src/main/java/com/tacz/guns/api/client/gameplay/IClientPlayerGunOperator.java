package com.tacz.guns.api.client.gameplay;

import com.tacz.guns.client.gameplay.LocalPlayerDataHolder;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface IClientPlayerGunOperator {
    static IClientPlayerGunOperator fromLocalPlayer(LocalPlayer player) {
        return (IClientPlayerGunOperator) player;
    }

    LocalPlayerDataHolder getShootCoolDown();
    void draw(ItemStack lastItem);
    void shoot();
    void aim(boolean isAim);
    void crawl(boolean isCrawl);
    void fireSelect();
    void bolt();
    void reload();
    void inspect();
    void melee();
    void zoom();

    // Métodos getters para dados do cliente (Adicione conforme necessário)
    default float getClientAimingProgress(float partialTicks) { return 0; }
    default long getClientShootCoolDown() { return 0; }
    default boolean isCrawl() { return false; }
    default boolean isAim() { return false; }
    default LocalPlayerDataHolder getDataHolder() { return null; }
}