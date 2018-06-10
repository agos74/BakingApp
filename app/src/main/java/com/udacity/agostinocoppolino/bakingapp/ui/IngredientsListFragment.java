package com.udacity.agostinocoppolino.bakingapp.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
public class IngredientsListFragment extends Fragment {

    private static final String INGREDIENTS_LIST_TEXT_KEY = "ingredients_list";
    private static final String INGREDIENTS_LIST_EXPANDED_TEXT_KEY = "ingredients_list_expanded";
    private final String RECIPE_SERVINGS_TEXT_KEY = "recipe_servings";

    private List<Ingredient> mIngredientsList;
    private int mServings;

    private boolean mIngredientsListExpanded = false;

    @BindView(R.id.tv_servings)
    TextView mServingsTextView;

    @BindView(R.id.arrow_up_button)
    ImageButton mArrowUpButton;

    @BindView(R.id.arrow_down_button)
    ImageButton mArrowDownButton;

    @BindView(R.id.ingredients_list_view)
    ListView mIngredientsListView;


    // Mandatory empty constructor
    public IngredientsListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_ingredients_list, container, false);

        ButterKnife.bind(this, view);

        // Load the saved state (the list of steps) if there is one
        if (savedInstanceState != null) {
            mIngredientsList = savedInstanceState.getParcelableArrayList(INGREDIENTS_LIST_TEXT_KEY);
            mServings = savedInstanceState.getInt(RECIPE_SERVINGS_TEXT_KEY);

            // Restore the ingredients list expanded state
            if (savedInstanceState.containsKey(INGREDIENTS_LIST_EXPANDED_TEXT_KEY)) {
                mIngredientsListExpanded = savedInstanceState
                        .getBoolean(INGREDIENTS_LIST_EXPANDED_TEXT_KEY);
                if (mIngredientsListExpanded) {
                    expandMoreIngredientsList();
                } else {
                    expandLessIngredientsList();
                }
            }

        }

        // Set servings
        String servingsText = String.valueOf(mServings);
        mServingsTextView.setText(servingsText);

        // Create the adapter
        // This adapter takes in the context and an ArrayList of Ingredients to display
        //noinspection ConstantConditions
        IngredientAdapter mAdapter = new IngredientAdapter(getContext(), mIngredientsList);

        // Set the adapter on the ListView
        mIngredientsListView.setAdapter(mAdapter);


        mArrowUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                expandLessIngredientsList();
            }
        });

        mArrowDownButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                expandMoreIngredientsList();
            }
        });

        return view;
    }

    public void setIngredientsList(List<Ingredient> ingredientsList) {
        mIngredientsList = ingredientsList;
    }


    private void expandMoreIngredientsList() {
        mArrowDownButton.setVisibility(View.GONE);
        mArrowUpButton.setVisibility(View.VISIBLE);
        mIngredientsListView.setVisibility(View.VISIBLE);
        mIngredientsListExpanded = true;
    }

    private void expandLessIngredientsList() {
        mArrowUpButton.setVisibility(View.GONE);
        mArrowDownButton.setVisibility(View.VISIBLE);
        mIngredientsListView.setVisibility(View.GONE);
        mIngredientsListExpanded = false;
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        currentState.putParcelableArrayList(INGREDIENTS_LIST_TEXT_KEY, (ArrayList<? extends Parcelable>) mIngredientsList);
        currentState.putInt(RECIPE_SERVINGS_TEXT_KEY, mServings);
        //Put the ingredients list expanded state in the currentState bundle
        currentState.putBoolean(INGREDIENTS_LIST_EXPANDED_TEXT_KEY, mIngredientsListExpanded);

    }

    public void setServings(int servings) {
        mServings = servings;
    }
}
