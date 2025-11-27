package com.tacz.guns.api.entity;

import com.tacz.guns.resource.modifier.AttachmentCacheProperty;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface IGunOperator {
    /**
     * Método estático para converter qualquer LivingEntity em um IGunOperator.
     * Graças ao Mixin que criaremos a seguir, todas as LivingEntity implementarão isso.
     */
    static IGunOperator fromLivingEntity(LivingEntity entity) {
        return (IGunOperator) entity;
    }

    /**
     * Obtém o tempo em que a entidade começou a mirar (em ticks ou ms).
     * Retorna -1 se não estiver mirando.
     */
    long getSynAimingProgress();

    /**
     * Define o progresso da mira.
     */
    void setAimingProgress(long progress);

    /**
     * Verifica se a entidade está recarregando.
     */
    @Nullable
    ReloadState getSynReloadState();

    /**
     * Define o estado de recarga.
     */
    void setReloadState(@Nullable ReloadState reloadState);

    /**
     * Obtém o timestamp base para sincronização (importante para animações).
     */
    long getSynShootCoolDown();

    void setShootCoolDown(long timestamp);

    // Cache de propriedades de anexos (RPM, Recuo alterado pelos acessórios)
    void setCacheProperty(AttachmentCacheProperty property);

    @Nullable
    AttachmentCacheProperty getCacheProperty();

    // Dados de corrida/sprint
    void setSprintTime(float time);
    float getSprintTime();
}