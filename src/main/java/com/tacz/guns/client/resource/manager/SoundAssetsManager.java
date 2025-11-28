package com.tacz.guns.client.resource.manager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.Map;

public class SoundAssetsManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();

    public SoundAssetsManager() {
        super(GSON, "tacz_sounds");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        // Lógica desativada temporariamente para permitir que o jogo abra.
        // Futuramente, reimplementaremos o carregamento de sons do Fabric.
    }
}