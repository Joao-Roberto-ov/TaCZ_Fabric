package com.tacz.guns;

import com.tacz.guns.init.ModCreativeTabs;
import com.tacz.guns.init.ModDataComponents;
import com.tacz.guns.init.ModItems;
import com.tacz.guns.init.ModResourceLoaders;
import com.tacz.guns.resource.modifier.AttachmentPropertyManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GunModFabric implements ModInitializer {
    public static final String MOD_ID = "tacz";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Constante para o pacote de armas padrão, conforme o original
    public static final String DEFAULT_GUN_PACK_NAME = "tacz_default_gun";

    @Override
    public void onInitialize() {
        LOGGER.info("Inicializando Timeless & Classics Guns: Zero (Fabric 1.21.1)");

        ModDataComponents.init();
        ModResourceLoaders.init();
        ModItems.init();
        ModCreativeTabs.init();
        AttachmentPropertyManager.registerModifier(); // Registra os modificadores que você criou

        registerDefaultExtraGunPack();
        // Registra os componentes que criamos antes
        // 1. Inicialização de Configurações
        // No Forge era: ModLoadingContext...registerConfig
        // No Fabric, usaremos o Cloth Config ou um sistema próprio mais tarde.
        // Por enquanto, vamos apenas logar.
        LOGGER.info("Carregando configurações...");
        // CommonConfig.init(); // Implementaremos isso depois

        // 2. Registro de Itens, Blocos e Entidades
        // No Forge, isso usava DeferredRegister. No Fabric, chamaremos classes estáticas.
        // ModBlocks.register();
        // ModItems.register();
        // ModEntities.register();
        // ModSounds.register();

        // 3. Registro de Recursos (Data Packs)
        registerDefaultExtraGunPack();
    }

    private void registerDefaultExtraGunPack() {
        // Lógica adaptada do original para registrar o resource pack embutido
        String jarDefaultPackPath = String.format("/assets/%s/custom/%s", MOD_ID, DEFAULT_GUN_PACK_NAME);
        LOGGER.info("Registrando pacote de armas padrão em: " + jarDefaultPackPath);
        // ResourceManager.registerExportResource(...) // Implementaremos a classe ResourceManager depois
    }
}