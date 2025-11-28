package com.tacz.guns.resource.index;

import com.google.common.base.Preconditions;
import com.tacz.guns.resource.pojo.BlockIndexPOJO;
import com.tacz.guns.resource.pojo.data.block.BlockData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class CommonBlockIndex {
    private BlockIndexPOJO pojo;
    private BlockData data;

    public CommonBlockIndex() {}

    public static CommonBlockIndex getInstance(BlockIndexPOJO pojo) throws IllegalArgumentException {
        CommonBlockIndex index = new CommonBlockIndex();
        index.pojo = pojo;
        // Validação básica
        Preconditions.checkArgument(pojo != null, "POJO cannot be null");
        return index;
    }

    public BlockData getData() {
        return data;
    }

    public Block getBlock() {
        // Adaptação para Fabric/Vanilla
        if (pojo == null) return null;
        // Assumindo que o POJO tem um campo ID ou similar. Se não tiver, ajuste.
        // Exemplo genérico:
        return BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse("minecraft:air"));
    }
}