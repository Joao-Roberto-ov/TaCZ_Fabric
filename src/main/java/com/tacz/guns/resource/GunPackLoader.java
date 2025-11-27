package com.tacz.guns.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.tacz.guns.GunModFabric;
import com.tacz.guns.resource.manager.CommonDataManager;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class GunPackLoader extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
    public static final Gson GSON = new GsonBuilder().create();

    // ID único para este listener no Fabric
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(GunModFabric.MOD_ID, "gun_pack_loader");

    public GunPackLoader() {
        super(GSON, "tacz/guns"); // Vai ler arquivos em data/namespace/tacz/guns/...
    }

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        GunModFabric.LOGGER.info("Carregando pacotes de armas (TaCZ)...");
        CommonDataManager.clear();

        // Passo 1: Carregar todas as pastas "data" (Estatísticas)
        object.forEach((location, json) -> {
            if (location.getPath().contains("data/guns")) {
                CommonDataManager.loadGunData(location, json);
            }
        });

        // Passo 2: Carregar todas as pastas "index" (Definições que usam as estatísticas)
        object.forEach((location, json) -> {
            if (location.getPath().contains("index/guns")) {
                try {
                    // Convertemos o JSON para o POJO
                    com.tacz.guns.resource.pojo.GunIndexPOJO pojo = GSON.fromJson(json, com.tacz.guns.resource.pojo.GunIndexPOJO.class);

                    // Criamos o índice (isso vai buscar o GunData carregado no Passo 1)
                    com.tacz.guns.resource.index.CommonGunIndex index = com.tacz.guns.resource.index.CommonGunIndex.getInstance(pojo);

                    // Registramos
                    CommonDataManager.registerGunIndex(location, index);
                } catch (Exception e) {
                    GunModFabric.LOGGER.error("Erro ao processar índice da arma: {}", location, e);
                }
            }
        });

        GunModFabric.LOGGER.info("Carregamento de armas concluído.");
    }
}