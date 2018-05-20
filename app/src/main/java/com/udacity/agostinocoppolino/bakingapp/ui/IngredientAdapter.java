package com.udacity.agostinocoppolino.bakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Ingredient;

import java.util.ArrayList;
import java.util.List;


public class IngredientAdapter extends ArrayAdapter<Ingredient> {

    private static final String TAG = IngredientAdapter.class.getSimpleName();

    private Context mContext;
    private List<Ingredient> ingredientsList = new ArrayList<>();

    public IngredientAdapter(@NonNull Context context, @NonNull List<Ingredient> list) {
        super(context, 0, list);

        mContext = context;
        ingredientsList = list;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;

        // Check if an existing view is being reused, otherwise inflate the view
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.ingredient_item, parent, false);
        }

        Ingredient ingredient = ingredientsList.get(position);

        Log.d(TAG, "Ingredient: " + ingredient.toString());

        TextView mQuantityTextView = listItem.findViewById(R.id.tv_quantity);
        TextView mMeasureTextView = listItem.findViewById(R.id.tv_measure);
        TextView mIngredientTextView = listItem.findViewById(R.id.tv_ingredient);
        mQuantityTextView.setText(ingredient.getQuantity());
        mMeasureTextView.setText(ingredient.getMeasure());
        mIngredientTextView.setText(ingredient.getIngredient());

        return listItem;
    }
}
