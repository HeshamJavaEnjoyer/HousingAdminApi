package org.school.housing.api.controllers;


import android.util.Log;

import androidx.annotation.NonNull;
import org.school.housing.api.controllers.manger.ApiBaseController;
import org.school.housing.api.controllers.manger.ApiController;
import org.school.housing.interfaces.ProcessCallback;
import org.school.housing.models.BaseResponse;
import org.school.housing.models.admin.Employee;
import java.util.HashMap;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmpApiController extends ApiBaseController {
    private static final String TAG = "EmpApiController";
    private final ApiController apiController;

    //Singleton
    private static EmpApiController Instance;
    public EmpApiController() {
        apiController = ApiController.getInstance();
    }
    public static synchronized EmpApiController getInstance() {
        if (Instance == null) {
            Instance = new EmpApiController();
        }
        return Instance;
    }

    //C-R-UD Rx
    public void createEmployeeMap(String name, String mobile, String national_number, byte[] imagePart, ProcessCallback processCallback) {
        /*-
        RequestBody r_name = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody r_mobile = RequestBody.create(MediaType.parse("multipart/form-data"), mobile);
        RequestBody r_national_number = RequestBody.create(MediaType.parse("multipart/form-data"), national_number);
         */
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imagePart);
        MultipartBody.Part file = MultipartBody.Part.createFormData("image", "image-file", requestBody);

        HashMap<String, RequestBody> map = new HashMap<>();
        String[] values = {name, mobile, national_number};
        String[] keys = {"name","mobile", "national_number"};
        for (int i = 0; i <= values.length - 1; i++) {
            map.put(keys[i], RequestBody.create(MediaType.parse("text/plain"), values[i]));
        }

        Call<BaseResponse<Employee>> call = apiController.getRetrofitRequests().store_emp_map(map, file);
        call.enqueue(new Callback<BaseResponse<Employee>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Employee>> call, @NonNull Response<BaseResponse<Employee>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processCallback.onSuccess(response.body().message); //the( var => list ) refer to whats in The BaseResponse
                } else {
                    generalErrorMessage(response,processCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Employee>> call, @NonNull Throwable throwable) {
                processCallback.onFailure("Error in the server 500");
            }
        });
    }

    public void  updateEmployeeMap(int id , String name,String mobile,String national_number ,byte[] imagePart ,ProcessCallback processCallback){
        Log.d(TAG, "updateEmployeeMap() returned: Id =" + id +" name = " + name);
        /*-
        RequestBody r_name = RequestBody.create(MediaType.parse("multipart/form-data"),name);
        RequestBody r_mobile = RequestBody.create(MediaType.parse("multipart/form-data"),mobile);
        RequestBody r_national_number = RequestBody.create(MediaType.parse("multipart/form-data"),national_number);
*/

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imagePart);
        MultipartBody.Part file = MultipartBody.Part.createFormData("image", "image-file", requestBody);

        HashMap<String, RequestBody> map = new HashMap<>();
        String[] values = {name, mobile, national_number};
        String[] keys = {"name", "mobile", "national_number"};
        for (int i = 0; i <= values.length - 1; i++) {
            map.put(keys[i], RequestBody.create(MediaType.parse("text/plain"), values[i]));
        }
        //For update purpose TODO
        map.put("_method",RequestBody.create(MediaType.parse("text/plain"),"PUT"));

        Call<BaseResponse<Employee>> call = apiController.getRetrofitRequests().update_emp_map(id,map,file);
        call.enqueue(new Callback<BaseResponse<Employee>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Employee>> call, @NonNull Response<BaseResponse<Employee>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processCallback.onSuccess("updated successfully =>"+response.body().message);
                } else {
                    generalErrorMessage(response,processCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Employee>> call, @NonNull Throwable throwable) {
                processCallback.onFailure("Error in the server 500");
            }
        });
    }

    public void  deleteEmployee(int id , ProcessCallback processCallback){
        Call<BaseResponse<Employee>> call = apiController.getRetrofitRequests().delete_emp(id);
        call.enqueue(new Callback<BaseResponse<Employee>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Employee>> call, @NonNull Response<BaseResponse<Employee>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processCallback.onSuccess("deleted successfully =>"+response.body().message);
                } else {
                    generalErrorMessage(response,processCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Employee>> call, @NonNull Throwable throwable) {
                processCallback.onFailure("Error in the server 500");
            }
        });
    }


}
/*-
    public void  createEmployee(String name,String mobile,String national_number ,byte[] imagePart ,ProcessCallback processCallback){
        RequestBody r_name = RequestBody.create(MediaType.parse("multipart/form-data"),name);
        RequestBody r_mobile = RequestBody.create(MediaType.parse("multipart/form-data"),mobile);
        RequestBody r_national_number = RequestBody.create(MediaType.parse("multipart/form-data"),national_number);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imagePart);
        MultipartBody.Part file = MultipartBody.Part.createFormData("image", "image-file", requestBody);

        Call<BaseResponse<Employee>> call = apiController.getRetrofitRequests().store_emp(r_name,r_mobile,r_national_number,file);
        call.enqueue(new Callback<BaseResponse<Employee>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Employee>> call, @NonNull Response<BaseResponse<Employee>> response) {
                if (response.code() == 200 && response.body() != null) {
                    processCallback.onSuccess(response.body().message); //the( var => list ) refer to whats in The BaseResponse
                }else {
                    processCallback.onFailure("Failed to create A Employee");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Employee>> call, @NonNull Throwable throwable) {
                processCallback.onFailure("Error in the server 500");
            }
        });
    }
*/
/*-
    public void  updateEmployee(String name,String mobile,String national_number ,byte[] imagePart ,ProcessCallback processCallback){
        RequestBody r_name = RequestBody.create(MediaType.parse("multipart/form-data"),name);
        RequestBody r_mobile = RequestBody.create(MediaType.parse("multipart/form-data"),mobile);
        RequestBody r_national_number = RequestBody.create(MediaType.parse("multipart/form-data"),national_number);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imagePart);
        MultipartBody.Part file = MultipartBody.Part.createFormData("image", "image-file", requestBody);

        Call<BaseResponse<Employee>> call = apiController.getRetrofitRequests().update_emp(r_name,r_mobile,r_national_number,file);
        call.enqueue(new Callback<BaseResponse<Employee>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Employee>> call, @NonNull Response<BaseResponse<Employee>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processCallback.onSuccess("updated successfully =>"+response.body().message);
                } else {
                    processCallback.onFailure("failed to update Emp!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Employee>> call, @NonNull Throwable throwable) {
                processCallback.onFailure("Error in the server 500");
            }
        });
    }
*/