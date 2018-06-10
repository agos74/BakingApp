package com.udacity.agostinocoppolino.bakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


class IngredientAdapter extends ArrayAdapter<Ingredient> {

    private final Context mContext;
    private final List<Ingredient> ingredientsList;

    public IngredientAdapter(@NonNull Context context, @NonNull List<Ingredient> list) {
        super(context, 0, list);

        mContext = context;
        ingredientsList = list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;

        View listItem = convertView;

        // Check if an existing view is being reused, otherwise inflate the view
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.ingredient_item, parent, false);
            holder = new ViewHolder(listItem);
            listItem.setTag(holder);
        } else {
            holder = (ViewHolder) listItem.getTag();
        }

        Ingredient ingredient = ingredientsList.get(position);

        Timber.d("Ingredient: ".concat(ingredient.toString()));

        holder.mQuantityTextView.setText(ingredient.getQuantity());
        holder.mMeasureTextView.setText(ingredient.getMeasure());
        holder.mIngredientTextView.setText(ingredient.getIngredient());

        return listItem;
    }

    static class ViewHolder {
        @BindView(R.id.tv_quantity)
        TextView mQuantityTextView;
        @BindView(R.id.tv_measure)
        TextView mMeasureTextView;
        @BindView(R.id.tv_ingredient)
        TextView mIngredientTextView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
