package com.udacity.agostinocoppolino.bakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends ArrayAdapter<Ingredient> {


    public IngredientAdapter(@NonNull Context context, int resource, @NonNull List<Ingredient> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        convertView = inflater.inflate(R.layout.ingredient_item, null);

        TextView mQuantityTextView = convertView.findViewById(R.id.tv_quantity);
        TextView mMeasureTextView = convertView.findViewById(R.id.tv_measure);
        TextView mIngredientTextView = convertView.findViewById(R.id.tv_ingredient);
        Ingredient ingredient = getItem(position);
        mQuantityTextView.setText(ingredient.getQuantity());
        mMeasureTextView.setText(ingredient.getMeasure());
        mIngredientTextView.setText(ingredient.getQuantity());
        return convertView;
    }
}
