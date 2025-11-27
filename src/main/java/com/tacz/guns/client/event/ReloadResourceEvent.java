//package com.tacz.guns.client.event;
//
//import com.tacz.guns.client.resource.InternalAssetLoader;
//import net.minecraft.resources.ResourceLocation;
//
//
//
//
//
//@Mod.EventBusSubscriber(
//Value = EnvType.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
//public class ReloadResourceEvent {
//    public static final ResourceLocation BLOCK_ATLAS_TEXTURE = new ResourceLocation("textures/atlas/blocks.png");
//
//    @SubscribeEvent
//    public static void onTextureStitchEventPost(TextureStitchEvent.Post event) {
//        if (BLOCK_ATLAS_TEXTURE.equals(event.getAtlas().location())) {
//            // InternalAssetLoader 需要加载一些默认的动画、模型，需要先于枪包加载。
//            InternalAssetLoader.onResourceReload();
////            ClientReloadManager.reloadAllPack();
//        }
//    }
//}
