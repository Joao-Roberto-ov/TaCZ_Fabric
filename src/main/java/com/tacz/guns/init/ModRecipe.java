//package com.tacz.guns.init;
//
//import com.tacz.guns.GunModFabric;
//import com.tacz.guns.crafting.GunSmithTableRecipe;
//import com.tacz.guns.crafting.GunSmithTableSerializer;
//import net.minecraft.world.item.crafting.RecipeSerializer;
//import net.minecraft.world.item.crafting.RecipeType;
//
//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
//public class ModRecipe {
//    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, GunModFabric.MOD_ID);
//    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, GunModFabric.MOD_ID);
//
//    public static RegistryObject<RecipeSerializer<?>> GUN_SMITH_TABLE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("gun_smith_table_crafting", GunSmithTableSerializer::new);
//    public static RegistryObject<RecipeType<GunSmithTableRecipe>> GUN_SMITH_TABLE_CRAFTING = RECIPE_TYPES.register("gun_smith_table_crafting", () -> new RecipeType<>() {
//        @Override
//        public String toString() {
//            return GunModFabric.MOD_ID + ":gun_smith_table_crafting";
//        }
//    });
//
//}
