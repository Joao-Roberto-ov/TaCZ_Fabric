package com.tacz.guns.init;

import com.tacz.guns.GunModFabric;
import com.tacz.guns.resource.GunPackLoader;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public class ModResourceLoaders {

    public static void init() {
        GunModFabric.LOGGER.info("Registrando carregadores de recursos do TaCZ...");

        // Registra o carregador de Gun Packs (Dados do Servidor/Comuns)
        // Isso carrega os JSONs de armas da pasta 'data'
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new GunPackLoader());

        // Se houver loaders apenas de cliente (texturas/modelos), registre aqui usando PackType.CLIENT_RESOURCES
        // Exemplo:
        // ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new ClientGunPackLoader());
    }
}