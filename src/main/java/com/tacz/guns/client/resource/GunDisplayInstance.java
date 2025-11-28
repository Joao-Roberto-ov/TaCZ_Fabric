package com.tacz.guns.client.resource;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.tacz.guns.GunModFabric;
import com.tacz.guns.api.client.animation.AnimationController;
import com.tacz.guns.api.client.animation.Animations;
import com.tacz.guns.api.client.animation.ObjectAnimation;
import com.tacz.guns.api.client.animation.gltf.AnimationStructure;
import com.tacz.guns.api.client.animation.statemachine.LuaAnimationStateMachine;
import com.tacz.guns.api.client.animation.statemachine.LuaStateMachineFactory;
import com.tacz.guns.api.client.other.GunModelTypeManager;
import com.tacz.guns.api.item.gun.FireMode;
import com.tacz.guns.client.animation.statemachine.GunAnimationStateContext;
import com.tacz.guns.client.model.BedrockGunModel;
import com.tacz.guns.client.resource.pojo.animation.bedrock.BedrockAnimationFile;
import com.tacz.guns.client.resource.pojo.display.LaserConfig;
import com.tacz.guns.client.resource.pojo.display.ammo.AmmoParticle;
import com.tacz.guns.client.resource.pojo.display.ammo.MuzzleFlash;
import com.tacz.guns.client.resource.pojo.display.ammo.ShellEjection;
import com.tacz.guns.client.resource.pojo.display.gun.*;
import com.tacz.guns.client.resource.pojo.model.BedrockModelPOJO;
import com.tacz.guns.client.resource.pojo.model.BedrockVersion;
import com.tacz.guns.client.resource.pojo.model.GeometryModelLegacy;
import com.tacz.guns.client.resource.pojo.model.GeometryModelNew;
import com.tacz.guns.client.sound.SoundPlayManager;
import com.tacz.guns.sound.SoundManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class GunDisplayInstance {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();
    private @Nullable ResourceLocation modelTexture;
    private @Nullable ResourceLocation hudTexture;
    private @Nullable ResourceLocation hudEmptyTexture;
    private @Nullable ResourceLocation slotTexture;
    private @Nullable String thirdPersonAnimation = "rifle_default";
    private @Nullable BedrockGunModel gunModel;
    private @Nullable AnimationStructure gltfAnimation;
    private @Nullable BedrockAnimationFile bedrockAnimation;
    private @Nullable LuaAnimationStateMachine animationStateMachine;
    private @Nullable ShellEjection shellEjection;
    private @Nullable AmmoParticle particle;
    private @Nullable MuzzleFlash muzzleFlash;
    private @Nullable Map<String, ResourceLocation> soundMaps;
    private @Nullable TransformScale transformScale;
    private @Nullable GunTransform transform;
    private @Nullable LayerGunShow offhandShow;
    private @Nullable Int2ObjectArrayMap<LayerGunShow> hotbarShow;
    private float ironZoom = 1.2f;
    private float zoomModelFov = 70f;
    private boolean showCrosshair = false;
    private @Nullable ResourceLocation playerAnimator3rd = ResourceLocation.fromNamespaceAndPath(GunModFabric.MOD_ID, "rifle_default.player_animation");
    private boolean is3rdFixedHand = false;
    private final EnumMap<FireMode, ControllableData> controllableData = new EnumMap<>(FireMode.class);
    private AmmoCountStyle ammoCountStyle = AmmoCountStyle.PERCENT;
    private DamageStyle damageStyle = DamageStyle.BASE_OVAL;
    private @Nullable LaserConfig laserConfig;
    private @Nullable TextShow textShow;
    private float @Nullable [] tracerColor;
    private @Nullable LuaTable stateMachineParam = null;

    private GunDisplayInstance() {
    }

    public static @Nullable GunDisplayInstance create(@Nullable GunDisplayPojo gunDisplayPojo) {
        if (gunDisplayPojo == null) {
            return null;
        }
        GunDisplayInstance gunDisplay = new GunDisplayInstance();
        checkTexture(gunDisplayPojo, gunDisplay);
        checkModel(gunDisplayPojo, gunDisplay);
        checkAnimation(gunDisplayPojo, gunDisplay);
        checkSounds(gunDisplayPojo, gunDisplay);
        checkTransform(gunDisplayPojo, gunDisplay);
        checkShellEjection(gunDisplayPojo, gunDisplay);
        checkGunFlash(gunDisplayPojo, gunDisplay);
        checkOffhandShow(gunDisplayPojo, gunDisplay);
        checkHotbarShow(gunDisplayPojo, gunDisplay);
        checkZoom(gunDisplayPojo, gunDisplay);
        checkParticle(gunDisplayPojo, gunDisplay);
        checkPlayerAnimator3rd(gunDisplayPojo, gunDisplay);
        checkControllableData(gunDisplayPojo, gunDisplay);
        checkHud(gunDisplayPojo, gunDisplay);
        checkLaser(gunDisplayPojo, gunDisplay);
        checkTextShow(gunDisplayPojo, gunDisplay);
        checkTracerColor(gunDisplayPojo, gunDisplay);
        checkStateMachineParam(gunDisplayPojo, gunDisplay);
        return gunDisplay;
    }

    private static void checkStateMachineParam(GunDisplayPojo gunDisplayPojo, GunDisplayInstance gunDisplay) {
        if (gunDisplayPojo.getStateMachineParam() != null) {
            gunDisplay.stateMachineParam = gunDisplayPojo.getStateMachineParam();
        }
    }

    private static void checkTracerColor(GunDisplayPojo gunDisplayPojo, GunDisplayInstance gunDisplay) {
        if (gunDisplayPojo.getTracerColor() != null) {
            try {
                String colorHex = gunDisplayPojo.getTracerColor();
                if (colorHex.startsWith("#")) {
                    colorHex = colorHex.substring(1);
                }
                int i = Integer.parseInt(colorHex, 16);
                float r = (float) (i >> 16 & 255) / 255.0F;
                float g = (float) (i >> 8 & 255) / 255.0F;
                float b = (float) (i & 255) / 255.0F;
                gunDisplay.tracerColor = new float[]{r, g, b};
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void checkTextShow(GunDisplayPojo gunDisplayPojo, GunDisplayInstance gunDisplay) {
        if (gunDisplayPojo.getTextShow() != null) {
            gunDisplay.textShow = gunDisplayPojo.getTextShow();
        }
    }

    private static void checkLaser(GunDisplayPojo gunDisplayPojo, GunDisplayInstance gunDisplay) {
        if (gunDisplayPojo.getLaserConfig() != null) {
            gunDisplay.laserConfig = gunDisplayPojo.getLaserConfig();
        }
    }

    private static void checkHud(GunDisplayPojo gunDisplayPojo, GunDisplayInstance gunDisplay) {
        gunDisplay.showCrosshair = gunDisplayPojo.isShowCrosshair();
        if (gunDisplayPojo.getAmmoCountStyle() != null) {
            gunDisplay.ammoCountStyle = gunDisplayPojo.getAmmoCountStyle();
        }
        if (gunDisplayPojo.getDamageStyle() != null) {
            gunDisplay.damageStyle = gunDisplayPojo.getDamageStyle();
        }
    }

    private static void checkControllableData(GunDisplayPojo gunDisplayPojo, GunDisplayInstance gunDisplay) {
        Map<FireMode, ControllableData> data = gunDisplayPojo.getControllableData();
        if (data != null) {
            gunDisplay.controllableData.putAll(data);
        }
    }

    private static void checkPlayerAnimator3rd(GunDisplayPojo gunDisplayPojo, GunDisplayInstance gunDisplay) {
        if (gunDisplayPojo.getPlayerAnimator3rd() != null) {
            gunDisplay.playerAnimator3rd = ResourceLocation.parse(gunDisplayPojo.getPlayerAnimator3rd());
        }
        gunDisplay.is3rdFixedHand = gunDisplayPojo.is3rdFixedHand();
    }

    private static void checkParticle(GunDisplayPojo gunDisplayPojo, GunDisplayInstance gunDisplay) {
        AmmoParticle particle = gunDisplayPojo.getParticle();
        if (particle != null) {
            try {
                String name = particle.getName();
                if (StringUtils.isNoneBlank(name)) {
                    // Linha comentada para evitar erro de compilação no Fabric 1.21 temporariamente
                    // particle.setParticleOptions(ParticleArgument.readParticle(new StringReader(name), BuiltInRegistries.PARTICLE_TYPE.asLookup()));
                    gunDisplay.particle = particle;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void checkZoom(GunDisplayPojo gunDisplayPojo, GunDisplayInstance gunDisplay) {
        if (gunDisplayPojo.getIronZoom() > 0) {
            gunDisplay.ironZoom = gunDisplayPojo.getIronZoom();
        }
        if (gunDisplayPojo.getZoomModelFov() > 0) {
            gunDisplay.zoomModelFov = gunDisplayPojo.getZoomModelFov();
        }
    }

    private static void checkHotbarShow(GunDisplayPojo gunDisplayPojo, GunDisplayInstance gunDisplay) {
        Int2ObjectArrayMap<LayerGunShow> show = gunDisplayPojo.getHotbarShow();
        if (show != null) {
            gunDisplay.hotbarShow = show;
        }
    }

    private static void checkOffhandShow(GunDisplayPojo gunDisplayPojo, GunDisplayInstance gunDisplay) {
        LayerGunShow show = gunDisplayPojo.getOffhandShow();
        if (show != null) {
            gunDisplay.offhandShow = show;
        }
    }

    private static void checkGunFlash(GunDisplayPojo gunDisplayPojo, GunDisplayInstance gunDisplay) {
        MuzzleFlash flash = gunDisplayPojo.getMuzzleFlash();
        if (flash != null) {
            gunDisplay.muzzleFlash = flash;
        }
    }

    private static void checkShellEjection(GunDisplayPojo gunDisplayPojo, GunDisplayInstance gunDisplay) {
        ShellEjection shellEjection = gunDisplayPojo.getShellEjection();
        if (shellEjection != null) {
            gunDisplay.shellEjection = shellEjection;
        }
    }

    private static void checkTransform(GunDisplayPojo gunDisplayPojo, GunDisplayInstance gunDisplay) {
        TransformScale scale = gunDisplayPojo.getTransformScale();
        if (scale != null) {
            gunDisplay.transformScale = scale;
        }
        GunTransform transform = gunDisplayPojo.getTransform();
        if (transform != null) {
            gunDisplay.transform = transform;
        }
    }

    private static void checkSounds(GunDisplayPojo gunDisplayPojo, GunDisplayInstance gunDisplay) {
        Map<String, ResourceLocation> soundMaps = gunDisplayPojo.getSounds();
        if (soundMaps == null) {
            soundMaps = Maps.newHashMap();
        }
        gunDisplay.soundMaps = soundMaps;
    }

    private static void checkAnimation(GunDisplayPojo gunDisplayPojo, GunDisplayInstance gunDisplay) {
        ResourceLocation animationLocation = gunDisplayPojo.getAnimation();
        if (animationLocation != null) {
            gunDisplay.gltfAnimation = ClientAssetsManager.INSTANCE.getGltfAnimation(animationLocation);
            gunDisplay.bedrockAnimation = ClientAssetsManager.INSTANCE.getBedrockAnimation(animationLocation);
        }
        if (gunDisplayPojo.getThirdPersonAnimation() != null) {
            gunDisplay.thirdPersonAnimation = gunDisplayPojo.getThirdPersonAnimation();
        }

        // State machine logic
        ResourceLocation stateMachineLocation = gunDisplayPojo.getStateMachine();
        if (stateMachineLocation == null) {
            stateMachineLocation = ResourceLocation.fromNamespaceAndPath("tacz", "default_state_machine");
        }
        if (gunDisplay.bedrockAnimation != null && gunDisplay.gunModel != null) {
            gunDisplay.animationStateMachine = LuaStateMachineFactory.create(stateMachineLocation);
        }
    }

    private static void checkModel(GunDisplayPojo gunDisplayPojo, GunDisplayInstance gunDisplay) {
        ResourceLocation modelLocation = gunDisplayPojo.getModel();
        if (modelLocation != null) {
            BedrockModelPOJO modelPOJO = ClientAssetsManager.INSTANCE.getBedrockModelPOJO(modelLocation);
            if (modelPOJO != null) {
                if (BedrockVersion.isLegacyVersion(modelPOJO) && modelPOJO.getGeometryModelLegacy() != null) {
                    GeometryModelLegacy geometryModelLegacy = modelPOJO.getGeometryModelLegacy();
                    gunDisplay.gunModel = new BedrockGunModel(modelPOJO, BedrockVersion.LEGACY);
                }
                if (BedrockVersion.isNewVersion(modelPOJO) && modelPOJO.getGeometryModelNew() != null) {
                    GeometryModelNew geometryModelNew = modelPOJO.getGeometryModelNew();
                    gunDisplay.gunModel = new BedrockGunModel(modelPOJO, BedrockVersion.NEW);
                }
            }
        }
    }

    private static void checkTexture(GunDisplayPojo gunDisplayPojo, GunDisplayInstance gunDisplay) {
        gunDisplay.modelTexture = gunDisplayPojo.getModelTexture();
        gunDisplay.hudTexture = gunDisplayPojo.getHudTexture();
        gunDisplay.slotTexture = gunDisplayPojo.getSlotTexture();
        gunDisplay.hudEmptyTexture = gunDisplayPojo.getHudEmptyTexture();
    }

    public @Nullable ResourceLocation getModelTexture() {
        return modelTexture;
    }

    public @Nullable ResourceLocation getHudTexture() {
        return hudTexture;
    }

    public @Nullable ResourceLocation getHudEmptyTexture() {
        return hudEmptyTexture;
    }

    public @Nullable ResourceLocation getSlotTexture() {
        return slotTexture;
    }

    public @Nullable String getThirdPersonAnimation() {
        return thirdPersonAnimation;
    }

    public @Nullable BedrockGunModel getGunModel() {
        return gunModel;
    }

    public @Nullable AnimationStructure getGltfAnimation() {
        return gltfAnimation;
    }

    public @Nullable BedrockAnimationFile getBedrockAnimation() {
        return bedrockAnimation;
    }

    public @Nullable LuaAnimationStateMachine getAnimationStateMachine() {
        return animationStateMachine;
    }

    public @Nullable ShellEjection getShellEjection() {
        return shellEjection;
    }

    public @Nullable AmmoParticle getParticle() {
        return particle;
    }

    public @Nullable MuzzleFlash getMuzzleFlash() {
        return muzzleFlash;
    }

    public @Nullable Map<String, ResourceLocation> getSoundMaps() {
        return soundMaps;
    }

    public void addSoundMaps(Map<String, ResourceLocation> map) {
        if(this.soundMaps != null) {
            this.soundMaps.putAll(map);
        }
    }

    public @Nullable TransformScale getTransformScale() {
        return transformScale;
    }

    public @Nullable GunTransform getTransform() {
        return transform;
    }

    public @Nullable LayerGunShow getOffhandShow() {
        return offhandShow;
    }

    public @Nullable Int2ObjectArrayMap<LayerGunShow> getHotbarShow() {
        return hotbarShow;
    }

    public float getIronZoom() {
        return ironZoom;
    }

    public float getZoomModelFov() {
        return zoomModelFov;
    }

    public boolean isShowCrosshair() {
        return showCrosshair;
    }

    public @Nullable ResourceLocation getPlayerAnimator3rd() {
        return playerAnimator3rd;
    }

    public boolean is3rdFixedHand() {
        return is3rdFixedHand;
    }

    public EnumMap<FireMode, ControllableData> getControllableData() {
        return controllableData;
    }

    public AmmoCountStyle getAmmoCountStyle() {
        return ammoCountStyle;
    }

    public DamageStyle getDamageStyle() {
        return damageStyle;
    }

    public @Nullable LaserConfig getLaserConfig() {
        return laserConfig;
    }

    public @Nullable TextShow getTextShow() {
        return textShow;
    }

    public float @Nullable [] getTracerColor() {
        return tracerColor;
    }

    public @Nullable LuaTable getStateMachineParam() {
        return stateMachineParam;
    }

    public ResourceLocation getSounds(String name) {
        if (soundMaps == null) {
            return null;
        }
        return soundMaps.get(name);
    }

    public void setKerning() {
        // Implementation for setting kerning if needed, currently empty placeholder
    }

    public void setTexture() {
        if (soundMaps == null) {
            soundMaps = Maps.newHashMap();
        }
        soundMaps.putIfAbsent(SoundManager.DRY_FIRE_SOUND, ResourceLocation.fromNamespaceAndPath(GunModFabric.MOD_ID, SoundManager.DRY_FIRE_SOUND));
        soundMaps.putIfAbsent(SoundManager.FIRE_SELECT, ResourceLocation.fromNamespaceAndPath(GunModFabric.MOD_ID, SoundManager.FIRE_SELECT));
        soundMaps.putIfAbsent(SoundManager.HEAD_HIT_SOUND, ResourceLocation.fromNamespaceAndPath(GunModFabric.MOD_ID, SoundManager.HEAD_HIT_SOUND));
        soundMaps.putIfAbsent(SoundManager.FLESH_HIT_SOUND, ResourceLocation.fromNamespaceAndPath(GunModFabric.MOD_ID, SoundManager.FLESH_HIT_SOUND));
        soundMaps.putIfAbsent(SoundManager.KILL_SOUND, ResourceLocation.fromNamespaceAndPath(GunModFabric.MOD_ID, SoundManager.KILL_SOUND));
        soundMaps.putIfAbsent(SoundManager.MELEE_BAYONET, ResourceLocation.fromNamespaceAndPath(GunModFabric.MOD_ID, "melee_bayonet/melee_bayonet_01"));
        soundMaps.putIfAbsent(SoundManager.MELEE_STOCK, ResourceLocation.fromNamespaceAndPath(GunModFabric.MOD_ID, "melee_stock/melee_stock_01"));
        soundMaps.putIfAbsent(SoundManager.MELEE_PUSH, ResourceLocation.fromNamespaceAndPath(GunModFabric.MOD_ID, "melee_stock/melee_stock_02"));
    }
}