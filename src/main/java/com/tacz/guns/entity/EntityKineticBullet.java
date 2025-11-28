package com.tacz.guns.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.resources.ResourceLocation;

public class EntityKineticBullet extends Projectile {
    private static final EntityDataAccessor<Integer> GRAVITY = SynchedEntityData.defineId(EntityKineticBullet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> SPEED = SynchedEntityData.defineId(EntityKineticBullet.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> FRICTION = SynchedEntityData.defineId(EntityKineticBullet.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DENSITY = SynchedEntityData.defineId(EntityKineticBullet.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> AERODYNAMICS = SynchedEntityData.defineId(EntityKineticBullet.class, EntityDataSerializers.FLOAT);

    private int life;
    private float damage = 5.0f;
    private float knockback = 0;
    private int igniteTime = 0;
    private int pierce = 1;
    private float extraDamage = 0;
    private float speed = 1.0f;

    public EntityKineticBullet(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    public EntityKineticBullet(EntityType<? extends Projectile> type, Level level, LivingEntity shooter) {
        super(type, level);
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(GRAVITY, 0);
        builder.define(SPEED, 1.0f);
        builder.define(FRICTION, 0.01f);
        builder.define(DENSITY, 1.0f);
        builder.define(AERODYNAMICS, 0.99f);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            // Partícula simples para debug visual
            this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        } else {
            // Movimento básico
            Vec3 motion = this.getDeltaMovement();
            HitResult result = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);

            if (result.getType() != HitResult.Type.MISS) {
                this.onHit(result);
            }

            this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);

            if (this.life++ > 200) {
                this.discard();
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity target = result.getEntity();
        DamageSource source = this.damageSources().thrown(this, this.getOwner());
        target.hurt(source, this.damage + this.extraDamage);
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.discard();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("Damage")) this.damage = compound.getFloat("Damage");
        if (compound.contains("Life")) this.life = compound.getInt("Life");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putFloat("Damage", this.damage);
        compound.putInt("Life", this.life);
    }

    // Stubs para manter compatibilidade com chamadas de outros arquivos
    public void setSpeed(float speed) { this.speed = speed; }
    public void setDamage(float damage) { this.damage = damage; }
    public void setKnockback(float knockback) { this.knockback = knockback; }
    public void setPierce(int pierce) { this.pierce = pierce; }
    public void setIgniteTime(int igniteTime) { this.igniteTime = igniteTime; }
    public void setExtraDamage(float extraDamage) { this.extraDamage = extraDamage; }
    public void setAmmoId(ResourceLocation id) {}
    public void setGunId(ResourceLocation id) {}
    public void setDistanceDamage(Object map) {}
}