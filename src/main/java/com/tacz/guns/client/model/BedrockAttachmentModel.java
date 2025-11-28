package com.tacz.guns.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tacz.guns.client.model.bedrock.BedrockPart;
import com.tacz.guns.client.resource.pojo.model.BedrockModelPOJO;
import com.tacz.guns.client.resource.pojo.model.BedrockVersion;

public class BedrockAttachmentModel extends BedrockGunModel {
    public BedrockAttachmentModel(BedrockModelPOJO pojo, BedrockVersion version) {
        super(pojo, version);
    }

    @Override
    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, int color) {
        if (modelMap == null) return;

        // Removida lógica condicional de Oculus/Iris.
        // Renderização padrão:
        for (BedrockPart part : modelMap.values()) {
            part.render(poseStack, consumer, light, overlay, color);
        }
    }
}