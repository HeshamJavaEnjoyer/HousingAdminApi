package org.school.housing.api;

import androidx.annotation.NonNull;
import org.school.housing.Prefs.AppSharedPreferences;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSettings {
    //Retrofit Instance
    private static Retrofit retrofit;

    //BaseURL
    private static final String BaseURL = "https://towers.mr-dev.tech/api/";
    //this BaseURL are connected to the interface

    //Empty Constructor
    public RetrofitSettings() {
    }

    public static synchronized Retrofit getInstance() {
        if (retrofit == null) {
            //Make-method that control the client
            OkHttpClient client = getOkHttpClient();
            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BaseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient getOkHttpClient() {
        OkHttpClient client;
        client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request.Builder builder = chain.request().newBuilder();

                    builder.addHeader("Accept", "application/json");
                    builder.addHeader("Content-Type", "application/json");
                    builder.addHeader("Authorization", AppSharedPreferences.getInstance().getToken());

                    return chain.proceed(builder.build());
                }).build();
        return client;
    }
}
