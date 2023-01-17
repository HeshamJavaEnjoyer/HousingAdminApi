package org.school.housing.models.admin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Advertisement {
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("info")
    @Expose
    public String info;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("image_url")
    @Expose
    public Object imageUrl;
    @SerializedName("tower_name")
    @Expose
    public String towerName;

}