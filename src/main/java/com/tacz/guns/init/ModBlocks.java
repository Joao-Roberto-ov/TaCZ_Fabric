package com.tacz.guns.init;

import com.tacz.guns.GunMod;
import com.tacz.guns.block.GunSmithTableBlock; // Verifique o nome exato da classe que sobrou na pasta block
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    // Exemplo: Gun Smith Table
    // Você precisará confirmar se a classe GunSmithTableBlock ainda existe e ajustar os imports
    public static final Block GUN_SMITH_TABLE = register("gun_smith_table", new GunSmithTableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));

    // Adicione aqui os outros blocos (Statue, Target, etc) seguindo o padrão

    public static final Block WORKBENCH_111 = register("workbench_111", new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK))); // Placeholder

    public static void init() {}

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, name), block);
    }
}