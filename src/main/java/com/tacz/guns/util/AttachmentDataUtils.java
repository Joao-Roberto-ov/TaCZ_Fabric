package com.tacz.guns.util;

import com.tacz.guns.api.DefaultAssets;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.api.item.gun.FireMode;
import com.tacz.guns.api.modifier.JsonProperty;
// import com.tacz.guns.config.sync.SyncConfig; // Config ainda não portada
import com.tacz.guns.resource.index.CommonAttachmentIndex;
import com.tacz.guns.resource.modifier.AttachmentPropertyManager;
import com.tacz.guns.resource.pojo.data.attachment.AttachmentData;
import com.tacz.guns.resource.pojo.data.attachment.Modifier;
import com.tacz.guns.resource.pojo.data.gun.BulletData;
import com.tacz.guns.resource.pojo.data.gun.ExtraDamage;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import com.tacz.guns.resource.pojo.data.gun.GunFireModeAdjustData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class AttachmentDataUtils {

    // Placeholder para configurações de sincronização enquanto não portamos o SyncConfig
    private static final double DAMAGE_BASE_MULTIPLIER = 1.0;
    private static final double ARMOR_IGNORE_BASE_MULTIPLIER = 1.0;
    private static final double HEAD_SHOT_BASE_MULTIPLIER = 1.0;

    public static void getAllAttachmentData(ItemStack gunItem, GunData gunData, Consumer<AttachmentData> dataConsumer) {
        IGun iGun = IGun.getIGunOrNull(gunItem);
        if (iGun == null) {
            return;
        }
        for (AttachmentType type : AttachmentType.values()) {
            if (type == AttachmentType.NONE) {
                continue;
            }
            ResourceLocation attachmentId = iGun.getAttachmentId(gunItem, type);
            if (DefaultAssets.isEmptyAttachmentId(attachmentId)) {
                continue;
            }
            AttachmentData attachmentData = gunData.getExclusiveAttachments().get(attachmentId);
            if (attachmentData != null) {
                dataConsumer.accept(attachmentData);
            } else {
                TimelessAPI.getCommonAttachmentIndex(attachmentId).ifPresent(index -> dataConsumer.accept(index.getData()));
            }
        }
    }

    public static int getMagExtendLevel(ItemStack gunItem, GunData gunData) {
        IGun iGun = IGun.getIGunOrNull(gunItem);
        if (iGun == null) {
            return 0;
        }
        ResourceLocation attachmentId = iGun.getAttachmentId(gunItem, AttachmentType.EXTENDED_MAG);
        if (DefaultAssets.isEmptyAttachmentId(attachmentId)) {
            return 0;
        }
        AttachmentData attachmentData = gunData.getExclusiveAttachments().get(attachmentId);
        if (attachmentData != null) {
            int level = attachmentData.getExtendedMagLevel();
            return level <= 0 ? 0 : Math.min(level, 3);
        } else {
            return TimelessAPI.getCommonAttachmentIndex(attachmentId).map(index -> {
                int level = index.getData().getExtendedMagLevel();
                return level <= 0 ? 0 : Math.min(level, 3);
            }).orElse(0);
        }
    }

    public static int getAmmoCountWithAttachment(ItemStack gunItem, GunData gunData) {
        int[] extendedMagAmmoAmount = gunData.getExtendedMagAmmoAmount();
        if (extendedMagAmmoAmount == null) {
            return gunData.getAmmoAmount();
        }
        int level = getMagExtendLevel(gunItem, gunData);
        if (level == 0) {
            return gunData.getAmmoAmount();
        }
        // Previne index out of bounds se o level for maior que o array
        int index = Math.min(level - 1, extendedMagAmmoAmount.length - 1);
        return extendedMagAmmoAmount[index];
    }

    public static double getWightWithAttachment(ItemStack gunItem, GunData gunData) {
        IGun iGun = IGun.getIGunOrNull(gunItem);
        if (iGun == null) {
            return gunData.getWeight();
        }

        List<Modifier> modifiers = new ArrayList<>();
        for (AttachmentType type : AttachmentType.values()){
            ResourceLocation id = iGun.getAttachmentId(gunItem, type);
            // Nota: "weight" deve ser a ID correta do modificador. Verifique WeightModifier.ID
            String modifierId = "weight";

            AttachmentData attachmentData = gunData.getExclusiveAttachments().get(id);
            if (attachmentData != null) {
                var m = attachmentData.getModifier().get(modifierId);
                if(m != null && m.getValue() instanceof Modifier modifier) {
                    modifiers.add(modifier);
                } else {
                    Modifier modifier = new Modifier();
                    modifier.setAddend(attachmentData.getWeight());
                    modifiers.add(modifier);
                }
            } else {
                TimelessAPI.getCommonAttachmentIndex(id).ifPresent(index -> {
                    var m = index.getData().getModifier().get(modifierId);
                    if(m != null && m.getValue() instanceof Modifier modifier) {
                        modifiers.add(modifier);
                    } else {
                        Modifier modifier = new Modifier();
                        modifier.setAddend(index.getData().getWeight());
                        modifiers.add(modifier);
                    }
                });
            }
        }
        return AttachmentPropertyManager.eval(modifiers, gunData.getWeight());
    }

    public static boolean isExplodeEnabled(ItemStack gunItem, GunData gunData) {
        IGun iGun = IGun.getIGunOrNull(gunItem);
        if (iGun == null) {
            if (gunData.getBulletData().getExplosionData() != null) {
                return gunData.getBulletData().getExplosionData().isExplode();
            } else {
                return false;
            }
        }
        // Nota: IDs de modificadores como "explosion" precisam estar definidos em Strings constantes se as classes Modifier não foram portadas
        return calcBooleanValue(gunItem, gunData, "explosion", com.tacz.guns.resource.modifier.custom.ExplosionModifier.ExplosionModifierValue.class,
                com.tacz.guns.resource.modifier.custom.ExplosionModifier.ExplosionModifierValue::isExplode);
    }

    public static double getArmorIgnoreWithAttachment(ItemStack gunItem, GunData gunData) {
        IGun iGun = IGun.getIGunOrNull(gunItem);
        if (iGun == null) {
            return 0;
        }
        FireMode fireMode = iGun.getFireMode(gunItem);
        BulletData bulletData = gunData.getBulletData();
        GunFireModeAdjustData fireModeAdjustData = gunData.getFireModeAdjustData(fireMode);

        ExtraDamage extraDamage = bulletData.getExtraDamage();

        float finalBase = extraDamage != null ? extraDamage.getArmorIgnore() : 0f;
        finalBase = fireModeAdjustData != null ? finalBase + fireModeAdjustData.getArmorIgnore() : finalBase;
        finalBase *= ARMOR_IGNORE_BASE_MULTIPLIER;

        List<Modifier> modifiers = getModifiers(gunItem, gunData, "armor_ignore");
        return AttachmentPropertyManager.eval(modifiers, finalBase);
    }

    public static double getHeadshotMultiplier(ItemStack gunItem, GunData gunData) {
        IGun iGun = IGun.getIGunOrNull(gunItem);
        if (iGun == null) {
            return 0;
        }
        FireMode fireMode = iGun.getFireMode(gunItem);
        BulletData bulletData = gunData.getBulletData();
        GunFireModeAdjustData fireModeAdjustData = gunData.getFireModeAdjustData(fireMode);

        ExtraDamage extraDamage = bulletData.getExtraDamage();

        float finalBase = extraDamage != null ? extraDamage.getHeadShotMultiplier() : 0f;
        finalBase = fireModeAdjustData != null ? finalBase + fireModeAdjustData.getHeadShotMultiplier() : finalBase;
        finalBase *= HEAD_SHOT_BASE_MULTIPLIER;

        List<Modifier> modifiers = getModifiers(gunItem, gunData, "head_shot");
        return AttachmentPropertyManager.eval(modifiers, finalBase);
    }

    public static double getDamageWithAttachment(ItemStack gunItem, GunData gunData) {
        IGun iGun = IGun.getIGunOrNull(gunItem);
        if (iGun == null) {
            return 0;
        }
        FireMode fireMode = iGun.getFireMode(gunItem);
        BulletData bulletData = gunData.getBulletData();
        GunFireModeAdjustData fireModeAdjustData = gunData.getFireModeAdjustData(fireMode);

        ExtraDamage extraDamage = bulletData.getExtraDamage();
        float rawDamage = bulletData.getDamageAmount();

        float finalBase = fireModeAdjustData != null ? fireModeAdjustData.getDamageAmount() : 0f;
        if (extraDamage != null && extraDamage.getDamageAdjust() != null && !extraDamage.getDamageAdjust().isEmpty()) {
            finalBase += extraDamage.getDamageAdjust().get(0).getDamage();
        } else {
            finalBase += rawDamage;
        }
        finalBase *= DAMAGE_BASE_MULTIPLIER;

        List<Modifier> modifiers = getModifiers(gunItem, gunData, "damage");
        return AttachmentPropertyManager.eval(modifiers, finalBase);
    }

    private static List<Modifier> getModifiers(ItemStack gunItem, GunData gunData, String id) {
        IGun iGun = IGun.getIGunOrNull(gunItem);
        if (iGun == null) {
            return new ArrayList<>();
        }
        List<Modifier> modifiers = new ArrayList<>();
        for (AttachmentType type : AttachmentType.values()) {
            ResourceLocation attachmentId = iGun.getAttachmentId(gunItem, type);
            if (DefaultAssets.isEmptyAttachmentId(attachmentId)) {
                continue;
            }
            AttachmentData attachmentData = gunData.getExclusiveAttachments().get(attachmentId);
            if (attachmentData != null) {
                var m = attachmentData.getModifier().get(id);
                if(m != null && m.getValue() instanceof Modifier modifier) {
                    modifiers.add(modifier);
                }
            } else {
                TimelessAPI.getCommonAttachmentIndex(attachmentId).ifPresent(index -> {
                    var m = index.getData().getModifier().get(id);
                    if(m != null && m.getValue() instanceof Modifier modifier) {
                        modifiers.add(modifier);
                    }
                });
            }
        }
        return modifiers;
    }

    private static <T> boolean calcBooleanValue(ItemStack gunItem, GunData gunData, String id, Class<T> clazz, BooleanResolver<T> resolver) {
        IGun iGun = IGun.getIGunOrNull(gunItem);
        if (iGun == null) {
            return false;
        }
        for (AttachmentType type : AttachmentType.values()) {
            ResourceLocation attachmentId = iGun.getAttachmentId(gunItem, type);
            if (DefaultAssets.isEmptyAttachmentId(attachmentId)) {
                continue;
            }
            AttachmentData attachmentData = gunData.getExclusiveAttachments().get(attachmentId);
            if (attachmentData != null) {
                var m = attachmentData.getModifier().get(id);
                if (resolve(m, resolver, clazz)) {
                    return true;
                }
            } else {
                var indexOpt = TimelessAPI.getCommonAttachmentIndex(attachmentId);
                if (indexOpt.isPresent()) {
                    var m = indexOpt.get().getData().getModifier().get(id);
                    if (resolve(m, resolver, clazz)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private static <T> boolean resolve(JsonProperty<?> raw, BooleanResolver<T> data, Class<T> type){
        if (raw != null && raw.getValue() != null && type.isAssignableFrom(raw.getValue().getClass())) {
            return data.apply((T) raw.getValue());
        }
        return false;
    }

    @FunctionalInterface
    public interface BooleanResolver<T> {
        boolean apply(T data);
    }
}