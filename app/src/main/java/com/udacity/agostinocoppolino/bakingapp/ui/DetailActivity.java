package com.udacity.agostinocoppolino.bakingapp.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Recipe;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        recipe = intent != null ? (Recipe) intent.getParcelableExtra("Recipe") : null;

        if (recipe != null) {
            this.setTitle(recipe.getName());

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
            }
        }

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }



}
