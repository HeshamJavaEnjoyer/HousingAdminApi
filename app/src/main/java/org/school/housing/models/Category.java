package org.school.housing.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("actions_count")
    @Expose
    public String actionsCount;
    //it was integer but i turn it to double
    @SerializedName("total")
    @Expose
    public Double total;

}
