package com.udacity.agostinocoppolino.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.agostinocoppolino.bakingapp.Constants;
import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Ingredient;
import com.udacity.agostinocoppolino.bakingapp.model.Recipe;

import java.util.List;

import timber.log.Timber;

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Recipe recipe = null;
        //get the Recipe
        if (intent.hasExtra("BUNDLE")) {
            Bundle b = intent.getBundleExtra("BUNDLE");
            recipe = b.getParcelable(Constants.RECIPE_KEY);
        }
        return new ListRemoteViewsFactory(this.getApplicationContext(), recipe);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    private List<Ingredient> mIngredientsList;
    private Recipe mRecipe;

    public ListRemoteViewsFactory(Context applicationContext, Recipe recipe) {
        mContext = applicationContext;
        mRecipe = recipe;
        mIngredientsList = recipe.getIngredients();
        Timber.d(mIngredientsList.toString());
    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        Timber.d("onDataSetChanged");

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return (mIngredientsList == null) ? 0 : mIngredientsList.size();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {

        if (mIngredientsList == null || mIngredientsList.size() == 0) return null;
        final Ingredient ingredient = mIngredientsList.get(position);
        final String ingredientQuantity = ingredient.getQuantity();
        final String ingredientMeasure = ingredient.getMeasure();
        final String ingredientName = ingredient.getIngredient();

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_provider);

        views.setTextViewText(R.id.tv_quantity, ingredientQuantity);
        views.setTextViewText(R.id.tv_measure, ingredientMeasure);
        views.setTextViewText(R.id.tv_ingredient, ingredientName);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}