//package com.tacz.guns.event;
//
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.player.Player;
//
//
//@Mod.EventBusSubscriber
//public class SyncBaseTimestamp {
//    @SubscribeEvent
//    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
//        Entity entity = event.getEntity();
//        if (entity instanceof Player player && !event.getLevel().isClientSide()) {
//            NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new ServerMessageSyncBaseTimestamp());
//        }
//    }
//}
