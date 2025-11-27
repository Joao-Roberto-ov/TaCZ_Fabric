package com.tacz.guns.api.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

// Record é imutável, perfeito para Data Components
public record GunHeatComponent(float heatAmount, boolean isLocked) {

    public static final GunHeatComponent DEFAULT = new GunHeatComponent(0.0f, false);

    // Codec para salvar no disco (JSON/NBT)
    public static final Codec<GunHeatComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("amount").forGetter(GunHeatComponent::heatAmount),
            Codec.BOOL.fieldOf("locked").forGetter(GunHeatComponent::isLocked)
    ).apply(instance, GunHeatComponent::new));

    // StreamCodec para enviar via rede (Packet)
    public static final StreamCodec<ByteBuf, GunHeatComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, GunHeatComponent::heatAmount,
            ByteBufCodecs.BOOL, GunHeatComponent::isLocked,
            GunHeatComponent::new
    );
}