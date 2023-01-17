package org.school.housing.api.controllers;

import androidx.annotation.NonNull;

import org.school.housing.api.controllers.manger.ApiBaseController;
import org.school.housing.api.controllers.manger.ApiController;
import org.school.housing.interfaces.ProcessCallback;
import org.school.housing.models.BaseResponse;
import org.school.housing.models.admin.Advertisement;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvApiController extends ApiBaseController {
    private final ApiController apiController;

    //Singleton
    private static AdvApiController Instance;

    public AdvApiController() {
        apiController = ApiController.getInstance();
    }

    public static synchronized AdvApiController getInstance() {
        if (Instance == null) {
            Instance = new AdvApiController();
        }
        return Instance;
    }

    public void storeAdv(String title, String info, byte[] imagePart, ProcessCallback processCallback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imagePart);
        MultipartBody.Part file = MultipartBody.Part.createFormData("image", "image-file", requestBody);

        Call<BaseResponse<Advertisement>> call = apiController.getRetrofitRequests().store_advertisements(title,info,file);
        call.enqueue(new Callback<BaseResponse<Advertisement>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Advertisement>> call, @NonNull Response<BaseResponse<Advertisement>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processCallback.onSuccess("stored successfully =>"+response.body().message);
                } else {
                    generalErrorMessage(response,processCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Advertisement>> call, @NonNull Throwable throwable) {
                processCallback.onFailure("ServerError 500");
            }
        });
    }

    public void storeAdv_noImage(String title, String info, ProcessCallback processCallback) {
        Call<BaseResponse<Advertisement>> call = apiController.getRetrofitRequests().store_advertisements_noImage(title,info);
        call.enqueue(new Callback<BaseResponse<Advertisement>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Advertisement>> call, @NonNull Response<BaseResponse<Advertisement>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processCallback.onSuccess("stored successfully =>"+response.body().message);
                } else {
                    generalErrorMessage(response,processCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Advertisement>> call, @NonNull Throwable throwable) {
                processCallback.onFailure("ServerError 500");
            }
        });
    }

    public void updateAdv(int id ,String newTitle, String newInfo, byte[] newImagePart,ProcessCallback processCallback){
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), newImagePart);
        MultipartBody.Part file = MultipartBody.Part.createFormData("image", "image-file", requestBody);

        Call<BaseResponse<Advertisement>> call = apiController.getRetrofitRequests().update_advertisements(id,newTitle,newInfo,file);
        call.enqueue(new Callback<BaseResponse<Advertisement>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Advertisement>> call, @NonNull Response<BaseResponse<Advertisement>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processCallback.onSuccess("updated a Adv successfully =>"+response.body().message);
                } else {
                    generalErrorMessage(response,processCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Advertisement>> call, @NonNull Throwable throwable) {
                processCallback.onFailure("ServerError 500");
            }
        });
    }

    public void updateAdv_noImage(int id ,String newTitle, String newInfo,ProcessCallback processCallback){
        Call<BaseResponse<Advertisement>> call = apiController.getRetrofitRequests().update_advertisements_noImage(id,newTitle,newInfo);
        call.enqueue(new Callback<BaseResponse<Advertisement>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Advertisement>> call, @NonNull Response<BaseResponse<Advertisement>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processCallback.onSuccess("updated a Adv successfully =>"+response.body().message);
                } else {
                    generalErrorMessage(response,processCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Advertisement>> call, @NonNull Throwable throwable) {
                processCallback.onFailure("ServerError 500");
            }
        });
    }

    public void deleteAdv(int id,ProcessCallback processCallback){
        Call<BaseResponse<Advertisement>> call = apiController.getRetrofitRequests().delete_advertisements(id);
        call.enqueue(new Callback<BaseResponse<Advertisement>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Advertisement>> call, @NonNull Response<BaseResponse<Advertisement>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processCallback.onSuccess("deleted Adv successfully =>"+response.body().message);
                } else {
                    generalErrorMessage(response,processCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Advertisement>> call, @NonNull Throwable throwable) {
                processCallback.onFailure("ServerError 500");
            }
        });
    }

}
