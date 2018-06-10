package com.udacity.agostinocoppolino.bakingapp.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


/**
 * The configuration screen for the {@link RecipeWidgetProvider RecipeWidgetProvider} AppWidget.
 */
@SuppressWarnings("unchecked")
public class RecipeWidgetProviderConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.udacity.agostinocoppolino.bakingapp.widget.RecipeWidgetProvider";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private ArrayList<Recipe> mRecipeArrayList;
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Spinner mSpinnerRecipeSelected;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = RecipeWidgetProviderConfigureActivity.this;

            // When the button is clicked, store the recipe selected position locally
            int recipeSelectedPosition = mSpinnerRecipeSelected.getSelectedItemPosition();

            Timber.d("Recipe selected position: ".concat(String.valueOf(recipeSelectedPosition)));

            saveRecipePref(context, mAppWidgetId, String.valueOf(recipeSelectedPosition));

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RecipeWidgetProvider.updateAppWidgetFirstConfigure(context, appWidgetManager, mAppWidgetId, mRecipeArrayList);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public RecipeWidgetProviderConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    private static void saveRecipePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadRecipePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
    }

    static void deleteRecipePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.recipe_widget_provider_configure);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        Timber.d("configuration activity started");
        // To prevent any ANR timeouts, we perform the update in a Retrofit style or create another service
        RecipesAPIService gitHubService = RecipesAPIService.retrofit.create(RecipesAPIService.class);
        Call<ArrayList<Recipe>> call = gitHubService.getRecipes();
        call.enqueue(new Callback<ArrayList<Recipe>>() {

            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
                if (response.isSuccessful()) {

                    mRecipeArrayList = new ArrayList<>();

                    mRecipeArrayList.addAll(response.body());

                    ArrayList<String> recipeNames = new ArrayList<>();
                    //get array with name of recipes
                    for (Recipe recipe : mRecipeArrayList) {
                        recipeNames.add(recipe.getName());
                    }

                    Timber.d("Recipe names: ".concat(recipeNames.toString()));

                    mSpinnerRecipeSelected = findViewById(R.id.recipes_spinner);
                    // Creating the ArrayAdapter instance having the recipe name list
                    ArrayAdapter aa = new ArrayAdapter(mSpinnerRecipeSelected.getContext(), android.R.layout.simple_spinner_item, recipeNames);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Setting the ArrayAdapter data on the Spinner
                    mSpinnerRecipeSelected.setAdapter(aa);

                    findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

                    // Find the widget id from the intent.
                    Intent intent = getIntent();
                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        mAppWidgetId = extras.getInt(
                                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
                    }

                    // If this activity was started with an intent without an app widget ID, finish with an error.
                    if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                        finish();
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {
                Timber.d("Failure Response: ".concat(t.getMessage()));

            }

        });


    }
}

