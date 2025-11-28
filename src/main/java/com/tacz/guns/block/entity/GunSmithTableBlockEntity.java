package com.tacz.guns.block.entity;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import com.tacz.guns.api.DefaultAssets;
import com.tacz.guns.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class GunSmithTableBlockEntity extends BlockEntity implements MenuProvider {
    // .get() removido dos argumentos
    public static final BlockEntityType<GunSmithTableBlockEntity> TYPE = BlockEntityType.Builder.of(GunSmithTableBlockEntity::new,
            ModBlocks.GUN_SMITH_TABLE,
            ModBlocks.WORKBENCH_111,
            ModBlocks.WORKBENCH_121,
            ModBlocks.WORKBENCH_211
    ).build(null);

    private static final String ID_TAG = "BlockId";

    @Nullable
    private ResourceLocation id = null;

    public GunSmithTableBlockEntity(BlockPos pos, BlockState blockState) {
        super(TYPE, pos, blockState);
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }

    @Nullable
    public ResourceLocation getId() {
        return id;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return this.saveWithoutMetadata(registryLookup);
    }

    // Removido @Environment(EnvType.CLIENT) se causar problemas, mas geralmente é seguro no Fabric se o loader lidar bem.
    // getRenderBoundingBox não existe no Fabric/Vanilla padrão da mesma forma, mas deixarei comentado se der erro.
    // public AABB getRenderBoundingBox() { ... }

    @Override
    public Component getDisplayName() {
        return Component.literal("Gun Smith Table");
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
        super.loadAdditional(tag, registryLookup);
        if (tag.contains(ID_TAG, Tag.TAG_STRING)) {
            this.id = ResourceLocation.tryParse(tag.getString(ID_TAG));
        } else {
            this.id = DefaultAssets.DEFAULT_BLOCK_ID;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
        super.saveAdditional(tag, registryLookup);
        if (id != null) {
            tag.putString(ID_TAG, id.toString());
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        // Retornando null por enquanto pois deletamos o Menu/Container
        return null;
    }
}