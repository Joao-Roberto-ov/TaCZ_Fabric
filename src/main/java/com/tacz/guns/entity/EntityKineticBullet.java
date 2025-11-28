package com.tacz.guns.entity;

import com.google.common.collect.Lists;
import com.tacz.guns.GunModFabric;
import com.tacz.guns.api.DefaultAssets;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.entity.ITargetEntity;
import com.tacz.guns.api.entity.KnockBackModifier;
import com.tacz.guns.api.event.common.EntityHurtByGunEvent;
import com.tacz.guns.api.event.server.AmmoHitBlockEvent;
import com.tacz.guns.client.particle.AmmoParticleSpawner;
import com.tacz.guns.config.common.AmmoConfig;
import com.tacz.guns.config.sync.SyncConfig;
import com.tacz.guns.init.ModDamageTypes;
import com.tacz.guns.particles.BulletHoleOption;
import com.tacz.guns.resource.modifier.AttachmentCacheProperty;
import com.tacz.guns.resource.modifier.custom.*;
import com.tacz.guns.resource.pojo.data.gun.BulletData;
import com.tacz.guns.resource.pojo.data.gun.ExplosionData;
import com.tacz.guns.resource.pojo.data.gun.ExtraDamage.DistanceDamagePair;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import com.tacz.guns.resource.pojo.data.gun.Ignite;
import com.tacz.guns.util.EntityUtil;
import com.tacz.guns.util.ExplodeUtil;
import com.tacz.guns.util.TacHitResult;
import com.tacz.guns.util.block.BlockRayTrace;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.*;

import static com.tacz.guns.api.event.common.GunDamageSourcePart.ARMOR_PIERCING;
import static com.tacz.guns.api.event.common.GunDamageSourcePart.NON_ARMOR_PIERCING;

/**
 * 动能武器打出的子弹实体。
 */
public class EntityKineticBullet extends Projectile {
    // Ajuste para 1.21: setShouldReceiveVelocityUpdates removido (agora é padrão ou controlado de outra forma)
    public static final EntityType<EntityKineticBullet> TYPE = EntityType.Builder.<EntityKineticBullet>of(EntityKineticBullet::new, MobCategory.MISC)
            .noSummon().noSave().fireImmune().sized(0.0625F, 0.0625F).clientTrackingRange(5).updateInterval(5).build("bullet");

    public static final TagKey<EntityType<?>> USE_MAGIC_DAMAGE_ON = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("tacz", "use_magic_damage_on"));
    public static final TagKey<EntityType<?>> USE_VOID_DAMAGE_ON = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("tacz", "use_void_damage_on"));
    public static final TagKey<EntityType<?>> PRETEND_MELEE_DAMAGE_ON = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("tacz", "pretend_melee_damage_on"));

    public static final String TRACER_COLOR_OVERRIDER_KEY = GunModFabric.MOD_ID + ":tracer_override";
    public static final String TRACER_SIZE_OVERRIDER_KEY = GunModFabric.MOD_ID + ":tracer_size";

    private ResourceLocation ammoId = DefaultAssets.EMPTY_AMMO_ID;
    private int life = 200;
    private float speed = 1;
    private float gravity = 0;
    private float friction = 0.01F;
    private LinkedList<DistanceDamagePair> damageAmount = Lists.newLinkedList();
    private float distanceAmount = 0;
    private float knockback = 0;
    private boolean explosion = false;
    private boolean igniteEntity = false;
    private boolean igniteBlock = false;
    private int igniteEntityTime = 2;
    private float explosionDamage = 3;
    private float explosionRadius = 3;
    private int explosionDelayCount = Integer.MAX_VALUE;
    private boolean explosionKnockback = false;
    private boolean explosionDestroyBlock = false;
    private float damageModifier = 1;
    private int pierce = 1;
    private Vec3 startPos;
    private boolean isTracerAmmo;
    private float cameraXRot;
    private float cameraYRot;
    private Vector3f firstPersonRenderOffset;
    private ResourceLocation gunId;
    private ResourceLocation gunDisplayId;
    private float armorIgnore;
    private float headShot;

    // Campo para substituir getPersistentData do Forge
    private final CompoundTag persistentData = new CompoundTag();

    public EntityKineticBullet(EntityType<? extends Projectile> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityKineticBullet(EntityType<? extends Projectile> type, double x, double y, double z, Level worldIn) {
        this(type, worldIn);
        this.setPos(x, y, z);
    }

    public EntityKineticBullet(Level worldIn, LivingEntity throwerIn, ItemStack gunItem, ResourceLocation ammoId, ResourceLocation gunId,
                               ResourceLocation gunDisplayId, boolean isTracerAmmo, GunData gunData, BulletData bulletData) {
        this(TYPE, worldIn, throwerIn, gunItem, ammoId, gunId, gunDisplayId, isTracerAmmo, gunData, bulletData);
    }

    public EntityKineticBullet(Level worldIn, LivingEntity throwerIn, ItemStack gunItem, ResourceLocation ammoId, ResourceLocation gunId, boolean isTracerAmmo, GunData gunData, BulletData bulletData) {
        this(TYPE, worldIn, throwerIn, gunItem, ammoId, gunId, DefaultAssets.DEFAULT_GUN_DISPLAY_ID, isTracerAmmo, gunData, bulletData);
    }

    protected EntityKineticBullet(EntityType<? extends Projectile> type, Level worldIn, LivingEntity throwerIn, ItemStack gunItem,
                                  ResourceLocation ammoId, ResourceLocation gunId, ResourceLocation gunDisplayId,
                                  boolean isTracerAmmo, GunData gunData, BulletData bulletData) {
        this(type, throwerIn.getX(), throwerIn.getEyeY() - (double) 0.1F, throwerIn.getZ(), worldIn);
        this.setOwner(throwerIn);
        AttachmentCacheProperty cacheProperty = Objects.requireNonNull(IGunOperator.fromLivingEntity(throwerIn).getCacheProperty());
        this.armorIgnore = Mth.clamp(cacheProperty.getCache(ArmorIgnoreModifier.ID), 0f, 1f);
        this.headShot = Math.max(cacheProperty.getCache(HeadShotModifier.ID), 0f);
        this.knockback = Math.max(cacheProperty.getCache(KnockbackModifier.ID), 0f);
        this.ammoId = ammoId;
        this.life = Mth.clamp((int) (bulletData.getLifeSecond() * 20), 1, Integer.MAX_VALUE);
        this.speed = Mth.clamp(cacheProperty.<Float>getCache(AmmoSpeedModifier.ID) / 20f, 0f, 30f);
        this.gravity = Mth.clamp(bulletData.getGravity(), 0f, Float.MAX_VALUE);
        this.friction = Mth.clamp(bulletData.getFriction(), 0f, Float.MAX_VALUE);
        Ignite ignite = cacheProperty.getCache(IgniteModifier.ID);
        this.igniteEntity = bulletData.getIgnite().isIgniteEntity() || ignite.isIgniteEntity();
        this.igniteEntityTime = Math.max(bulletData.getIgniteEntityTime(), 0);
        this.igniteBlock = bulletData.getIgnite().isIgniteBlock() || ignite.isIgniteBlock();
        this.damageAmount = cacheProperty.getCache(DamageModifier.ID);
        this.distanceAmount = cacheProperty.getCache(EffectiveRangeModifier.ID);
        if (bulletData.getBulletAmount() > 1) {
            this.damageModifier = 1f / bulletData.getBulletAmount();
        }
        this.pierce = Mth.clamp(cacheProperty.getCache(PierceModifier.ID), 1, Integer.MAX_VALUE);
        ExplosionData explosionData = cacheProperty.getCache(ExplosionModifier.ID);
        if (explosionData != null) {
            this.explosion = explosionData.isExplode();
            this.explosionDamage = (float) Mth.clamp(explosionData.getDamage() * SyncConfig.DAMAGE_BASE_MULTIPLIER.get(), 0, Float.MAX_VALUE);
            this.explosionRadius = Mth.clamp(explosionData.getRadius(), 0, Float.MAX_VALUE);
            this.explosionKnockback = explosionData.isKnockback();
            int delayTickCount = (int)(explosionData.getDelay() * 20);
            if (delayTickCount < 0) {
                delayTickCount = Integer.MAX_VALUE;
            }
            this.explosionDestroyBlock = explosionData.isDestroyBlock() && AmmoConfig.EXPLOSIVE_AMMO_DESTROYS_BLOCK.get();
            this.explosionDelayCount = Math.max(delayTickCount, 1);
        }
        double posX = throwerIn.xOld + (throwerIn.getX() - throwerIn.xOld) / 2.0;
        double posY = throwerIn.yOld + (throwerIn.getY() - throwerIn.yOld) / 2.0 + throwerIn.getEyeHeight();
        double posZ = throwerIn.zOld + (throwerIn.getZ() - throwerIn.zOld) / 2.0;
        this.setPos(posX, posY, posZ);
        this.startPos = this.position();
        this.isTracerAmmo = isTracerAmmo;
        this.gunId = gunId;
        this.gunDisplayId = gunDisplayId;
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
        // Implementação vazia necessária para 1.21 (Builder)
    }

    @Override
    public void tick() {
        super.tick();
        this.onBulletTick();
        if (this.level().isClientSide) {
            // Substituição de DistExecutor para Fabric
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                AmmoParticleSpawner.addParticle(this);
            }
        }
        Vec3 movement = this.getDeltaMovement();
        double x = movement.x;
        double y = movement.y;
        double z = movement.z;
        double distance = movement.horizontalDistance();
        this.setYRot((float) Math.toDegrees(Mth.atan2(x, z)));
        this.setXRot((float) Math.toDegrees(Mth.atan2(y, distance)));
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }
        this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
        this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
        double nextPosX = this.getX() + x;
        double nextPosY = this.getY() + y;
        double nextPosZ = this.getZ() + z;
        this.setPos(nextPosX, nextPosY, nextPosZ);
        float friction = this.friction;
        float gravity = this.gravity;
        if (this.isInWater()) {
            for (int i = 0; i < 4; i++) {
                this.level().addParticle(ParticleTypes.BUBBLE, nextPosX - x * 0.25F, nextPosY - y * 0.25F, nextPosZ - z * 0.25F, x, y, z);
            }
            friction = 0.4F;
            gravity *= 0.6F;
        }
        this.setDeltaMovement(this.getDeltaMovement().scale(1 - friction));
        this.setDeltaMovement(this.getDeltaMovement().add(0, -gravity, 0));
        if (this.tickCount >= this.life - 1) {
            this.discard();
        }
    }

    protected void onBulletTick() {
        if (!this.level().isClientSide()) {
            if (this.explosion) {
                if (this.explosionDelayCount > 0) {
                    this.explosionDelayCount--;
                } else {
                    ExplodeUtil.createExplosion(this.getOwner(), this, this.explosionDamage, this.explosionRadius, this.explosionKnockback, this.explosionDestroyBlock, this.position());
                    this.discard();
                    return;
                }
            }
            Vec3 startVec = this.position();
            Vec3 endVec = startVec.add(this.getDeltaMovement());
            HitResult result = BlockRayTrace.rayTraceBlocks(this.level(), new ClipContext(startVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            BlockHitResult resultB = (BlockHitResult) result;
            if (resultB.getType() != HitResult.Type.MISS) {
                endVec = resultB.getLocation();
            }

            List<EntityResult> hitEntities = null;
            if (this.pierce <= 1 || this.explosion) {
                EntityResult entityResult = EntityUtil.findEntityOnPath(this, startVec, endVec);
                if (entityResult != null) {
                    hitEntities = Collections.singletonList(entityResult);
                }
            } else {
                hitEntities = EntityUtil.findEntitiesOnPath(this, startVec, endVec);
            }
            if (hitEntities != null && !hitEntities.isEmpty()) {
                EntityResult[] hitEntityResult = hitEntities.toArray(new EntityResult[0]);
                for (int i = 0; (i < this.pierce || i < 1) && i < (hitEntityResult.length - 1); i++) {
                    int k = i;
                    for (int j = i + 1; j < hitEntityResult.length; j++) {
                        if (hitEntityResult[j].hitVec.distanceTo(startVec) < hitEntityResult[k].hitVec.distanceTo(startVec)) {
                            k = j;
                        }
                    }
                    EntityResult t = hitEntityResult[i];
                    hitEntityResult[i] = hitEntityResult[k];
                    hitEntityResult[k] = t;
                }
                for (EntityResult entityResult : hitEntityResult) {
                    result = new TacHitResult(entityResult);
                    this.onHitEntity((TacHitResult) result, startVec, endVec);
                    this.pierce--;
                    if (this.pierce < 1 || this.explosion) {
                        this.discard();
                        return;
                    }
                }
            }
            this.onHitBlock(resultB, startVec, endVec);
        }
    }

    public void shoot(double pitch, double yaw, float pVelocity, Vector2d vector2d) {
        Vector3d left = new Vector3d(vector2d.x, vector2d.y, 8);
        left.rotateX(pitch * Mth.DEG_TO_RAD);
        left.rotateY(-yaw * Mth.DEG_TO_RAD);
        Vec3 vec3 = new Vec3(left.x, left.y, left.z).normalize().scale(pVelocity);
        this.setDeltaMovement(vec3.x, vec3.y, vec3.z);
        double d0 = vec3.horizontalDistance();
        this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
        this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public void shootFromRotation(Entity pShooter, float pX, float pY, float pZ, float pVelocity, Vector2d vector2d) {
        this.shoot(pX, pY, pVelocity, vector2d);
        Vec3 vec3 = pShooter.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(vec3.x, pShooter.onGround() ? 0.0D : vec3.y, vec3.z));
    }

    public record MaybeMultipartEntity(Entity hitPart, Entity core) {
        public static MaybeMultipartEntity of(Entity hitPart) {
            // 1.21 não usa mais PartEntity da mesma forma, simplificando para Entity
            return new MaybeMultipartEntity(hitPart, hitPart);
        }
    }

    protected void onHitEntity(TacHitResult result, Vec3 startVec, Vec3 endVec) {
        if (result.getEntity() instanceof ITargetEntity targetEntity) {
            DamageSource source = this.damageSources().thrown(this, this.getOwner());
            targetEntity.onProjectileHit(this, result, source, this.getDamage(result.getLocation()));
            return;
        }
        Entity entity = result.getEntity();
        @Nullable Entity owner = this.getOwner();
        LivingEntity attacker = owner instanceof LivingEntity ? (LivingEntity) owner : null;
        var sources = createDamageSources(MaybeMultipartEntity.of(entity));
        boolean headshot = result.isHeadshot();
        float damage = this.getDamage(result.getLocation());
        float headShotMultiplier = Math.max(this.headShot, 0);

        // Removido MinecraftForge.EVENT_BUS
        // Em vez disso, apenas executamos a lógica padrão sem eventos do Forge por enquanto.
        // Se houver eventos Fabric equivalentes implementados, chame-os aqui.

        if (entity == null) {
            return;
        }
        if (this.igniteEntity && AmmoConfig.IGNITE_ENTITY.get()) {
            // 1.21 usa setRemainingFireTicks ou similar, mas setSecondsOnFire ainda existe em alguns mappings?
            // Se não, usar: entity.setRemainingFireTicks(this.igniteEntityTime * 20);
            entity.setRemainingFireTicks(this.igniteEntityTime * 20);
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.LAVA, entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ(), 1, 0, 0, 0, 0);
            }
        }
        if (headshot) {
            damage *= headShotMultiplier;
        }
        var parts = MaybeMultipartEntity.of(entity);
        if (parts.core() instanceof LivingEntity livingCore) {
            KnockBackModifier modifier = KnockBackModifier.fromLivingEntity(livingCore);
            modifier.setKnockBackStrength(this.knockback);
            tacAttackEntity(parts, damage, sources);
            modifier.resetKnockBackStrength();
        } else {
            tacAttackEntity(parts, damage, sources);
        }
        if (this.explosion) {
            parts.core().invulnerableTime = 0;
            ExplodeUtil.createExplosion(this.getOwner(), this, this.explosionDamage, this.explosionRadius, this.explosionKnockback, this.explosionDestroyBlock, result.getLocation());
        }
    }

    protected void onHitBlock(BlockHitResult result, Vec3 startVec, Vec3 endVec) {
        if (result.getType() == HitResult.Type.MISS) {
            return;
        }
        BlockPos pos = result.getBlockPos();
        Vec3 hitVec = result.getLocation();
        // Removido evento Forge AmmoHitBlockEvent

        super.onHitBlock(result);
        if (this.explosion) {
            ExplodeUtil.createExplosion(this.getOwner(), this, this.explosionDamage, this.explosionRadius, this.explosionKnockback, this.explosionDestroyBlock, hitVec);
            this.discard();
            return;
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            BulletHoleOption bulletHoleOption = new BulletHoleOption(result.getDirection(), result.getBlockPos(), this.ammoId.toString(), this.gunId.toString(), this.gunDisplayId.toString());
            serverLevel.sendParticles(bulletHoleOption, hitVec.x, hitVec.y, hitVec.z, 1, 0, 0, 0, 0);
            if (this.igniteBlock) {
                serverLevel.sendParticles(ParticleTypes.LAVA, hitVec.x, hitVec.y, hitVec.z, 1, 0, 0, 0, 0);
            }
        }
        if (this.igniteBlock && AmmoConfig.IGNITE_BLOCK.get()) {
            BlockPos offsetPos = pos.relative(result.getDirection());
            if (BaseFireBlock.canBePlacedAt(this.level(), offsetPos, result.getDirection())) {
                BlockState fireState = BaseFireBlock.getState(this.level(), offsetPos);
                this.level().setBlock(offsetPos, fireState, Block.UPDATE_ALL_IMMEDIATE);
                ((ServerLevel) this.level()).sendParticles(ParticleTypes.LAVA, hitVec.x - 1.0 + this.random.nextDouble() * 2.0, hitVec.y, hitVec.z - 1.0 + this.random.nextDouble() * 2.0, 4, 0, 0, 0, 0);
            }
        }
        this.discard();
    }

    public float getDamage(Vec3 hitVec) {
        double playerDistance = hitVec.distanceTo(this.startPos);
        for (DistanceDamagePair pair : this.damageAmount) {
            float effectiveDistance = this.damageAmount.get(0).getDistance() == pair.getDistance() ? this.distanceAmount : pair.getDistance();
            if (playerDistance < effectiveDistance) {
                float damage = pair.getDamage();
                return Math.max(damage * this.damageModifier, 0F);
            }
        }
        return 0;
    }

    private Pair<DamageSource, DamageSource> createDamageSources(MaybeMultipartEntity parts) {
        DamageSource source1, source2;
        var hitPartType = parts.hitPart().getType();
        var directCause = hitPartType.is(PRETEND_MELEE_DAMAGE_ON) ? this.getOwner() : this;
        if (hitPartType.is(USE_MAGIC_DAMAGE_ON)) {
            source1 = source2 = this.damageSources().indirectMagic(this, getOwner());
        } else if (hitPartType.is(USE_VOID_DAMAGE_ON)) {
            source1 = ModDamageTypes.Sources.bulletVoid(this.level().registryAccess(), directCause, this.getOwner(), false);
            source2 = ModDamageTypes.Sources.bulletVoid(this.level().registryAccess(), directCause, this.getOwner(), true);
        } else {
            source1 = ModDamageTypes.Sources.bullet(this.level().registryAccess(), directCause, this.getOwner(), false);
            source2 = ModDamageTypes.Sources.bullet(this.level().registryAccess(), directCause, this.getOwner(), true);
        }
        return Pair.of(source1, source2);
    }

    private void tacAttackEntity(MaybeMultipartEntity parts, float damage, Pair<DamageSource, DamageSource> sources) {
        var source1 = sources.getLeft();
        var source2 = sources.getRight();
        float armorDamagePercent = Mth.clamp(this.armorIgnore, 0.0F, 1.0F);
        float normalDamagePercent = 1 - armorDamagePercent;
        parts.core().invulnerableTime = 0;
        parts.hitPart().hurt(source1, damage * normalDamagePercent);
        parts.core().invulnerableTime = 0;
        parts.hitPart().hurt(source2, damage * armorDamagePercent);
    }

    public ResourceLocation getAmmoId() { return ammoId; }
    public ResourceLocation getGunId() { return gunId; }
    public ResourceLocation getGunDisplayId() { return gunDisplayId; }
    public boolean isTracerAmmo() { return isTracerAmmo; }
    public RandomSource getRandom() { return this.random; }
    public float getCameraYRot() { return cameraYRot; }
    public void setCameraYRot(float cameraYRot) { this.cameraYRot = cameraYRot; }
    public float getCameraXRot() { return cameraXRot; }
    public void setCameraXRot(float cameraXRot) { this.cameraXRot = cameraXRot; }
    public Vector3f getFirstPersonRenderOffset() { return firstPersonRenderOffset; }
    public void setFirstPersonRenderOffset(Vector3f originRenderOffset) { this.firstPersonRenderOffset = originRenderOffset; }

    public CompoundTag getPersistentData() {
        return persistentData;
    }

    public Optional<float[]> getTracerColorOverride() {
        var pd = getPersistentData();
        if (!pd.contains(TRACER_COLOR_OVERRIDER_KEY, Tag.TAG_INT_ARRAY)) {
            return Optional.empty();
        } else {
            var ints = pd.getIntArray(TRACER_COLOR_OVERRIDER_KEY);
            switch (ints.length) {
                case 0: return Optional.empty();
                case 1: return Optional.of(new float[]{ints[0] / 255F, ints[0] / 255F, ints[0] / 255F, 1});
                case 2: return Optional.of(new float[]{ints[0] / 255F, ints[0] / 255F, ints[0] / 255F, ints[1] / 255F});
                case 3: return Optional.of(new float[]{ints[0] / 255F, ints[1] / 255F, ints[2] / 255F, 1});
                default: return Optional.of(new float[]{ints[0] / 255F, ints[1] / 255F, ints[2] / 255F, ints[3] / 255F});
            }
        }
    }

    public float getTracerSizeOverride() {
        var pd = getPersistentData();
        return pd.contains(TRACER_SIZE_OVERRIDER_KEY, Tag.TAG_ANY_NUMERIC) ? pd.getFloat(TRACER_SIZE_OVERRIDER_KEY) : 1;
    }

    @Override
    public boolean ownedBy(@Nullable Entity entity) {
        if (entity == null) {
            return false;
        }
        return super.ownedBy(entity);
    }

    public static class EntityResult {
        private final Entity entity;
        private final Vec3 hitVec;
        private final boolean headshot;

        public EntityResult(Entity entity, Vec3 hitVec, boolean headshot) {
            this.entity = entity;
            this.hitVec = hitVec;
            this.headshot = headshot;
        }
        public Entity getEntity() { return this.entity; }
        public Vec3 getHitPos() { return this.hitVec; }
        public boolean isHeadshot() { return this.headshot; }
    }
}