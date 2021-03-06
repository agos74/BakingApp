package com.udacity.agostinocoppolino.bakingapp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.Constants;
import com.udacity.agostinocoppolino.bakingapp.utils.NetworkUtils;
import com.udacity.agostinocoppolino.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<Recipe>> {

    private static final int RECIPES_LOADER_ID = 0;

    //ButterKnife Binding
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.recyclerview_main)
    RecyclerView mRecyclerView;

    /*
     * The ProgressBar that will indicate to the user that we are loading data. It will be
     * hidden when no data is loading.
     */
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    private RecipeAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Set up Timber
        Timber.plant(new Timber.DebugTree());

        /*
         * GridLayoutManager to show the recipe images
         */
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());

        /* Set the layoutManager on mRecyclerView */
        mRecyclerView.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance because we know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        // set mRecipeAdapter equal to a new RecipeAdapterRecipeAdapter
        mRecipeAdapter = new RecipeAdapter(this);

        /* attaches adapter to the RecyclerView in layout. */
        mRecyclerView.setAdapter(mRecipeAdapter);

        if (NetworkUtils.isConnected(getBaseContext())) {
            // Initialize the AsyncTaskLoader
            getSupportLoaderManager().initLoader(RECIPES_LOADER_ID, null, MainActivity.this);
        } else {
            showErrorMessage(getBaseContext().getResources().getString(R.string.network_error_message));
        }

    }


    /**
     * This method will make the loading indicator visible and
     * hide the recipes data.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't need to check whether
     * each view is currently visible or invisible.
     */
    private void showLoading() {
        /* Show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
        // Hide mRecyclerView
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    /**
     * This method will make the View for the recipes data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showRecipesDataView() {
        // Hide the load indicator
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        // Show mRecyclerView
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the recipes
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     *
     * @param errorMessage Error message to display in alert
     */
    private void showErrorMessage(String errorMessage) {
        // Hide the load indicator
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Show the error */
        showAlertDialog(errorMessage);
    }


    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        showLoading();
        return new MyAsyncTaskLoader(getBaseContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> recipesList) {
        if (recipesList != null) {
            showRecipesDataView();
            // Instead of iterating through every recipe, use mRecipeAdapter.setRecipesList and pass in the recipes List
            mRecipeAdapter.setRecipesList(recipesList);
        } else {
            showErrorMessage(getBaseContext().getResources().getString(R.string.network_error_message));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Recipe>> loader) {

    }

    private static class MyAsyncTaskLoader extends AsyncTaskLoader<List<Recipe>> {

        List<Recipe> mRecipesList = null;

        MyAsyncTaskLoader(Context context) {
            super(context);
        }

        @Override
        protected void onStartLoading() {
            if (mRecipesList != null) {
                deliverResult(mRecipesList);
            } else {
                forceLoad();
            }

        }

        @Override
        public List<Recipe> loadInBackground() {

            try {

                String response = NetworkUtils.getHttpResponse();

                List<Recipe> recipesList = null;
                if (response != null) {
                    Timber.d("populate UI after response from service using OkHttp client");
                    Gson gsn = new GsonBuilder().create();
                    Type collectionType = new TypeToken<List<Recipe>>() {
                    }.getType();
                    recipesList = gsn.fromJson(response, collectionType);
                    for (Recipe entry : recipesList) {
                        Timber.d(entry.toString().concat(","));
                    }
                }

                return recipesList;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Sends the result of the load to the registered listener.
         *
         * @param recipesList The result of the load
         */
        public void deliverResult(List<Recipe> recipesList) {
            mRecipesList = recipesList;
            super.deliverResult(recipesList);
        }

    }


    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param recipe The recipe that was clicked
     */
    @Override
    public void onClick(Recipe recipe) {
        // Launch the DetailActivity using an explicit Intent
        Intent intentToStartDetailActivity = new Intent(this, DetailActivity.class);
        intentToStartDetailActivity.putExtra(Constants.RECIPE_KEY, recipe);
        startActivity(intentToStartDetailActivity);
    }

    /**
     * This method dynamically calculate the number of columns and the layout would adapt to the screen size and orientation
     */
    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
//        int widthDivider = 300;
        int width = displayMetrics.widthPixels;
        //noinspection UnnecessaryLocalVariable
        int nColumns = width / widthDivider;
//        if (nColumns < 2) return 2;
        return nColumns;
    }

    private void showAlertDialog(final String errorMessage) {

        AlertDialog alertDialog = new AlertDialog.Builder(
                MainActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle(getString(R.string.alert_dialog_title));

        // Make a choice mandatory
        alertDialog.setCancelable(false);

        // Setting Dialog Message
        alertDialog.setMessage(errorMessage);

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_offline);

        // Setting Retry Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.retry_button_title),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // retry reloading
                        if (NetworkUtils.isConnected(getBaseContext())) {
                            getSupportLoaderManager().initLoader(RECIPES_LOADER_ID, null, MainActivity.this);
                        } else {
                            showErrorMessage(errorMessage);
                        }
                        dialog.dismiss();// use dismiss to cancel alert dialog
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

}
