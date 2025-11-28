package com.tacz.guns.api.item.gun;

import com.tacz.guns.api.item.IGun;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractGunItem extends Item implements IGun {

    public AbstractGunItem(Properties properties) {
        super(properties);
    }

    @Override
    public ResourceLocation getGunId(ItemStack stack) {
        // Retorne o ID da arma aqui baseado no NBT. Stub por enquanto.
        return null;
    }

    @Override
    public void setGunId(ItemStack stack, ResourceLocation id) {
        // Stub
    }

    /**
     * Versão refatorada para Fabric: Usa Inventory e Player diretamente
     * em vez de IItemHandler do Forge.
     */
    public int findAndExtractInventoryAmmo(Player player, ItemStack gunItem, int needAmmoCount) {
        if (player.isCreative()) return needAmmoCount;

        Inventory inventory = player.getInventory();
        int found = 0;

        // Iteração simples no inventário
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.isEmpty()) continue;

            // Aqui viria a checagem: if (isAmmoCompatible(stack)) { ... }
            // Stub:
            // int amount = Math.min(needAmmoCount - found, stack.getCount());
            // stack.shrink(amount);
            // found += amount;
            // if (found >= needAmmoCount) break;
        }
        return found;
    }

    // Stub para evitar erros em chamadas antigas
    public boolean allowNbtUpdateAnimation(Player player, ItemStack handStack, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    // Stubs da interface IGun para compilar
    @Override public FireMode getFireMode(ItemStack stack) { return FireMode.SEMI; }
    @Override public void setFireMode(ItemStack stack, FireMode mode) {}
    @Override public int getCurrentAmmoCount(ItemStack stack) { return 0; }
    @Override public void setCurrentAmmoCount(ItemStack stack, int count) {}
    @Override public void reduceCurrentAmmoCount(ItemStack stack, int count) {}
    @Override public boolean hasBulletInBarrel(ItemStack stack) { return false; }
    @Override public void setBulletInBarrel(ItemStack stack, boolean has) {}
    @Override public int getMaxDummyAmmoAmount() { return 0; }
    @Override public int getDummyAmmoAmount(ItemStack stack) { return 0; }
    @Override public void setDummyAmmoAmount(ItemStack stack, int amount) {}
    @Override public void addDummyAmmoAmount(ItemStack stack, int amount) {}
    @Override public int getLevel(int exp) { return 0; }
    @Override public int getExp(int level) { return 0; }
    @Override public int getMaxLevel() { return 0; }
}