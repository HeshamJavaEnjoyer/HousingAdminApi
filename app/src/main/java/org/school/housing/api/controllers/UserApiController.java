package org.school.housing.api.controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import org.school.housing.api.controllers.manger.ApiBaseController;
import org.school.housing.api.controllers.manger.ApiController;
import org.school.housing.interfaces.ProcessCallback;
import org.school.housing.models.BaseResponse;
import org.school.housing.models.admin.User;
import java.util.HashMap;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserApiController extends ApiBaseController {
    private static final String TAG = "UserApiController";
    //let's create singleton

    private static UserApiController Instance;

    public UserApiController() {
    }

    public static synchronized UserApiController getInstance() {
        if (Instance == null) {
            Instance = new UserApiController();
        }
        return Instance;
    }


    //CREATE
    public void create_user_map(String name, String email, String mobile, String national_number, String family_members, String gender, byte[] imagePart, ProcessCallback processCallback) {
        Log.d(TAG, "INSIDE create_user_map() returned: " + name + " " + email + " " + mobile + " " + national_number + " " + family_members + " " + gender + " " + imagePart.length);

        RequestBody imageToRequestBody = RequestBody.create(MediaType.parse("image/*"), imagePart);
        MultipartBody.Part file = MultipartBody.Part.createFormData("image", "image-file", imageToRequestBody);

        HashMap<String, RequestBody> map = new HashMap<>();
        String[] values = {name, email, mobile, national_number, family_members, gender};
        String[] keys = {"name", "email", "mobile", "national_number", "family_members", "gender"};
        for (int i = 0; i <= values.length - 1; i++) {
            map.put(keys[i], RequestBody.create(MediaType.parse("text/plain"), values[i]));
        }

        Call<BaseResponse<User>> call = ApiController.getInstance().getRetrofitRequests().create_user_map(map, file);
        call.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<User>> call, @NonNull Response<BaseResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "onResponse() returned: " + true);
                    processCallback.onSuccess("user created successfully " + response.body().message);
                } else {
                    generalErrorMessage(response, processCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<User>> call, @NonNull Throwable throwable) {
                processCallback.onFailure("something went wrong!");
            }
        });
    }
    //UPDATE
    public void update_user_map(int id, String name, String email, String mobile, String national_number, String family_members, String gender, byte[] imagePart, ProcessCallback processCallback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imagePart);
        MultipartBody.Part file = MultipartBody.Part.createFormData("image", "image-file", requestBody);

        HashMap<String, RequestBody> map = new HashMap<>();
        String[] values = {name, email, mobile, national_number, family_members, gender};
        String[] keys = {"name", "email", "mobile", "national_number", "family_members", "gender"};
        for (int i = 0; i <= values.length - 1; i++) {
            map.put(keys[i], RequestBody.create(MediaType.parse("text/plain"), values[i]));
        }

        Call<BaseResponse<User>> call = ApiController.getInstance().getRetrofitRequests().update_user_Map(id, map, file);
        call.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<User>> call, @NonNull Response<BaseResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processCallback.onSuccess("updated successfully =>" + response.body().message);
                    Log.i(TAG, "onResponse: updated successfully");
                } else {
                    generalErrorMessage(response, processCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<User>> call, @NonNull Throwable throwable) {
                processCallback.onFailure("something went wrong!");
            }
        });
    }
    public void update_user_no_pic_map(int id, String name, String email, String mobile, String national_number, String family_members, String gender, ProcessCallback processCallback) {
        HashMap<String, RequestBody> map = new HashMap<>();
        String[] values = {name, email, mobile, national_number, family_members, gender};
        String[] keys = {"name", "email", "mobile", "national_number", "family_members", "gender"};
        for (int i = 0; i <= values.length - 1; i++) {
            map.put(keys[i], RequestBody.create(MediaType.parse("text/plain"), values[i]));
        }

        Call<BaseResponse<User>> call = ApiController.getInstance().getRetrofitRequests().update_user_no_pic_Map(id,map);
        call.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<User>> call, @NonNull Response<BaseResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processCallback.onSuccess("updated successfully =>" + response.body().message);
                    Log.i(TAG, "onResponse: updated successfully");
                } else {
                    generalErrorMessage(response,processCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<User>> call, @NonNull Throwable throwable) {
                processCallback.onFailure("something went wrong!");
            }
        });
    }
    //Delete
    public void delete_user(int id, ProcessCallback callback) {
        Call<BaseResponse<User>> call = ApiController.getInstance().getRetrofitRequests().DELETE_USER_CALL(id);
        call.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<User>> call, @NonNull Response<BaseResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess("user deleted successfully =>" + response.body().message);
                } else {
                    generalErrorMessage(response,callback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<User>> call, @NonNull Throwable throwable) {
                callback.onFailure("something went wrong!");
            }
        });
    }
}

    /*88888888888888888888888
    //My Message Method
    private void generalErrorMessage(@NonNull Response<?> response, ProcessCallback processCallback) {
        Log.i(TAG, "generalErrorMessage: something went wrong!");
        try {
            if (response.errorBody() != null) {
                String errorString = new String(response.errorBody().bytes(), StandardCharsets.UTF_8);
                JSONObject errorJsonObject = new JSONObject(errorString);
                processCallback.onFailure("failed to process! " + errorJsonObject.getString("message"));
            }
        } catch (JSONException | IOException jsonException) {
            jsonException.printStackTrace();
        }
    }
}
    /*-
    public void update_user_no_pic(int id, String name, String email, String mobile, String national_number, int family_members, char gender, ProcessCallback processCallback) {
        Call<BaseResponse<User>> call = ApiController.getInstance().getRetrofitRequests().UPDATE_USERS_NO_PIC_CALL(id, name, email, mobile, national_number, family_members, gender);
        call.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<User>> call, @NonNull Response<BaseResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processCallback.onSuccess("updated successfully =>" + response.body().message);
                } else {
                    processCallback.onFailure("failed to update user!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<User>> call, @NonNull Throwable throwable) {
                processCallback.onFailure("something went wrong!");
            }
        });
    }
*/
    /*-
    public void update_user(int id, String name, String email, String mobile, String national_number, int family_members, char gender, byte[] imagePart, ProcessCallback processCallback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imagePart);
        MultipartBody.Part file = MultipartBody.Part.createFormData("image", "image-file", requestBody);

        Call<BaseResponse<User>> call = ApiController.getInstance().getRetrofitRequests().UPDATE_USERS_FULL_CALL(id, name, email, mobile, national_number, family_members, gender, file);
        call.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<User>> call, @NonNull Response<BaseResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processCallback.onSuccess("updated successfully =>" + response.body().message);
                } else {
                    processCallback.onFailure("failed to update user!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<User>> call, @NonNull Throwable throwable) {
                processCallback.onFailure("something went wrong!");
            }
        });
    }
*/
    /*1
    map.put("name", RequestBody.create(MediaType.parse("text/plain"), name));
    map.put("email", RequestBody.create(MediaType.parse("text/plain"), email));
    map.put("mobile", RequestBody.create(MediaType.parse("text/plain"), mobile));
    map.put("national_number", RequestBody.create(MediaType.parse("text/plain"), national_number));
    map.put("family_members", RequestBody.create(MediaType.parse("text/plain"), family_members));
    map.put("gender", RequestBody.create(MediaType.parse("text/plain"), gender));
*/
    /*-
    public void create_user(String name, String email, String mobile, String national_number, String family_members, String gender, byte[] imagePart, ProcessCallback processCallback) {
        RequestBody r_name = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody r_email = RequestBody.create(MediaType.parse("multipart/form-data"), email);
        RequestBody r_mobile = RequestBody.create(MediaType.parse("multipart/form-data"), mobile);
        RequestBody r_national_number = RequestBody.create(MediaType.parse("multipart/form-data"), national_number);
        RequestBody r_family_member = RequestBody.create(MediaType.parse("multipart/form-data"), family_members);
        RequestBody r_gender = RequestBody.create(MediaType.parse("multipart/form-data"), gender);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imagePart);
        MultipartBody.Part file = MultipartBody.Part.createFormData("image", "image-file", requestBody);
        Call<BaseResponse<User>> call = ApiController.getInstance().getRetrofitRequests().create_user(r_name, r_email, r_mobile, r_national_number, r_family_member, r_gender, file);
        call.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<User>> call, @NonNull Response<BaseResponse<User>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    processCallback.onSuccess("user created successfully " + response.body().message);

                } else {
                    if (response.body() != null) {
                        Log.i("TAG", "onResponse: " + response.body().message + "=> " + response.body().status);
                    }
                    Log.i(TAG, "onRes: isSuccessful" + response.isSuccessful());
                    Log.i(TAG, "onRes: code" + response.code());
                    Log.i(TAG, "onRes: massage" + response.message());
                    processCallback.onFailure("failed to create a new user! " + response.code() + response.message());

                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<User>> call, @NonNull Throwable throwable) {
                processCallback.onFailure("something went wrong!");
            }
        });
    }
*/