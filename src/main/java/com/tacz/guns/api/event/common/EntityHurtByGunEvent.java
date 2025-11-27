package com.tacz.guns.api.event.common;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class EntityHurtByGunEvent {

    // Classes estáticas internas para manter compatibilidade com código que usa EntityHurtByGunEvent.Pre/Post
    public static class Pre {
        private final Entity entity;
        private final LivingEntity attacker;
        private final ItemStack gun;
        private final ItemStack bullet; // Supondo que tenha bullet, adicionei baseado no seu erro 'getBullet'
        private float damage;
        private final boolean isHeadShot;
        private final float headshotMultiplier;

        public Pre(Entity entity, LivingEntity attacker, ItemStack gun, ItemStack bullet, float damage, boolean isHeadShot, float headshotMultiplier) {
            this.entity = entity;
            this.attacker = attacker;
            this.gun = gun;
            this.bullet = bullet;
            this.damage = damage;
            this.isHeadShot = isHeadShot;
            this.headshotMultiplier = headshotMultiplier;
        }

        public Entity getEntity() { return entity; }
        public LivingEntity getAttacker() { return attacker; }
        public ItemStack getGun() { return gun; }
        public ItemStack getBullet() { return bullet; }
        public float getDamage() { return damage; }
        public void setDamage(float damage) { this.damage = damage; }
        public boolean isHeadShot() { return isHeadShot; }
        public float getHeadshotMultiplier() { return headshotMultiplier; }

        public boolean isCanceled() { return false; }
    }

    public static class Post {
        private final Entity entity;
        private final LivingEntity attacker;
        private final ItemStack gun;
        private final ItemStack bullet;
        private final float damage;
        private final boolean isHeadShot;
        private final float headshotMultiplier;

        public Post(Entity entity, LivingEntity attacker, ItemStack gun, ItemStack bullet, float damage, boolean isHeadShot, float headshotMultiplier) {
            this.entity = entity;
            this.attacker = attacker;
            this.gun = gun;
            this.bullet = bullet;
            this.damage = damage;
            this.isHeadShot = isHeadShot;
            this.headshotMultiplier = headshotMultiplier;
        }

        public Entity getEntity() { return entity; }
        public LivingEntity getAttacker() { return attacker; }
        public ItemStack getGun() { return gun; }
        public ItemStack getBullet() { return bullet; }
        public float getDamage() { return damage; }
        public boolean isHeadShot() { return isHeadShot; }
        public float getHeadshotMultiplier() { return headshotMultiplier; }
    }
}