package com.udacity.agostinocoppolino.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.udacity.agostinocoppolino.bakingapp.Constants;
import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link RecipeWidgetProviderConfigureActivity RecipeWidgetProviderConfigureActivity}
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        Timber.d("appWidgetId ".concat(String.valueOf(appWidgetId)));
        // To prevent any ANR timeouts, we perform the update in a Retrofit style or create another service
        RecipesAPIService gitHubService = RecipesAPIService.retrofit.create(RecipesAPIService.class);
        Call<ArrayList<Recipe>> call = gitHubService.getRecipes();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                if (response.isSuccessful()) {

                    ArrayList<Recipe> recipeList = new ArrayList<>();

                    recipeList.addAll(response.body());

                    CharSequence recipeId = RecipeWidgetProviderConfigureActivity.loadTitlePref(context, appWidgetId);

                    Timber.d("Recipe ID: ".concat(recipeId.toString()));

                    Recipe recipe = recipeList.get(1);

                    // Construct the RemoteViews object
                    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
                    views.setTextViewText(R.id.recipe_name, recipe.getName());

                    // Set the ListWidgetService intent to act as the adapter for the ListView
                    Intent intent = new Intent(context, ListWidgetService.class);
                    Bundle b = new Bundle();
                    b.putParcelable(Constants.RECIPE_KEY, recipe);
                    intent.putExtra("BUNDLE", b);

                    views.setRemoteAdapter(R.id.ingredients_list_view, intent);

                    // Instruct the widget manager to update the widget
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {

            }
        });

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Timber.d("OnUpdate ");
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            RecipeWidgetProviderConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    // Read the prefix from the SharedPreferences object for this widget.


}