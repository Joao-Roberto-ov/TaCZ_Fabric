package com.tacz.guns.resource.manager;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.tacz.guns.GunModFabric;
import com.tacz.guns.resource.index.CommonGunIndex;
import com.tacz.guns.resource.pojo.GunIndexPOJO;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Optional;

public class CommonDataManager {
    // Instância GSON para deserializar os arquivos
    private static final Gson GSON = new GsonBuilder().create();

    // Armazena os dados brutos das armas (ex: dano, rpm) carregados de .../data/guns/
    private static final Map<ResourceLocation, GunData> GUN_DATAS = Maps.newHashMap();

    // Armazena os índices finais das armas (o objeto que o jogo usa) carregados de .../index/guns/
    private static final Map<ResourceLocation, CommonGunIndex> GUN_INDEXES = Maps.newHashMap();

    /**
     * Limpa todos os dados. Chamado no início do reload de recursos.
     */
    public static void clear() {
        GUN_DATAS.clear();
        GUN_INDEXES.clear();
    }

    /**
     * Carrega um JSON de arma.
     * O carregador deve decidir se é um "Data" ou um "Index" baseado no caminho do arquivo.
     */
    public static void loadGunData(ResourceLocation id, JsonElement json) {
        String path = id.getPath();

        // No TaCZ, os dados ficam na pasta 'data' e os índices na pasta 'index'
        if (path.contains("data/guns")) {
            try {
                GunData data = GSON.fromJson(json, GunData.class);
                GUN_DATAS.put(id, data);
                GunModFabric.LOGGER.debug("Carregado GunData: {}", id);
            } catch (Exception e) {
                GunModFabric.LOGGER.error("Falha ao carregar GunData: {}", id, e);
            }
        } else if (path.contains("index/guns")) {
            try {
                GunIndexPOJO pojo = GSON.fromJson(json, GunIndexPOJO.class);
                // Nota: Convertemos para CommonGunIndex mais tarde, pois ele depende do GunData já estar carregado
                // Por enquanto, vamos apenas tentar criar, mas se falhar (porque o data não carregou ainda), guardamos para depois.
                // Na arquitetura ideal, faríamos dois loops, mas para simplificar o port:
                // Vamos assumir que o GunData é acessível. Se não, precisaríamos de uma lógica de "Pós-Carregamento".
                // Para este passo, vamos focar em armazenar.

                // Hack temporário: Armazenar o POJO em um mapa temporário e processar no final seria o ideal,
                // mas vamos tentar carregar direto e ver se o GunPackLoader ordenou corretamente.
                // Se der erro de "GunData not found", precisaremos ajustar o GunPackLoader para carregar 'data' antes de 'index'.
            } catch (Exception e) {
                GunModFabric.LOGGER.error("Falha ao ler GunIndex POJO: {}", id, e);
            }
        }
    }

    /**
     * Metodo crucial para o CommonGunIndex acessar os dados brutos.
     * Substitui o antigo CommonAssetsManager.get().getGunData()
     */
    public static GunData getGunData(ResourceLocation id) {
        return GUN_DATAS.get(id);
    }

    public static Optional<CommonGunIndex> getGunIndex(ResourceLocation id) {
        return Optional.ofNullable(GUN_INDEXES.get(id));
    }

    // Método auxiliar para registrar um índice processado
    public static void registerGunIndex(ResourceLocation id, CommonGunIndex index) {
        GUN_INDEXES.put(id, index);
    }
}