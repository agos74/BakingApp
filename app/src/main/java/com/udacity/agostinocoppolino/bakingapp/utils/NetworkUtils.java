package com.udacity.agostinocoppolino.bakingapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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

    /**
     * This method returns the state of network access.
     *
     * @param context The context required for call getSystemService.
     * @return true if network access is available.
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork;
        activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }

}
