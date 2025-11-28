package com.tacz.guns;

import com.tacz.guns.init.ModBlocks;
import com.tacz.guns.init.ModCreativeTabs;
import com.tacz.guns.init.ModItems;
import com.tacz.guns.init.ModSounds;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GunMod implements ModInitializer {
    public static final String MOD_ID = "tacz";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Loading Timeless and Classics Zero (Fabric)...");

        // 1. Registrar Sons (Sempre primeiro)
        ModSounds.init();

        // 2. Registrar Blocos
        ModBlocks.init();

        // 3. Registrar Itens
        ModItems.init();

        // 4. Registrar Abas Criativas
        ModCreativeTabs.init();

        // TODO: Registrar Configurações e Pack Loaders depois
    }
}