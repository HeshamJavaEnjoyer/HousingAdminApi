package org.school.housing.api.controllers.manger;

import org.school.housing.api.RetrofitRequests;
import org.school.housing.api.RetrofitSettings;

//THIS CLASS IS FOR connecting between the RetrofitRequests Interface AND RetrofitSettings Class
public class ApiController {
    //The Interface Instance
    private final RetrofitRequests retrofitRequests;
    //The Class Instance For The Singleton
    private static ApiController Instance;

    //this will run once ..At the time we start using the single tone object
    public ApiController() {
        retrofitRequests = RetrofitSettings.getInstance().create(RetrofitRequests.class);
    }

    public static synchronized ApiController getInstance(){
        if (Instance == null) {
            Instance = new ApiController();
        }
        return Instance;
    }

    public RetrofitRequests getRetrofitRequests() {
        return retrofitRequests;
    }

}
