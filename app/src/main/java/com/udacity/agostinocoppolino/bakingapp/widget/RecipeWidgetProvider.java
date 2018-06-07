package com.udacity.agostinocoppolino.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.udacity.agostinocoppolino.bakingapp.Constants;
import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Recipe;
import com.udacity.agostinocoppolino.bakingapp.ui.DetailActivity;
import com.udacity.agostinocoppolino.bakingapp.ui.MainActivity;

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

                    CharSequence recipeSelectedPosition = RecipeWidgetProviderConfigureActivity.loadRecipePref(context, appWidgetId);

                    if (recipeSelectedPosition != null) {
                        Timber.d("Recipe Selected Position: ".concat(recipeSelectedPosition.toString()));

                        Recipe recipe = recipeList.get(Integer.valueOf(String.valueOf(recipeSelectedPosition)));

                        // Set the click handler to open the DetailActivity for recipe,
                        Timber.d("recipeName=" + recipe.getName());
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra(Constants.RECIPE_KEY, recipe);
                        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                        // When intents are compared, the extras are ignored, so we need to embed the extras
                        // into the data so that the extras will not be ignored.
                        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        // Use TaskStackBuilder to build the back stack and get the PendingIntent
                        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                                // add all of DetailsActivity's parents to the stack,
                                // followed by DetailsActivity itself
                                .addNextIntentWithParentStack(intent)
                                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                        // Construct the RemoteViews object
                        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
                        views.setTextViewText(R.id.recipe_name, recipe.getName());

                        // Widgets allow click handlers to only launch pending intents
                        views.setOnClickPendingIntent(R.id.layout_widget, pendingIntent);

                        // Set the ListWidgetService intent to act as the adapter for the ListView
                        Intent serviceIntent = new Intent(context, ListWidgetService.class);
                        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                        Bundle b = new Bundle();
                        b.putParcelable(Constants.RECIPE_KEY, recipe);
                        serviceIntent.putExtra("BUNDLE", b);

                        // When intents are compared, the extras are ignored, so we need to embed the extras
                        // into the data so that the extras will not be ignored.
                        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

                        views.setRemoteAdapter(R.id.widget_ingredients_list_view, serviceIntent);

                        // Instruct the widget manager to update the widget
                        appWidgetManager.updateAppWidget(appWidgetId, views);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Timber.d("Failure Response: ".concat(t.getMessage()));
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
            RecipeWidgetProviderConfigureActivity.deleteRecipePref(context, appWidgetId);
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


}