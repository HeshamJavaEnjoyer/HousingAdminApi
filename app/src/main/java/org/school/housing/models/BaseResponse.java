package org.school.housing.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseResponse<Model> {

    @SerializedName("status")
    @Expose
    public Boolean status;

    @SerializedName("message")
    @Expose
    public String message;

    //this what is going to hold the array that comes as a response
    //BUT ONLY WITH THE {'list'} JSON KEY
    //So if you want to receive any BaseResponse *(status / massage / dataArray)*
        // Use The Keys in the Json and make a variable for it
    @SerializedName("list")
    @Expose
    public List<Model> list = null;//USER AND CATEGORY AND EMPLOYEE so far----HAS THE SAME KEY (list)


    //** Stop
    @SerializedName("data")
    @Expose
    public Model data;

    @SerializedName("object")
    @Expose
    public Model object;

}
