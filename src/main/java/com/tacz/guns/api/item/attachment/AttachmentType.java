package com.tacz.guns.api.item.attachment;

import com.google.gson.annotations.SerializedName;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

public enum AttachmentType implements StringRepresentable {
    /**
     * Mira / Scope
     */
    @SerializedName("scope")
    SCOPE,
    /**
     * Boca / Muzzle
     */
    @SerializedName("muzzle")
    MUZZLE,
    /**
     * Coronha / Stock
     */
    @SerializedName("stock")
    STOCK,
    /**
     * Empunhadura / Grip
     */
    @SerializedName("grip")
    GRIP,
    /**
     * Laser
     */
    @SerializedName("laser")
    LASER,
    /**
     * Carregador Estendido
     */
    @SerializedName("extended_mag")
    EXTENDED_MAG,
    /**
     * Nenhum (usado internamente)
     */
    NONE;

    // --- Adições para Minecraft 1.21 (Fabric) ---

    public static final Codec<AttachmentType> CODEC = StringRepresentable.fromEnum(AttachmentType::values);

    public static final IntFunction<AttachmentType> BY_ID = ByIdMap.continuous(AttachmentType::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);

    public static final StreamCodec<ByteBuf, AttachmentType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, AttachmentType::ordinal);

    @Override
    public String getSerializedName() {
        // Garante compatibilidade com os nomes antigos usados nos JSONs
        if (this == EXTENDED_MAG) {
            return "extended_mag";
        }
        return this.name().toLowerCase();
    }
}