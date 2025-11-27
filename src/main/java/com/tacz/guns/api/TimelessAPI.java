package com.tacz.guns.api;
import com.tacz.guns.client.resource.index.ClientAttachmentIndex;
import com.tacz.guns.client.resource.index.ClientGunIndex;
import com.tacz.guns.client.resource.index.ClientGunIndex;
import com.tacz.guns.resource.index.CommonAttachmentIndex;
import com.tacz.guns.resource.index.CommonGunIndex;
import com.tacz.guns.resource.manager.CommonDataManager;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class TimelessAPI {
    private TimelessAPI() {}

    /**
     * Obtém o índice comum de dados da arma (dados do servidor/comuns).
     */
    public static Optional<CommonGunIndex> getCommonGunIndex(ResourceLocation gunId) {
        return CommonDataManager.getGunIndex(gunId);
    }

    /**
     * Obtém o índice comum de dados de acessórios.
     */
    public static Optional<CommonAttachmentIndex> getCommonAttachmentIndex(ResourceLocation attachmentId) {
        return CommonDataManager.getAttachmentIndex(attachmentId);
    }

    /**
     * Lado do Cliente: Obtém o índice de exibição da arma (modelos, texturas, sons).
     */
    @Environment(EnvType.CLIENT)
    public static Optional<ClientGunIndex> getClientGunIndex(ResourceLocation gunId) {
        // Nota: Precisaremos implementar o ClientAssetManager mais tarde.
        // Por enquanto, retornamos vazio para não quebrar a compilação do servidor.
        // return ClientAssetManager.INSTANCE.getGunIndex(gunId);
        return Optional.empty(); // Placeholder temporário
    }

    /**
     * Lado do Cliente: Obtém o índice de exibição do anexo.
     */
    @Environment(EnvType.CLIENT)
    public static Optional<ClientAttachmentIndex> getClientAttachmentIndex(ResourceLocation attachmentId) {
        // return ClientAssetManager.INSTANCE.getAttachmentIndex(attachmentId);
        return Optional.empty(); // Placeholder temporário
    }

    // Método auxiliar para pegar GunData direto do ID
    public static @Nullable GunData getGunData(ResourceLocation gunId) {
        return getCommonGunIndex(gunId).map(CommonGunIndex::getGunData).orElse(null);
    }
}