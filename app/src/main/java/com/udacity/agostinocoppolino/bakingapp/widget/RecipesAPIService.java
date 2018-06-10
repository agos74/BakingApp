package com.udacity.agostinocoppolino.bakingapp.widget;

import com.udacity.agostinocoppolino.bakingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

interface RecipesAPIService {

    String ENDPOINT = "https://d17h27t6h515a5.cloudfront.net/";

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<Recipe>> getRecipes();


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
