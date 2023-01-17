package org.school.housing.models.special_respond;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthResponse<ModelAsAuth> {

    @SerializedName("status")
    @Expose
    public Boolean status;

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("data")
    @Expose
    public ModelAsAuth object;

}