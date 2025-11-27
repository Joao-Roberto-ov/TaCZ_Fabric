package com.tacz.guns.api.item;

import com.tacz.guns.api.DefaultAssets;
import com.tacz.guns.api.TimelessAPI; // Você precisará verificar se essa classe existe/compila depois
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.api.item.component.GunAttachmentsComponent;
import com.tacz.guns.api.item.component.GunHeatComponent;
import com.tacz.guns.api.item.gun.FireMode;
import com.tacz.guns.init.ModDataComponents;
import com.tacz.guns.resource.index.CommonGunIndex;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public interface IGun {
    /**
     * Verifica se o ItemStack é uma arma válida.
     */
    @Nullable
    static IGun getIGunOrNull(@Nullable ItemStack stack) {
        if (stack == null) {
            return null;
        }
        if (stack.getItem() instanceof IGun iGun) {
            return iGun;
        }
        return null;
    }

    static boolean mainHandHoldGun(LivingEntity livingEntity) {
        return livingEntity.getMainHandItem().getItem() instanceof IGun;
    }

    static FireMode getMainHandFireMode(LivingEntity livingEntity) {
        ItemStack mainHandItem = livingEntity.getMainHandItem();
        if (mainHandItem.getItem() instanceof IGun iGun) {
            return iGun.getFireMode(mainHandItem);
        }
        return FireMode.UNKNOWN;
    }

    // --- Métodos de Identificação ---

    @NotNull
    default ResourceLocation getGunId(ItemStack gun) {
        return gun.getOrDefault(ModDataComponents.GUN_ID, DefaultAssets.EMPTY_GUN_ID);
    }

    default void setGunId(ItemStack gun, @Nullable ResourceLocation gunId) {
        if (gunId != null) {
            gun.set(ModDataComponents.GUN_ID, gunId);
        } else {
            gun.remove(ModDataComponents.GUN_ID);
        }
    }

    @NotNull
    default ResourceLocation getGunDisplayId(ItemStack gun) {
        return gun.getOrDefault(ModDataComponents.GUN_DISPLAY_ID, DefaultAssets.DEFAULT_GUN_DISPLAY_ID);
    }

    default void setGunDisplayId(ItemStack gun, @Nullable ResourceLocation displayId) {
        if (displayId != null) {
            gun.set(ModDataComponents.GUN_DISPLAY_ID, displayId);
        } else {
            gun.remove(ModDataComponents.GUN_DISPLAY_ID);
        }
    }

    // --- Métodos de Munição ---

    default int getCurrentAmmoCount(ItemStack gun) {
        return gun.getOrDefault(ModDataComponents.CURRENT_AMMO, 0);
    }

    default void setCurrentAmmoCount(ItemStack gun, int ammoCount) {
        gun.set(ModDataComponents.CURRENT_AMMO, Math.max(ammoCount, 0));
    }

    default void reduceCurrentAmmoCount(ItemStack gun) {
        if (!useInventoryAmmo(gun)) {
            int current = getCurrentAmmoCount(gun);
            setCurrentAmmoCount(gun, current - 1);
        }
    }

    default boolean useDummyAmmo(ItemStack gun) {
        return gun.has(ModDataComponents.DUMMY_AMMO);
    }

    default int getDummyAmmoAmount(ItemStack gun) {
        return gun.getOrDefault(ModDataComponents.DUMMY_AMMO, 0);
    }

    default void setDummyAmmoAmount(ItemStack gun, int amount) {
        gun.set(ModDataComponents.DUMMY_AMMO, Math.max(amount, 0));
    }

    default void addDummyAmmoAmount(ItemStack gun, int amount) {
        if (!useDummyAmmo(gun)) {
            return;
        }
        int maxDummyAmmo = getMaxDummyAmmoAmount(gun); // Se não tiver limite, retorna MAX_VALUE (veja abaixo)
        int current = getDummyAmmoAmount(gun);
        // Evita overflow
        long result = (long) current + amount;
        int finalAmount = (int) Math.min(result, maxDummyAmmo);

        setDummyAmmoAmount(gun, finalAmount);
    }

    default boolean hasMaxDummyAmmo(ItemStack gun) {
        return gun.has(ModDataComponents.MAX_DUMMY_AMMO);
    }

    default int getMaxDummyAmmoAmount(ItemStack gun) {
        // Retorna MAX_VALUE se não houver componente definido, para lógica de adição funcionar
        return gun.getOrDefault(ModDataComponents.MAX_DUMMY_AMMO, Integer.MAX_VALUE);
    }

    default void setMaxDummyAmmoAmount(ItemStack gun, int amount) {
        gun.set(ModDataComponents.MAX_DUMMY_AMMO, Math.max(amount, 0));
    }

    default boolean hasBulletInBarrel(ItemStack gun) {
        return gun.getOrDefault(ModDataComponents.BULLET_IN_BARREL, false);
    }

    default void setBulletInBarrel(ItemStack gun, boolean bulletInBarrel) {
        gun.set(ModDataComponents.BULLET_IN_BARREL, bulletInBarrel);
    }

    // --- Métodos de Disparo e Estado ---

    default FireMode getFireMode(ItemStack gun) {
        return gun.getOrDefault(ModDataComponents.FIRE_MODE, FireMode.UNKNOWN);
    }

    default void setFireMode(ItemStack gun, @Nullable FireMode fireMode) {
        if (fireMode != null) {
            gun.set(ModDataComponents.FIRE_MODE, fireMode);
        } else {
            gun.set(ModDataComponents.FIRE_MODE, FireMode.UNKNOWN);
        }
    }

    // --- Acessórios (O ponto crítico da migração) ---

    default boolean hasAttachmentLock(ItemStack gun) {
        return gun.getOrDefault(ModDataComponents.ATTACHMENT_LOCK, false);
    }

    default void setAttachmentLock(ItemStack gun, boolean lock) {
        gun.set(ModDataComponents.ATTACHMENT_LOCK, lock);
    }

    @NotNull
    default ItemStack getAttachment(ItemStack gun, AttachmentType type) {
        if (!allowAttachmentType(gun, type)) {
            return ItemStack.EMPTY;
        }
        GunAttachmentsComponent attachments = gun.getOrDefault(ModDataComponents.ATTACHMENTS, GunAttachmentsComponent.EMPTY);
        return attachments.getAttachment(type);
    }

    default void installAttachment(@NotNull ItemStack gun, @NotNull ItemStack attachment) {
        if (!allowAttachment(gun, attachment)) {
            return;
        }
        // Precisamos da interface IAttachment no item do anexo para saber o tipo
        // Assumindo que IAttachment já foi migrado ou existe (se não, precisaremos dele)
        if (attachment.getItem() instanceof IAttachment iAttachment) {
            AttachmentType type = iAttachment.getType(attachment);

            GunAttachmentsComponent current = gun.getOrDefault(ModDataComponents.ATTACHMENTS, GunAttachmentsComponent.EMPTY);
            // withAttachment retorna uma NOVA instância (record imutável)
            GunAttachmentsComponent updated = current.withAttachment(type, attachment);

            gun.set(ModDataComponents.ATTACHMENTS, updated);
        }
    }

    default void unloadAttachment(@NotNull ItemStack gun, AttachmentType type) {
        if (!allowAttachmentType(gun, type)) {
            return;
        }
        GunAttachmentsComponent current = gun.getOrDefault(ModDataComponents.ATTACHMENTS, GunAttachmentsComponent.EMPTY);
        GunAttachmentsComponent updated = current.withAttachment(type, ItemStack.EMPTY); // Remove o anexo
        gun.set(ModDataComponents.ATTACHMENTS, updated);
    }

    @NotNull
    default ResourceLocation getAttachmentId(ItemStack gun, AttachmentType type) {
        ItemStack attachmentStack = getAttachment(gun, type);
        if (attachmentStack.isEmpty()) {
            return DefaultAssets.EMPTY_ATTACHMENT_ID;
        }
        // Aqui assume que o item de anexo também implementa IAttachment
        if (attachmentStack.getItem() instanceof IAttachment iAttachment) {
            return iAttachment.getAttachmentId(attachmentStack);
        }
        return DefaultAssets.EMPTY_ATTACHMENT_ID;
    }

    // --- Nível e XP ---

    default int getLevel(ItemStack gun) {
        int exp = getExp(gun);
        return getLevel(exp); // Chama o método abstrato que calcula level baseado em XP
    }

    default int getExp(ItemStack gun) {
        return gun.getOrDefault(ModDataComponents.EXP, 0);
    }

    // Métodos abstratos para lógica de RPG (quem implementar a interface deve definir a tabela de XP)
    int getLevel(int exp);
    int getExp(int level);
    int getMaxLevel();

    default int getExpToNextLevel(ItemStack gun) {
        int exp = getExp(gun);
        int level = getLevel(exp);
        if (level >= getMaxLevel()) {
            return 0;
        }
        int nextLevelExp = getExp(level + 1);
        return nextLevelExp - exp;
    }

    default int getExpCurrentLevel(ItemStack gun) {
        int exp = getExp(gun);
        int level = getLevel(exp);
        if (level <= 0) {
            return exp;
        } else {
            return exp - getExp(level - 1);
        }
    }

    // --- Aquecimento (Heat) ---

    default boolean hasHeatData(ItemStack gun) {
        // Na 1.21, verificamos se o componente existe. Se não existe, não tem dados de calor.
        return gun.has(ModDataComponents.HEAT);
    }

    default float getHeatAmount(ItemStack gun) {
        GunHeatComponent heat = gun.getOrDefault(ModDataComponents.HEAT, GunHeatComponent.DEFAULT);
        return heat.heatAmount();
    }

    default void setHeatAmount(ItemStack gun, float amount) {
        float safeAmount = Math.max(0, amount);
        GunHeatComponent current = gun.getOrDefault(ModDataComponents.HEAT, GunHeatComponent.DEFAULT);
        // Preserva o estado de 'isLocked' (travado por superaquecimento)
        gun.set(ModDataComponents.HEAT, new GunHeatComponent(safeAmount, current.isLocked()));
    }

    default boolean isOverheatLocked(ItemStack gun) {
        return gun.getOrDefault(ModDataComponents.HEAT, GunHeatComponent.DEFAULT).isLocked();
    }

    default void setOverheatLocked(ItemStack gun, boolean locked) {
        GunHeatComponent current = gun.getOrDefault(ModDataComponents.HEAT, GunHeatComponent.DEFAULT);
        gun.set(ModDataComponents.HEAT, new GunHeatComponent(current.heatAmount(), locked));
    }

    // --- Métodos Lógicos Abstratos (Mantidos do original) ---

    void dropAllAmmo(Player player, ItemStack gun);

    @NotNull
    ItemStack getBuiltinAttachment(ItemStack gun, AttachmentType type);

    @NotNull
    ResourceLocation getBuiltInAttachmentId(ItemStack gun, AttachmentType type);

    boolean allowAttachment(ItemStack gun, ItemStack attachmentItem);

    boolean allowAttachmentType(ItemStack gun, AttachmentType type);

    boolean useInventoryAmmo(ItemStack gun);

    boolean hasInventoryAmmo(LivingEntity shooter, ItemStack gun, boolean needCheckAmmo);

    int getRPM(ItemStack gun);

    boolean isCanCrawl(ItemStack gun);

    float getAimingZoom(ItemStack gunItem);

    // --- Laser e Cosméticos ---

    default boolean hasCustomLaserColor(ItemStack gun) {
        return gun.has(ModDataComponents.LASER_COLOR);
    }

    default int getLaserColor(ItemStack gun) {
        return gun.getOrDefault(ModDataComponents.LASER_COLOR, 0xFF0000);
    }

    default void setLaserColor(ItemStack gun, int color) {
        gun.set(ModDataComponents.LASER_COLOR, color);
    }

    // Lógica complexa de lerp (interpolação) movida para métodos default para facilitar
    default float lerpRPM(ItemStack gun) {
        // Nota: TimelessAPI.getCommonGunIndex precisará ser verificado se está disponível nessa fase
        Optional<CommonGunIndex> indexOpt = TimelessAPI.getCommonGunIndex(getGunId(gun));
        if (indexOpt.isPresent()) {
            var heatData = indexOpt.get().getGunData().getHeatData();
            float heatPercentage = (getHeatAmount(gun) / heatData.getHeatMax());
            return Mth.lerp(heatPercentage, heatData.getMinRpmMod(), heatData.getMaxRpmMod());
        }
        return 1f;
    }

    default float lerpInaccuracy(ItemStack gun) {
        Optional<CommonGunIndex> indexOpt = TimelessAPI.getCommonGunIndex(getGunId(gun));
        if (indexOpt.isPresent()) {
            var heatData = indexOpt.get().getGunData().getHeatData();
            float heatPercentage = (getHeatAmount(gun) / heatData.getHeatMax());
            return Mth.lerp(heatPercentage, heatData.getMinInaccuracy(), heatData.getMaxInaccuracy());
        }
        return 1f;
    }
}