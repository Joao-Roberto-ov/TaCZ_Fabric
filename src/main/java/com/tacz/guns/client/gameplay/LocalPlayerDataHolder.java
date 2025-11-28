package com.tacz.guns.client.gameplay;

import net.minecraft.client.player.LocalPlayer;

public class LocalPlayerDataHolder {
    // Dados de estado do cliente
    public long clientShootTimestamp = -1L;
    public long clientLastShootTimestamp = -1L;
    public float clientAimingProgress = 0.0f;

    // Adicione outros campos conforme necessário pelo IClientPlayerGunOperator

    private final LocalPlayer player;

    public LocalPlayerDataHolder(LocalPlayer player) {
        this.player = player;
    }

    // Métodos stub
    public void tick() {}
    public void reset() {}
}