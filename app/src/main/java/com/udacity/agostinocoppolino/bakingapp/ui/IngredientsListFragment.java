package com.udacity.agostinocoppolino.bakingapp.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsListFragment extends Fragment {

    private List<Ingredient> mIngredientsList;
    private static final String INGREDIENTS_LIST_TEXT_KEY = "ingredients_list";

    private static final String INGREDIENTS_LIST_EXPANDED_TEXT_KEY = "ingredients_list_expanded";
    private boolean mIngredientsListExpanded = false;

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.fragment_ingredients_list, container, false);

        ButterKnife.bind(this, rootView);

        // Load the saved state (the list of steps) if there is one
        if (savedInstanceState != null) {
            mIngredientsList = savedInstanceState.getParcelableArrayList(INGREDIENTS_LIST_TEXT_KEY);

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

        // Create the adapter
        // This adapter takes in the context and an ArrayList of Ingredients to display
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

        return rootView;
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
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList(INGREDIENTS_LIST_TEXT_KEY, (ArrayList<? extends Parcelable>) mIngredientsList);
        //Put the ingredients list expanded state in the outState bundle
        currentState.putBoolean(INGREDIENTS_LIST_EXPANDED_TEXT_KEY, mIngredientsListExpanded);

    }

}
