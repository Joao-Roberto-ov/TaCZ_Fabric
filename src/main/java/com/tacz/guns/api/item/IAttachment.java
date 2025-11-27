package com.tacz.guns.api.item;

import com.tacz.guns.api.DefaultAssets;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.init.ModDataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IAttachment {
    /**
     * Verifica se o ItemStack é um acessório válido.
     */
    @Nullable
    static IAttachment getIAttachmentOrNull(@Nullable ItemStack stack) {
        if (stack == null) {
            return null;
        }
        if (stack.getItem() instanceof IAttachment iAttachment) {
            return iAttachment;
        }
        return null;
    }

    /**
     * Retorna o tipo de acessório (Mira, Boca, etc).
     * Deve ser implementado pela classe do Item.
     */
    @NotNull
    AttachmentType getType(ItemStack stack);

    /**
     * Obtém o ID do acessório (ResourceLocation).
     */
    @NotNull
    default ResourceLocation getAttachmentId(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.ATTACHMENT_ID, DefaultAssets.EMPTY_ATTACHMENT_ID);
    }

    /**
     * Define o ID do acessório.
     */
    default void setAttachmentId(ItemStack stack, @Nullable ResourceLocation id) {
        if (id != null) {
            stack.set(ModDataComponents.ATTACHMENT_ID, id);
        } else {
            stack.remove(ModDataComponents.ATTACHMENT_ID);
        }
    }

    /**
     * Obtém o ID da skin aplicada.
     */
    @Nullable
    default ResourceLocation getSkinId(ItemStack stack) {
        return stack.get(ModDataComponents.SKIN_ID);
    }

    /**
     * Define a skin.
     */
    default void setSkinId(ItemStack stack, @Nullable ResourceLocation id) {
        if (id != null) {
            stack.set(ModDataComponents.SKIN_ID, id);
        } else {
            stack.remove(ModDataComponents.SKIN_ID);
        }
    }

    /**
     * Obtém o nível de zoom atual (para miras variáveis).
     */
    default int getZoomNumber(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.ZOOM_NUMBER, 0);
    }

    /**
     * Define o nível de zoom.
     */
    default void setZoomNumber(ItemStack stack, int number) {
        stack.set(ModDataComponents.ZOOM_NUMBER, number);
    }
}