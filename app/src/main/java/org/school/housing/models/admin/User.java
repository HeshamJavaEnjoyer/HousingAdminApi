package org.school.housing.models.admin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("national_number")
        @Expose
        public String nationalNumber;
        @SerializedName("family_members")
        @Expose
        public String familyMembers;
        @SerializedName("gender")
        @Expose
        public String gender;
        @SerializedName("admin_id")
        @Expose
        public Integer adminId;
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("image_url")
        @Expose
        public String imageUrl;

        //me
        public byte[] image_bytes;

}