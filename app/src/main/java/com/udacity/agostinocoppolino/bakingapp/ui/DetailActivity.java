package com.udacity.agostinocoppolino.bakingapp.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.arrow_up_button)
    ImageButton mArrowUpButton;

    @BindView(R.id.arrow_down_button)
    ImageButton mArrowDownButton;

    @BindView(R.id.ingredients_list)
    ListView mIngredientsListView;

    private static final String INGREDIENTS_LIST_EXPANDED_TEXT_KEY = "ingredients_list_expanded";
    private boolean mIngredientsListExpanded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = this.getSupportActionBar();
        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        Recipe recipe = intent != null ? (Recipe) intent.getParcelableExtra("Recipe") : null;

        if (recipe != null) {
            this.setTitle(recipe.getName());

            // Create the adapter to convert the array to views
            IngredientAdapter ingredientAdapter = new IngredientAdapter(this, recipe.getIngredients());
            // Attach the adapter to a ListView
            ListView listView = findViewById(R.id.ingredients_list);
            listView.setAdapter(ingredientAdapter);

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

            // Only create new fragments when there is no previously saved state
            if (savedInstanceState == null) {

                // Create a new stepsListFragment
                StepsListFragment stepsListFragment = new StepsListFragment();

                // Set the list of steps for the fragment
                stepsListFragment.setStepsList(recipe.getSteps());

                // Add the fragment to its container using a FragmentManager and a Transaction
                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.steps_list_container, stepsListFragment)
                        .commit();
            } else {
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
        }

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Put the ingredients list expanded state in the outState bundle
        outState.putBoolean(INGREDIENTS_LIST_EXPANDED_TEXT_KEY, mIngredientsListExpanded);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When the home button is pressed, take the user back to the MainActivity
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
