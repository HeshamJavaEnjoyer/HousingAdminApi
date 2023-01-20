package org.school.housing.api.controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import org.school.housing.api.controllers.manger.ApiBaseController;
import org.school.housing.api.controllers.manger.ApiController;
import org.school.housing.interfaces.ProcessCallback;
import org.school.housing.models.BaseResponse;
import org.school.housing.models.Operation;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperationApiController extends ApiBaseController {
    private static final String TAG = "OperationApiController";

    private static OperationApiController Instance;
    private static ApiController apiController;

    public OperationApiController() {
        apiController = ApiController.getInstance();
    }

    public static synchronized OperationApiController getInstance() {
        if (Instance == null) {
            Instance = new OperationApiController();
        }
        return Instance;
    }

    public void storeOperation(String category_id, String amount, String details, String actor_id, String actor_type, String date, ProcessCallback processCallback) {
        Log.d(TAG, "storeOperation() category_id :" + category_id + " amount :" + amount + " details :" + details + " actor_id :" + actor_id + " actor_type :" + actor_type + " date :" + date);
        HashMap<String, RequestBody> map = new HashMap<>();
        String[] values = {category_id, amount, details, actor_id, actor_type, date};
        String[] keys = {"category_id", "amount", "details", "actor_id", "actor_type", "date"};
        for (int i = 0; i <= values.length - 1; i++) {
            map.put(keys[i], RequestBody.create(MediaType.parse("text/plain"), values[i]));
        }

        Call<BaseResponse<Operation>> call = apiController.getRetrofitRequests().store_operation(map);
        call.enqueue(new Callback<BaseResponse<Operation>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<Operation>> call, @NonNull Response<BaseResponse<Operation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                    Log.d(TAG, "onResponse() returned: " + response.body().object.date);
                    processCallback.onSuccess("OpSuccessfullyProcessed: " + response.body().message);
                } else {
                    generalErrorMessage(response, processCallback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<Operation>> call, @NonNull Throwable throwable) {
                processCallback.onFailure("Server Error 500");
            }
        });
    }

}
