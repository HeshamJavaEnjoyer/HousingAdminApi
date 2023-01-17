package org.school.housing.models.special_respond;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PasswordChangeResponse {

    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("message")
    @Expose
    public String message;

}
