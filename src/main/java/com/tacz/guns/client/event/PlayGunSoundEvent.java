//package com.tacz.guns.client.event;
//
//import com.mojang.blaze3d.audio.SoundBuffer;
//import com.tacz.guns.client.sound.GunSoundInstance;
//
//@Mod.EventBusSubscriber(
//Value = EnvType.CLIENT)
//public class PlayGunSoundEvent {
//    @SubscribeEvent
//    public static void onPlaySoundSource(PlaySoundSourceEvent event) {
//        if (event.getSound() instanceof GunSoundInstance instance) {
//            SoundBuffer soundBuffer = instance.getSoundBuffer();
//            if (soundBuffer != null) {
//                event.getChannel().attachStaticBuffer(soundBuffer);
//                event.getChannel().play();
//            }
//        }
//    }
//}
