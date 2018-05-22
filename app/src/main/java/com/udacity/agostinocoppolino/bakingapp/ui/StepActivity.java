package com.udacity.agostinocoppolino.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Step;

import java.util.List;

import timber.log.Timber;


public class StepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        ActionBar actionBar = this.getSupportActionBar();
        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int stepIndex = intent != null ? intent.getExtras().getInt("StepIndex") : -1;

        String recipeName = intent.getExtras().getString("RecipeName");

        List<Step> stepsList = intent.getParcelableArrayListExtra("StepsList");

        Timber.d(stepsList.toString());

        if (stepIndex != -1) {
            this.setTitle(recipeName);

            // Only create new fragments when there is no previously saved state
            if (savedInstanceState == null) {

                // Create a new stepFragment
                StepFragment stepFragment = new StepFragment();

                // Set StepIndex and StepsList for the fragment
                stepFragment.setStepIndex(stepIndex);
                stepFragment.setStepsList(stepsList);

                // Add the fragment to its container using a FragmentManager and a Transaction
                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.step_container, stepFragment)
                        .commit();


            }
        }

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.step_error_message, Toast.LENGTH_SHORT).show();
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
