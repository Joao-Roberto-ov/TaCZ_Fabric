//package com.tacz.guns.event;
//
//import com.tacz.guns.api.entity.KnockBackModifier;
//
//@Mod.EventBusSubscriber
//public class KnockbackChange {
//    @SubscribeEvent
//    public static void onKnockback(LivingKnockBackEvent event) {
//        KnockBackModifier modifier = KnockBackModifier.fromLivingEntity(event.getEntity());
//        double strength = modifier.getKnockBackStrength();
//        if (strength >= 0) {
//            event.setStrength((float) strength);
//        }
//    }
//}
