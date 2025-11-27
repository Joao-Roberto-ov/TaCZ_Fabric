//package com.tacz.guns.client.init;
//
//import com.tacz.guns.GunModFabric;
//import com.tacz.guns.client.particle.BulletHoleParticle;
//import com.tacz.guns.init.ModParticles;
//
//
//@Mod.EventBusSubscriber(modid = GunModFabric.MOD_ID,
//Value = EnvType.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
//public class ParticleFactoryRegistry {
//    @SubscribeEvent
//    public static void onRegisterParticleFactory(RegisterParticleProvidersEvent event) {
//        event.registerSpecial(ModParticles.BULLET_HOLE.get(), new BulletHoleParticle.Provider());
//    }
//}