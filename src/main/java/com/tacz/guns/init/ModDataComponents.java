package com.tacz.guns.init;

import com.tacz.guns.GunModFabric;
import com.tacz.guns.api.item.component.GunAttachmentsComponent;
import com.tacz.guns.api.item.component.GunHeatComponent;
import com.tacz.guns.api.item.gun.FireMode;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

import java.util.function.UnaryOperator;

public class ModDataComponents {

    // 1. Identificação da Arma (Substitui "GunId")
    public static final DataComponentType<ResourceLocation> GUN_ID = register("gun_id", builder -> builder
            .persistent(ResourceLocation.CODEC)
            .networkSynchronized(ResourceLocation.STREAM_CODEC));

    // 2. ID de Exibição (Skin/Modelo visual) (Substitui "GunDisplayId")
    public static final DataComponentType<ResourceLocation> GUN_DISPLAY_ID = register("gun_display_id", builder -> builder
            .persistent(ResourceLocation.CODEC)
            .networkSynchronized(ResourceLocation.STREAM_CODEC));

    // 3. Munição Atual (Substitui "GunCurrentAmmoCount")
    public static final DataComponentType<Integer> CURRENT_AMMO = register("current_ammo", builder -> builder
            .persistent(ExtraCodecs.NON_NEGATIVE_INT) // Garante que não seja negativo
            .networkSynchronized(ByteBufCodecs.VAR_INT));

    // 4. Munição Virtual/Dummy (Para criativo ou testes)
    public static final DataComponentType<Integer> DUMMY_AMMO = register("dummy_ammo", builder -> builder
            .persistent(ExtraCodecs.NON_NEGATIVE_INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DataComponentType<Integer> MAX_DUMMY_AMMO = register("max_dummy_ammo", builder -> builder
            .persistent(ExtraCodecs.NON_NEGATIVE_INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT));

    // 5. Modo de Disparo (Substitui "GunFireMode")
    // Nota: Precisaremos garantir que o Enum FireMode tenha Codec. Faremos isso a seguir.
    public static final DataComponentType<FireMode> FIRE_MODE = register("fire_mode", builder -> builder
            .persistent(FireMode.CODEC)
            .networkSynchronized(FireMode.STREAM_CODEC));

    // 6. Estado do Cano (Bala na agulha?)
    public static final DataComponentType<Boolean> BULLET_IN_BARREL = register("bullet_in_barrel", builder -> builder
            .persistent(ExtraCodecs.BOOL)
            .networkSynchronized(ByteBufCodecs.BOOL));

    // 7. Experiência/Nível da Arma
    public static final DataComponentType<Integer> EXP = register("exp", builder -> builder
            .persistent(ExtraCodecs.NON_NEGATIVE_INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT));

    // 8. Trava de Acessórios
    public static final DataComponentType<Boolean> ATTACHMENT_LOCK = register("attachment_lock", builder -> builder
            .persistent(ExtraCodecs.BOOL)
            .networkSynchronized(ByteBufCodecs.BOOL));

    // 9. Cor do Laser (Inteiro RGB)
    public static final DataComponentType<Integer> LASER_COLOR = register("laser_color", builder -> builder
            .persistent(ExtraCodecs.INT) // Pode ser negativo dependendo de como a cor é codificada, ou use NON_NEGATIVE_INT
            .networkSynchronized(ByteBufCodecs.VAR_INT));

    // --- DADOS COMPLEXOS (Records) ---

    // 10. Sistema de Aquecimento (Agrupa "HeatAmount" e "OverHeated")
    public static final DataComponentType<GunHeatComponent> HEAT = register("heat", builder -> builder
            .persistent(GunHeatComponent.CODEC)
            .networkSynchronized(GunHeatComponent.STREAM_CODEC));

    // 11. Acessórios (O maior desafio - Substitui a tag "Attachment...")
    public static final DataComponentType<GunAttachmentsComponent> ATTACHMENTS = register("attachments", builder -> builder
            .persistent(GunAttachmentsComponent.CODEC)
            .networkSynchronized(GunAttachmentsComponent.STREAM_CODEC));

    // --- COMPONENTES DE ACESSÓRIOS ---

    // 12. ID do Acessório (Substitui "AttachmentId")
    public static final DataComponentType<ResourceLocation> ATTACHMENT_ID = register("attachment_id", builder -> builder
            .persistent(ResourceLocation.CODEC)
            .networkSynchronized(ResourceLocation.STREAM_CODEC));

    // 13. ID da Skin do Acessório (Substitui "Skin")
    public static final DataComponentType<ResourceLocation> SKIN_ID = register("skin_id", builder -> builder
            .persistent(ResourceLocation.CODEC)
            .networkSynchronized(ResourceLocation.STREAM_CODEC));

    // 14. Número de Zoom (para miras variáveis)
    public static final DataComponentType<Integer> ZOOM_NUMBER = register("zoom_number", builder -> builder
            .persistent(ExtraCodecs.NON_NEGATIVE_INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DataComponentType<ResourceLocation> AMMO_ID = register("ammo_id", builder -> builder
            .persistent(ResourceLocation.CODEC)
            .networkSynchronized(ResourceLocation.STREAM_CODEC));

    // Metodo auxiliar de registro
    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(GunModFabric.MOD_ID, name), (builderOperator.apply(DataComponentType.builder())).build());
    }

    // Chamado na inicialização
    public static void init() {
        // O Java carrega a classe estática e registra tudo
    }
}