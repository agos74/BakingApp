package com.udacity.agostinocoppolino.bakingapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Step;
import com.udacity.agostinocoppolino.bakingapp.utils.Constants;

import java.util.List;

import butterknife.ButterKnife;
import timber.log.Timber;


public class StepActivity extends AppCompatActivity implements NavigationFragment.OnStepSelectedListener {

    private List<Step> mStepsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        Timber.d("On Create");

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

        checkFullScreen();

        int stepIndex = intent != null ? intent.getExtras().getInt("StepIndex") : -1;

        String recipeName = intent.getExtras().getString("RecipeName");

        mStepsList = intent.getParcelableArrayListExtra("StepsList");

        Timber.d(mStepsList.toString());

        Timber.d("savedinstancestate: ".concat(savedInstanceState == null ? "null" : "created"));

        if (stepIndex != -1) {
            this.setTitle(recipeName);

            // Only create new fragments when there is no previously saved state
            if (savedInstanceState == null) {

                // Add the fragment to its container using a FragmentManager and a Transaction
                FragmentManager fragmentManager = getSupportFragmentManager();

                StepFragment stepFragment = (StepFragment) fragmentManager.findFragmentByTag("STEP_FRAGMENT");

                if (stepFragment == null) {
                    Timber.d("stepFragment null, recreated");

                    // Create a new stepFragment
                    stepFragment = new StepFragment();
                    // Set StepIndex and StepsList for the fragment
                    stepFragment.setStepsList(mStepsList);
                    stepFragment.setStepIndex(stepIndex);

                    fragmentManager.beginTransaction()
                            .add(R.id.step_container, stepFragment, "STEP_FRAGMENT")
                            .commit();
                }

                // Create a new navigationFragment
                NavigationFragment navigationFragment = new NavigationFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.FIRST_TIME_KEY, true);
                navigationFragment.setArguments(bundle);
                // Set StepIndex and StepsList for the fragment
                navigationFragment.setStepsList(mStepsList);
                navigationFragment.setStepIndex(stepIndex);

                // Add the fragment to its container using a FragmentManager and a Transaction
                fragmentManager.beginTransaction()
                        .add(R.id.navigation_container, navigationFragment)
                        .commit();

            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkFullScreen();
        Timber.d("On Resume");
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


    @Override
    public void onStepSelected(int stepIndex) {
        // The user selected a step in navigation fragment the NavigationFragment
        // Do something here to display that step

        // Create a new stepFragment
        StepFragment stepFragment = new StepFragment();

        // Set StepIndex and StepsList for the fragment
        stepFragment.setStepsList(mStepsList);
        stepFragment.setStepIndex(stepIndex);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_container, stepFragment)
                .commit();

        Timber.d("stepFragment replaced from onStepSelected");

    }


    private void checkFullScreen() {
        // Checks the orientation of the screen
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUI();
            Timber.d("FullScreen mode in landscape");
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Timber.d("Portrait mode");

        }
    }

    private void hideSystemUI() {
        // Enables FullScreen mode.
        View decorView = this.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


}


