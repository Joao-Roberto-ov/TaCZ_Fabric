package com.tacz.guns.item;

import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.IAttachment;
import com.tacz.guns.api.item.attachment.AttachmentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AttachmentItem extends Item implements IAttachment {
    private final AttachmentType type;

    public AttachmentItem(AttachmentType type) {
        super(new Properties().stacksTo(1)); // Acessórios não empilham
        this.type = type;
    }

    @Override
    public AttachmentType getType(ItemStack stack) {
        return this.type;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (level != null && level.isClientSide) {
            TimelessAPI.getClientAttachmentIndex(getAttachmentId(stack)).ifPresent(index -> {
                tooltipComponents.add(Component.literal("§7ID: " + getAttachmentId(stack)));
            });
        }
    }
}