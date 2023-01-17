package org.school.housing.models.admin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Employee implements Serializable {
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("national_number")
    @Expose
    public String nationalNumber;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("image_url")
    @Expose
    public String imageUrl;
    @SerializedName("tower_name")
    @Expose
    public String towerName;

}

/*
public class Employee {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("national_number")
    @Expose
    public String nationalNumber;
    @SerializedName("active")
    @Expose
    public Boolean active;
    @SerializedName("image_url")
    @Expose
    public String imageUrl;

}
*/