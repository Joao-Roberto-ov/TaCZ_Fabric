package com.tacz.guns.item;

import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.gun.FireMode;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModernKineticGunItem extends Item implements IGun {
    public ModernKineticGunItem() {
        super(new Properties().stacksTo(1)); // Armas não empilham
    }

    @Override
    public int getLevel(int exp) {
        return 0; // Lógica de nível implementaremos depois
    }

    @Override
    public int getExp(int level) {
        return 0;
    }

    @Override
    public int getMaxLevel() {
        return 10; // Exemplo
    }

    @Override
    public void dropAllAmmo(Player player, ItemStack gun) {
        // Lógica de dropar munição ao descarregar (implementaremos depois)
    }

    @Override
    public boolean allowAttachment(ItemStack gun, ItemStack attachmentItem) {
        // Implementação básica: verifica se o tipo é permitido no JSON da arma
        GunData data = TimelessAPI.getGunData(this.getGunId(gun));
        if (data != null && attachmentItem.getItem() instanceof AttachmentItem attachment) {
            return data.getAllowAttachments().contains(attachment.getType(attachmentItem));
        }
        return false;
    }

    @Override
    public boolean allowAttachmentType(ItemStack gun, AttachmentType type) {
        GunData data = TimelessAPI.getGunData(this.getGunId(gun));
        return data != null && data.getAllowAttachments().contains(type);
    }

    @Override
    public boolean useInventoryAmmo(ItemStack gun) {
        // Por padrão false, a arma usa munição interna ou carregador
        return false;
    }

    @Override
    public boolean hasInventoryAmmo(LivingEntity shooter, ItemStack gun, boolean needCheckAmmo) {
        // Verifica se tem munição no inventário (para recarga)
        return false; // TODO: Implementar verificação de inventário
    }

    @Override
    public int getRPM(ItemStack gun) {
        GunData data = TimelessAPI.getGunData(this.getGunId(gun));
        return data != null ? data.getRoundsPerMinute() : 300;
    }

    @Override
    public boolean isCanCrawl(ItemStack gun) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.literal("§6TaCZ Fabric Gun"));
        tooltipComponents.add(Component.literal("§7ID: " + getGunId(stack)));
        tooltipComponents.add(Component.literal("§eAmmo: " + getCurrentAmmoCount(stack)));
    }
}