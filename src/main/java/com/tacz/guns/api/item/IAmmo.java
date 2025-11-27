package com.tacz.guns.api.item;

import com.tacz.guns.api.DefaultAssets;
import com.tacz.guns.api.TimelessAPI; // Verificaremos essa classe em breve
import com.tacz.guns.init.ModDataComponents;
import com.tacz.guns.resource.index.CommonGunIndex;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface IAmmo {
    /**
     * Obtém o ID da munição. Reutilizamos o componente ATTACHMENT_ID ou criamos um AMMO_ID?
     * O ideal é criar um AMMO_ID no ModDataComponents para clareza, mas por enquanto vamos
     * assumir que você adicionará AMMO_ID lá ou usará GUN_ID se for genérico.
     * Vamos adicionar AMMO_ID ao ModDataComponents no próximo passo para ser correto.
     */
    @NotNull
    default ResourceLocation getAmmoId(ItemStack stack) {
        // Você precisará adicionar AMMO_ID em ModDataComponents!
        return stack.getOrDefault(ModDataComponents.AMMO_ID, DefaultAssets.EMPTY_AMMO_ID);
    }

    default void setAmmoId(ItemStack stack, @Nullable ResourceLocation id) {
        if (id != null) {
            stack.set(ModDataComponents.AMMO_ID, id);
        } else {
            stack.set(ModDataComponents.AMMO_ID, DefaultAssets.DEFAULT_AMMO_ID);
        }
    }

    default boolean isAmmoOfGun(ItemStack gun, ItemStack ammo) {
        if (gun.getItem() instanceof IGun iGun && ammo.getItem() instanceof IAmmo iAmmo) {
            ResourceLocation gunId = iGun.getGunId(gun);
            ResourceLocation ammoId = iAmmo.getAmmoId(ammo);

            // Lógica simplificada para evitar erro de compilação se TimelessAPI não estiver pronta
            // O original usava TimelessAPI.getCommonGunIndex
            return true; // Placeholder até migrarmos o TimelessAPI
        }
        return false;
    }
}