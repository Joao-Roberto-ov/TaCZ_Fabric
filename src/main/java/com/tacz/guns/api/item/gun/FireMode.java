package com.tacz.guns.api.item.gun;

import com.google.gson.annotations.SerializedName;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

public enum FireMode implements StringRepresentable {
    /**
     * Totalmente automático
     */
    @SerializedName("auto")
    AUTO,
    /**
     * Semiautomático
     */
    @SerializedName("semi")
    SEMI,
    /**
     * Rajada (Burst)
     */
    @SerializedName("burst")
    BURST,
    /**
     * Desconhecido
     */
    @SerializedName("unknown")
    UNKNOWN;

    // --- Adições para Minecraft 1.21 (Fabric) ---

    // Codec para serialização (JSON/NBT)
    public static final Codec<FireMode> CODEC = StringRepresentable.fromEnum(FireMode::values);

    // Mapa para busca eficiente por ID numérico (usado na rede)
    public static final IntFunction<FireMode> BY_ID = ByIdMap.continuous(FireMode::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);

    // StreamCodec para serialização de rede (Packets)
    public static final StreamCodec<ByteBuf, FireMode> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, FireMode::ordinal);

    @Override
    public String getSerializedName() {
        // Retorna o nome em minúsculo ("auto", "semi", etc)
        // Se quiser ser ultra-seguro com o @SerializedName antigo, poderia ler dele,
        // mas name().toLowerCase() funciona 99% das vezes se o enum seguir o padrão.
        return this.name().toLowerCase();
    }
}