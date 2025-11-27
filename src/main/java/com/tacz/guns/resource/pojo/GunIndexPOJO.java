package com.tacz.guns.resource.pojo;

import com.google.gson.annotations.SerializedName;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class GunIndexPOJO {
    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("data")
    private ResourceLocation data;

    @SerializedName("sort")
    private int sort;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public ResourceLocation getData() {
        return data;
    }

    public int getSort() {
        return sort;
    }
}