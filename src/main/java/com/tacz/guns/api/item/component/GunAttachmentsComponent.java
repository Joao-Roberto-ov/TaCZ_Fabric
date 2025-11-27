package com.tacz.guns.api.item.component;

import com.mojang.serialization.Codec;
import com.tacz.guns.api.item.attachment.AttachmentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public record GunAttachmentsComponent(Map<AttachmentType, ItemStack> attachments) {

    public static final GunAttachmentsComponent EMPTY = new GunAttachmentsComponent(Collections.emptyMap());

    // Codec: Um Mapa onde a Chave é o Enum (String) e o Valor é um ItemStack
    public static final Codec<GunAttachmentsComponent> CODEC = Codec.unboundedMap(
            AttachmentType.CODEC, // Você precisará adicionar o CODEC no Enum AttachmentType original
            ItemStack.CODEC
    ).xmap(EnumMap::new, map -> map).xmap(GunAttachmentsComponent::new, GunAttachmentsComponent::attachments);

    // StreamCodec: Requer RegistryFriendlyByteBuf pois ItemStack depende do registro do servidor
    public static final StreamCodec<RegistryFriendlyByteBuf, GunAttachmentsComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(EnumMap::new, AttachmentType.STREAM_CODEC, ItemStack.STREAM_CODEC),
            GunAttachmentsComponent::attachments,
            GunAttachmentsComponent::new
    );

    public ItemStack getAttachment(AttachmentType type) {
        return attachments.getOrDefault(type, ItemStack.EMPTY);
    }

    // Como Records são imutáveis, métodos "set" retornam uma NOVA instância
    public GunAttachmentsComponent withAttachment(AttachmentType type, ItemStack stack) {
        EnumMap<AttachmentType, ItemStack> newMap = new EnumMap<>(this.attachments);
        if (stack.isEmpty()) {
            newMap.remove(type);
        } else {
            newMap.put(type, stack);
        }
        return new GunAttachmentsComponent(newMap);
    }
}