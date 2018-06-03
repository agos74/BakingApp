package com.udacity.agostinocoppolino.bakingapp.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Recipe;
import com.udacity.agostinocoppolino.bakingapp.Constants;

import butterknife.ButterKnife;
import timber.log.Timber;

public class DetailActivity extends AppCompatActivity {

    // Track whether to display a two-pane or single-pane UI
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private boolean mTwoPane;


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

        Recipe recipe = intent != null ? (Recipe) intent.getParcelableExtra(Constants.RECIPE_KEY) : null;

        if (recipe != null) {
            this.setTitle(recipe.getName());

            // Only create new fragments when there is no previously saved state
            if (savedInstanceState == null) {

                // Create a new ingredientsListFragment
                IngredientsListFragment ingredientsListFragment = new IngredientsListFragment();
                // Set the list of ingredients for the fragment
                ingredientsListFragment.setIngredientsList(recipe.getIngredients());
                ingredientsListFragment.setServings(recipe.getServings());

                // Add the fragment to its container using a FragmentManager and a Transaction
                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.ingredients_list_container, ingredientsListFragment)
                        .commit();

                // Create a new stepsListFragment
                StepsListFragment stepsListFragment = new StepsListFragment();

                // Set the list of steps and the recipe name for the fragment
                stepsListFragment.setStepsList(recipe.getSteps());
                stepsListFragment.setRecipeName(recipe.getName());

                fragmentManager.beginTransaction()
                        .add(R.id.steps_list_container, stepsListFragment)
                        .commit();

                // Determine if you're creating a two-pane or single-pane display
                if (findViewById(R.id.step_container) != null) {
                    // This FrameLayout will only initially exist in the two-pane tablet case
                    mTwoPane = true;

                    Timber.d("Two Pane");

                    // Create a new stepFragment
                    StepFragment stepFragment = new StepFragment();

                    // Set StepIndex and StepsList for the fragment
                    stepFragment.setStepsList(recipe.getSteps());
                    stepFragment.setStepIndex(0);

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.step_container, stepFragment)
                            .commit();

                } else {

                    // We're in single-pane mode and displaying fragments on a phone in separate activities
                    mTwoPane = false;

                }
            }
        }

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
