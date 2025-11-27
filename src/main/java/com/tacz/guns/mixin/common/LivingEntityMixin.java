package com.tacz.guns.mixin.common;

import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.entity.ReloadState;
import com.tacz.guns.resource.modifier.AttachmentCacheProperty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IGunOperator {

    // Precisamos armazenar esses dados na entidade
    @Unique private long tacz$aimingProgress = 0;
    @Unique private long tacz$shootCoolDown = 0;
    @Unique private ReloadState tacz$reloadState = null;
    @Unique private AttachmentCacheProperty tacz$cacheProperty = null;
    @Unique private float tacz$sprintTime = 0.0f;

    // Construtor obrigatório para Mixins de Entity
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public long getSynAimingProgress() {
        return tacz$aimingProgress;
    }

    @Override
    public void setAimingProgress(long progress) {
        this.tacz$aimingProgress = progress;
    }

    @Override
    public @Nullable ReloadState getSynReloadState() {
        return tacz$reloadState;
    }

    @Override
    public void setReloadState(@Nullable ReloadState reloadState) {
        this.tacz$reloadState = reloadState;
    }

    @Override
    public long getSynShootCoolDown() {
        return tacz$shootCoolDown;
    }

    @Override
    public void setShootCoolDown(long timestamp) {
        this.tacz$shootCoolDown = timestamp;
    }

    @Override
    public void setCacheProperty(AttachmentCacheProperty property) {
        this.tacz$cacheProperty = property;
    }

    @Override
    public @Nullable AttachmentCacheProperty getCacheProperty() {
        return tacz$cacheProperty;
    }

    @Override
    public void setSprintTime(float time) {
        this.tacz$sprintTime = time;
    }

    @Override
    public float getSprintTime() {
        return tacz$sprintTime;
    }

    // Nota: Mais tarde precisaremos adicionar lógica no tick() e writeNbt/readNbt
    // para salvar esses dados e processar a recarga. Por enquanto, isso basta para compilar.
}