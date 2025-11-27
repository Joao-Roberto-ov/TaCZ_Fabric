package com.tacz.guns.api.modifier;

import com.tacz.guns.resource.modifier.AttachmentCacheProperty;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * Interface para modificadores de atributos de acessórios.
 *
 * @param <T> Tipo de dado processado após leitura do JSON
 * @param <K> Tipo do valor armazenado no cache
 */
public interface IAttachmentModifier<T, K> {
    /**
     * ID do modificador, usado também como nome do campo no JSON.
     *
     * @return Nome do campo no JSON
     */
    String getId();

    /**
     * Campos opcionais para compatibilidade com versões antigas do JSON.
     *
     * @return Nome do campo antigo no JSON ou string vazia
     */
    default String getOptionalFields() {
        return StringUtils.EMPTY;
    }

    /**
     * Lê os dados do JSON.
     *
     * @param json String JSON de entrada
     * @return Objeto JsonProperty com os dados processados
     */
    JsonProperty<T> readJson(String json);

    /**
     * Inicializa o cache com os dados padrão da arma.
     *
     * @param gunItem ItemStack da arma atual
     * @param gunData Dados da arma (GunData)
     * @return Valor inicial do cache
     */
    CacheValue<K> initCache(ItemStack gunItem, GunData gunData);

    /**
     * Calcula o valor final combinando os modificadores dos acessórios com o valor base.
     *
     * @param modifiedValues Lista de valores vindos dos acessórios
     * @param cache          Valor base da arma (será modificado com o resultado)
     */
    void eval(List<T> modifiedValues, CacheValue<K> cache);

    /**
     * Obtém dados para desenhar os gráficos de barras na tela de modificação (Refit Screen).
     */
    @Environment(EnvType.CLIENT)
    default List<DiagramsData> getPropertyDiagramsData(ItemStack gunItem, GunData gunData, AttachmentCacheProperty cacheProperty) {
        return Collections.emptyList();
    }

    /**
     * Obtém a quantidade de barras de atributos para calcular o offset dos botões na interface.
     */
    @Environment(EnvType.CLIENT)
    default int getDiagramsDataSize() {
        return 0;
    }

    /**
     * Dados para renderização das barras de atributos.
     *
     * @param defaultPercent   Porcentagem do valor padrão
     * @param modifierPercent  Porcentagem do valor modificado
     * @param modifier         O valor da modificação (numérico)
     * @param titleKey         Chave de tradução do título
     * @param positivelyString Texto mostrado quando o valor é maior que o padrão (Buff)
     * @param negativeString   Texto mostrado quando o valor é menor que o padrão (Debuff)
     * @param defaultString    Texto mostrado quando é igual
     * @param positivelyBetter Se true, aumento é verde (bom). Se false, aumento é vermelho (ruim).
     */
    @Environment(EnvType.CLIENT)
    record DiagramsData(double defaultPercent, double modifierPercent, Number modifier,
                        String titleKey, String positivelyString,
                        String negativeString, String defaultString,
                        boolean positivelyBetter) {
    }
}