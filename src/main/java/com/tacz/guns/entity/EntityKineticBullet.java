package com.tacz.guns.entity;

import com.google.common.collect.Lists;
import com.tacz.guns.GunModFabric;
import com.tacz.guns.api.DefaultAssets;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.entity.ITargetEntity;
import com.tacz.guns.api.entity.KnockBackModifier;
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
import com.tacz.guns.util.block.BlockRayTrace;
import com.tacz.guns.util.block.ProjectileExplosion;
import com.tacz.guns.util.math.MathUtil;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class EntityKineticBullet extends Projectile {
    public static final EntityType<EntityKineticBullet> TYPE = EntityType.Builder.<EntityKineticBullet>of(EntityKineticBullet::new, MobCategory.MISC)
            .noSummon().noSave().fireImmune().sized(0.0625F, 0.0625F).clientTrackingRange(5).updateInterval(5)
            // .setShouldReceiveVelocityUpdates(false) // Removido pois não existe no Fabric/Vanilla mappings default ou mudou
            .build("bullet");

    public static final TagKey<EntityType<?>> USE_MAGIC_DAMAGE_ON = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("tacz", "use_magic_damage_on"));
    public static final TagKey<EntityType<?>> USE_VOID_DAMAGE_ON = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("tacz", "use_void_damage_on"));
    public static final TagKey<EntityType<?>> PRETEND_MELEE_DAMAGE_ON = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("tacz", "pretend_melee_damage_on"));

    private static final EntityDataAccessor<Integer> GRAVITY = SynchedEntityData.defineId(EntityKineticBullet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DAMAGE_AMOUNT = SynchedEntityData.defineId(EntityKineticBullet.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DISTANCE = SynchedEntityData.defineId(EntityKineticBullet.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> SPEED = SynchedEntityData.defineId(EntityKineticBullet.class, EntityDataSerializers.FLOAT);

    // ... (Resto dos campos) ...
    private ResourceLocation ammoId = DefaultAssets.DEFAULT_AMMO_ID;
    private ResourceLocation gunId = DefaultAssets.DEFAULT_GUN_ID;
    private ResourceLocation gunDisplayId = DefaultAssets.DEFAULT_GUN_DISPLAY_ID;

    public EntityKineticBullet(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    public EntityKineticBullet(Level level, LivingEntity shooter, ResourceLocation ammoId, ResourceLocation gunId, ResourceLocation gunDisplayId, boolean isTracer) {
        super(TYPE, level);
        this.setOwner(shooter);
        this.ammoId = ammoId;
        this.gunId = gunId;
        this.gunDisplayId = gunDisplayId;
        // ... Lógica simplificada ...
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(GRAVITY, 0);
        builder.define(DAMAGE_AMOUNT, 0f);
        builder.define(DISTANCE, 0f);
        builder.define(SPEED, 0f);
    }

    // Métodos que dependiam do Forge Networking e Events foram removidos ou comentados

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this); // ou ServerEntity.createPacket se necessário
    }

    @Override
    public void tick() {
        super.tick();
        // Lógica de tick simplificada ou completa se não tiver dependências externas quebradas
    }

    // ... (Outros métodos) ...
}