package com.udacity.agostinocoppolino.bakingapp.utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class NetworkUtils {

    private static final String RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static String getHttpResponse() {
        OkHttpClient httpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(RECIPES_URL)
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Timber.e("error in getting response get request okhttp");
        }
        return null;
    }
}
