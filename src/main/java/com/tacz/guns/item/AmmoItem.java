package com.tacz.guns.item;

import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.IAmmo;
import com.tacz.guns.client.resource.index.ClientAmmoIndex;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class AmmoItem extends Item implements IAmmo {
    public AmmoItem() {
        super(new Properties().stacksTo(64)); // Empilha até 64
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        // Adiciona nome e descrição do pacote de recursos (lado do cliente)
        if (level != null && level.isClientSide) {
            Optional<ClientAmmoIndex> indexOpt = TimelessAPI.getClientAmmoIndex(getAmmoId(stack));
            indexOpt.ifPresent(index -> {
                // TODO: Adicionar tooltip traduzido quando o sistema de ClientIndex estiver pronto
                tooltipComponents.add(Component.literal("§7ID: " + getAmmoId(stack)));
            });
        }
    }
}