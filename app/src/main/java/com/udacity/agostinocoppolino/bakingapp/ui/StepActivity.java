package com.udacity.agostinocoppolino.bakingapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Step;
import com.udacity.agostinocoppolino.bakingapp.Constants;

import java.util.List;

import butterknife.ButterKnife;
import timber.log.Timber;


@SuppressWarnings("ConstantConditions")
public class StepActivity extends AppCompatActivity implements NavigationFragment.OnStepSelectedListener {

    private static final String IS_FULLSCREEN_KEY = "is_fullscreen";
    private static final String CURRENT_STEP_KEY = "current_step";

    private List<Step> mStepsList;
    private boolean isFullScreen;
    private int mCurrentStep;

    private GestureDetectorCompat mDetector;

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

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int stepIndex = intent != null ? intent.getExtras().getInt(Constants.EXTRA_STEP_INDEX_KEY) : -1;

        String recipeName = intent.getExtras().getString(Constants.EXTRA_RECIPE_NAME_KEY);

        mStepsList = intent.getParcelableArrayListExtra("StepsList");

        Timber.d(mStepsList.toString());

        Timber.d("savedinstancestate: ".concat(savedInstanceState == null ? "null" : "created"));

        if (stepIndex != -1) {
            this.setTitle(recipeName);

            isFullScreen = !TextUtils.isEmpty(mStepsList.get(stepIndex).getVideoURL());

            mCurrentStep = stepIndex;

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

                    checkFullScreen();

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

            } else {
                //restore isFullscreen boolean
                isFullScreen = savedInstanceState.getBoolean(IS_FULLSCREEN_KEY);
                mCurrentStep = savedInstanceState.getInt(CURRENT_STEP_KEY);

                checkFullScreen();

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

        isFullScreen = !TextUtils.isEmpty(mStepsList.get(stepIndex).getVideoURL());
        checkFullScreen();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_container, stepFragment)
                .commit();

        Timber.d("stepFragment replaced from onStepSelected");

        mCurrentStep = stepIndex;

    }


    private void checkFullScreen() {
        // Checks the orientation of the screen, activate fullscreen in landscape
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (isFullScreen) {
                hideSystemUI();
                Timber.d("FullScreen mode in landscape");
            }
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Timber.d("Portrait mode");
        }
    }

    private void hideSystemUI() {
        // Enables FullScreen mode.
        View decorView = this.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_FULLSCREEN_KEY, isFullScreen);
        outState.putInt(CURRENT_STEP_KEY, mCurrentStep);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    //Implements swipe left/right to change step
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return false;
        }

        private void onSwipeRight() {
            Timber.d("swipe Right");

            if (mCurrentStep > 0) {
                mCurrentStep--;
                // Send the event to the host activity
                onStepSelected(mCurrentStep);

                refreshNavigationUI();
            }

        }

        private void onSwipeLeft() {
            Timber.d("swipe Left");

            if ((mStepsList.size() - 1) > mCurrentStep) {
                mCurrentStep++;
                // Send the event to the host activity
                onStepSelected(mCurrentStep);

                refreshNavigationUI();
            }

        }

        private void refreshNavigationUI() {

            FragmentManager fragmentManager = getSupportFragmentManager();

            // Create a new navigationFragment
            NavigationFragment navigationFragment = new NavigationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.FIRST_TIME_KEY, false);
            navigationFragment.setArguments(bundle);
            // Set StepIndex and StepsList for the fragment
            navigationFragment.setStepsList(mStepsList);
            navigationFragment.setStepIndex(mCurrentStep);

            //Add the animation
//            fragmentManager.beginTransaction().setCustomAnimations(R.animator.enter_anim,
//                    R.animator.exit_anim);
            //end of animation

            // Replace the fragment to its container using a FragmentManager and a Transaction
            fragmentManager.beginTransaction()
                    .replace(R.id.navigation_container, navigationFragment)
                    .commit();

        }
    }
}


