package com.tacz.guns.api.client.event;



/**
 * 玩家交换主副手物品时触发该事件
 */
public class SwapItemWithOffHand {
    public SwapItemWithOffHand() {
        postClientEventToKubeJS(this);
    }
}
