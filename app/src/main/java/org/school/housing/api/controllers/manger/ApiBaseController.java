package org.school.housing.api.controllers.manger;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;
import org.school.housing.interfaces.ListCallback;
import org.school.housing.interfaces.ProcessCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import retrofit2.Response;

public class ApiBaseController {
    private static final String TAG = "ApiBaseController";
    //Message Error Method
    protected void generalErrorMessage(@NonNull Response<?> response, ProcessCallback processCallback) {
        Log.i(TAG, "generalErrorMessage: something went wrong!");
        try {
            if (response.errorBody() != null) {
                String errorString = new String(response.errorBody().bytes(), StandardCharsets.UTF_8);
                JSONObject errorJsonObject = new JSONObject(errorString);
                processCallback.onFailure("failed to process! " + errorJsonObject.getString("message"));
            }else {
                Log.i(TAG, "generalErrorMessage: errorBody is null");
            }
        } catch (JSONException | IOException jsonException) {
            jsonException.printStackTrace();
            processCallback.onFailure("Sorry Please Try Again Later!");
        }
    }

    protected void generalErrorMessageForContent(@NonNull Response<?> response, ListCallback<?> processCallback) {
        Log.i(TAG, "generalErrorMessageForContent Runs");
        try {
            if (response.errorBody() != null) {
                String errorString = new String(response.errorBody().bytes(), StandardCharsets.UTF_8);
                JSONObject errorJsonObject = new JSONObject(errorString);
                processCallback.onFailure("failed to process! " + errorJsonObject.getString("message"));
            }else {
                Log.i(TAG, "generalErrorMessageForContent: errorBody is null");
            }
        } catch (JSONException | IOException jsonException) {
            jsonException.printStackTrace();
            processCallback.onFailure("Sorry Please Try Again Later!");
        }
    }

}
