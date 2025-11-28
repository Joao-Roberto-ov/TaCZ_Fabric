package com.tacz.guns.api.client.event;

import com.mojang.blaze3d.vertex.PoseStack;

// No Fabric, eventos são callbacks, não classes que estendem Event.
// Vamos manter a classe como um objeto de dados por enquanto.
public class BeforeRenderHandEvent {
    private final PoseStack poseStack;

    public BeforeRenderHandEvent(PoseStack poseStack) {
        this.poseStack = poseStack;
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }
}