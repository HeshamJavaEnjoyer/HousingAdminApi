package org.school.housing.api.controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;
import org.school.housing.Prefs.AppSharedPreferences;
import org.school.housing.api.controllers.manger.ApiController;
import org.school.housing.interfaces.ProcessCallback;
import org.school.housing.models.admin.Admin;
import org.school.housing.models.special_respond.AuthResponse;
import org.school.housing.models.special_respond.PasswordChangeResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// STOPSHIP: 1/8/2023 => ✔✔✔
public class AuthApiController {
    //lets make a singleton
    public AuthApiController() {
    }

    private static AuthApiController Instance;

    public static AuthApiController getInstance() {
        if (Instance == null) {
            Instance = new AuthApiController();
        }
        return Instance;
    }

    public void login(String email, String password, ProcessCallback callback) {
        Call<AuthResponse<Admin>> call = ApiController.getInstance().getRetrofitRequests().POST_Login_Call(email, password);
        call.enqueue(new Callback<AuthResponse<Admin>>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse<Admin>> call, @NonNull Response<AuthResponse<Admin>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AppSharedPreferences.getInstance().save(response.body().object);// here we save the admin and his token in our AppSharedPreferences ==>so we could use his token in the headers
                    Log.d("TAG", "onResponse() returned: " + response.body().object.name + " " + response.body().object.towerName);
                    callback.onSuccess(response.body().message);
                } else {
                    try {
                        assert response.errorBody() != null;
                        String error_string = new String(response.errorBody().bytes(), StandardCharsets.UTF_8);
                        JSONObject jsonObject = new JSONObject(error_string);
                        callback.onFailure(jsonObject.getString("message"));
                    } catch (IOException | JSONException ioException) {
                        ioException.printStackTrace();
                    }
                    callback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse<Admin>> call, @NonNull Throwable throwable) {
                callback.onFailure("Something went wrong while login process");
            }
        });
    }

    public void logout(ProcessCallback processCallback) {
        Call<AuthResponse<Admin>> call = ApiController.getInstance().getRetrofitRequests().GET_Logout();
        call.enqueue(new Callback<AuthResponse<Admin>>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse<Admin>> call, @NonNull Response<AuthResponse<Admin>> response) {
                if ((response.isSuccessful() || (response.code() == 200 || response.code() == 401))) {
                    AppSharedPreferences.getInstance().clear();
                    processCallback.onSuccess("Logged Out Successfully");
                } else {
                    //temp!!!! ill pass

                    processCallback.onFailure("SorryAlmostDoneRight");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse<Admin>> call, @NonNull Throwable t) {
                processCallback.onFailure("Something went wrong !");
                t.printStackTrace();
            }
        });
    }

    //Password Authorization
    public void forget_password(String mobilePhone, ProcessCallback processCallback) {
        Call<AuthResponse<Admin>> call = ApiController.getInstance().getRetrofitRequests().Post_Forget_password(mobilePhone);
        call.enqueue(new Callback<AuthResponse<Admin>>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse<Admin>> call, @NonNull Response<AuthResponse<Admin>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processCallback.onSuccess("forget_password done successfully =>" + response.body().message);
                } else {
                    processCallback.onFailure("failed to change password!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse<Admin>> call, @NonNull Throwable throwable) {
                processCallback.onFailure("ServerError 500");
            }
        });
    }

    public void reset_password(String mobilePhone, String code, String newPass, String newPassConfirm, ProcessCallback processCallback) {
        Call<AuthResponse<Admin>> call = ApiController.getInstance().getRetrofitRequests().Post_Reset_password(mobilePhone, code, newPass, newPassConfirm);
        call.enqueue(new Callback<AuthResponse<Admin>>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse<Admin>> call, @NonNull Response<AuthResponse<Admin>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processCallback.onSuccess("reset successfully =>" + response.body().message);
                } else {
                    processCallback.onFailure("failed to change password!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse<Admin>> call, @NonNull Throwable throwable) {
                processCallback.onFailure("ServerError 500");
            }
        });
    }

    public void change_password(String currentPass, String newPass, String newPassConfirm, ProcessCallback processCallback) {
        Call<PasswordChangeResponse> call = ApiController.getInstance().getRetrofitRequests().POSTChangePassword(currentPass, newPass, newPassConfirm);
        call.enqueue(new Callback<PasswordChangeResponse>() {
            @Override
            public void onResponse(@NonNull Call<PasswordChangeResponse> call, @NonNull Response<PasswordChangeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processCallback.onSuccess("changed successfully =>" + response.body().message);
                } else {
                    processCallback.onFailure("failed to change password!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<PasswordChangeResponse> call, @NonNull Throwable throwable) {
                processCallback.onFailure("ServerError 500");
            }
        });
    }

}
