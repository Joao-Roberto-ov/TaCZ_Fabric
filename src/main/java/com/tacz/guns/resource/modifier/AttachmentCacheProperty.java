package com.tacz.guns.resource.modifier;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tacz.guns.api.GunProperty;
import com.tacz.guns.api.modifier.CacheValue;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import com.tacz.guns.util.AttachmentDataUtils;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Map;

/**
 * Cache de cálculo de modificadores de acessórios.
 */
public class AttachmentCacheProperty {
    @SuppressWarnings("rawtypes")
    private final Map<String, CacheValue> cacheValues = Maps.newHashMap();
    private final Map<String, List<?>> cacheModifiers = Maps.newHashMap();

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void eval(ItemStack gunItem, GunData gunData) {
        // 1. Inicializa os valores base
        var modifiers = AttachmentPropertyManager.getModifiers();
        modifiers.forEach((id, value) -> {
            cacheValues.put(id, value.initCache(gunItem, gunData));
            cacheModifiers.put(id, Lists.newArrayList());
        });

        // 2. Lê os acessórios instalados na arma
        // NOTA IMPORTANTE PARA O PORT: O método AttachmentDataUtils.getAllAttachmentData
        // precisará ser reescrito internamente para ler DataComponents em vez de NBT.
        AttachmentDataUtils.getAllAttachmentData(gunItem, gunData, data -> {
            data.getModifier().forEach((id, value) -> {
                List objects = cacheModifiers.get(id);
                if (objects != null) {
                    objects.add(value.getValue());
                }
            });
        });

        // 3. Calcula o resultado final e armazena no cache
        cacheValues.forEach((id, value) -> {
            List cacheModifier = cacheModifiers.get(id);
            if (cacheModifier == null || cacheModifier.isEmpty()) {
                return;
            }
            modifiers.get(id).eval(cacheModifier, value);
        });

        // 4. Limpa listas temporárias
        cacheModifiers.clear();
    }

    @SuppressWarnings("unchecked")
    public <T> T getCache(String id) {
        CacheValue cv = cacheValues.get(id);
        return cv != null ? (T) cv.getValue() : null;
    }

    @ApiStatus.Experimental
    public <T> T getCache(GunProperty<T> key) {
        CacheValue cv = cacheValues.get(key.name());
        return cv != null ? key.type().cast(cv.getValue()) : null;
    }

    @ApiStatus.Experimental
    @SuppressWarnings("unchecked")
    public <T> void setCache(GunProperty<T> key, T value) {
        if (!key.type().isInstance(value)) {
            throw new IllegalArgumentException("Gun cache type mismatch, needs %s, found %s"
                    .formatted(key.type().getSimpleName(), value.getClass().getSimpleName()));
        }
        CacheValue cv = cacheValues.get(key.name());
        if (cv != null) {
            cv.setValue(value);
        }
    }
}