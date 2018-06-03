package com.udacity.agostinocoppolino.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.agostinocoppolino.bakingapp.Constants;
import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// This fragment displays all of the steps in one list

public class StepsListFragment extends Fragment implements StepAdapter.StepAdapterOnClickHandler {

    private static final String STEPS_LIST_KEY = "steps_list";
    private static final String RECIPE_NAME_KEY = "recipe_name";
    private static final String CURRENT_STEP_KEY = "current_step";
    private static final String FIRST_TIME_STARTUP_KEY = "first_time_startup";

    //ButterKnife Binding
    @BindView(R.id.recyclerview_steps)
    RecyclerView mStepRecyclerView;

    @BindView(R.id.tv_steps_count)
    TextView mStepsCountTextView;

    private List<Step> mStepsList;

    private String mRecipeName;

    private int mCurrentStep = 0;

    private View mCurrentSelectedView;
    private boolean mFirstTimeStartup = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Load the saved state (the list of steps) if there is one
        if (savedInstanceState != null) {
            mStepsList = savedInstanceState.getParcelableArrayList(STEPS_LIST_KEY);
            mRecipeName = savedInstanceState.getString(RECIPE_NAME_KEY);
            mCurrentStep = savedInstanceState.getInt(CURRENT_STEP_KEY);
            mFirstTimeStartup = savedInstanceState.getBoolean(FIRST_TIME_STARTUP_KEY);
        }

        final View view = inflater.inflate(R.layout.fragment_steps_list, container, false);

        ButterKnife.bind(this, view);

        if (isTablet()) {
            mStepsCountTextView.setVisibility(View.VISIBLE);
            updateStepsCount();
        }

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mStepRecyclerView.setLayoutManager(horizontalLayoutManager);

        // Create the adapter
        // This adapter takes in the context and an ArrayList of Step to display
        StepAdapter mStepAdapter = new StepAdapter(getContext(), mStepsList, this, isTablet(), mCurrentStep);

        // Set the adapter on the RecyclerView
        mStepRecyclerView.setAdapter(mStepAdapter);

        return view;
    }

    public void setStepsList(List<Step> stepsList) {
        mStepsList = stepsList;
    }

    public void setRecipeName(String recipeName) {
        mRecipeName = recipeName;
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList(STEPS_LIST_KEY, (ArrayList<? extends Parcelable>) mStepsList);
        currentState.putString(RECIPE_NAME_KEY, mRecipeName);
        currentState.putInt(CURRENT_STEP_KEY, mCurrentStep);
        currentState.putBoolean(FIRST_TIME_STARTUP_KEY, mFirstTimeStartup);
    }

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param stepIndex The index of the step that was clicked
     * @param v         The current selected View
     * @param listView
     */

    public void onClick(int stepIndex, View v, ViewGroup listView) {

        // Determine if you're creating a two-pane or single-pane display

        // This FrameLayout will only initially exist in the two-pane tablet case
        if (isTablet()) {

            mCurrentStep = stepIndex;

            updateStepsCount();

            // Highlight current step selected
            if (mFirstTimeStartup) {// first time, highlight first step
                mCurrentSelectedView = listView.getChildAt(0);
                mFirstTimeStartup = false;
            } else {
                //reset steps selected state
                resetSelected(listView);
            }
            if (mCurrentSelectedView != null && mCurrentSelectedView != v) {
                mCurrentSelectedView.findViewById(R.id.iv_step_selected).setVisibility(View.INVISIBLE);
                mCurrentSelectedView.findViewById(R.id.item_circle).setBackground(getResources().getDrawable(R.drawable.ic_circle_black_24dp));
            }
            mCurrentSelectedView = v;
            mCurrentSelectedView.findViewById(R.id.iv_step_selected).setVisibility(View.VISIBLE);
            mCurrentSelectedView.findViewById(R.id.item_circle).setBackground(getResources().getDrawable(R.drawable.ic_circle_black_full_24dp));


            // Replace the fragment with the new step selected

            // Create a new stepFragment
            StepFragment stepFragment = new StepFragment();

            // Set StepIndex and StepsList for the fragment
            stepFragment.setStepsList(mStepsList);
            stepFragment.setStepIndex(stepIndex);

            this.getFragmentManager().beginTransaction()
                    .replace(R.id.step_container, stepFragment)
                    .commit();

        } else { //Phone
            // Launch the StepActivity using an explicit Intent
            Intent intentToStartStepActivity = new Intent(this.getContext(), StepActivity.class);
            intentToStartStepActivity.putExtra(Constants.EXTRA_STEP_INDEX_KEY, stepIndex);
            intentToStartStepActivity.putExtra(Constants.EXTRA_RECIPE_NAME_KEY, mRecipeName);
            intentToStartStepActivity.putParcelableArrayListExtra("StepsList", (ArrayList<? extends Parcelable>) mStepsList);
            startActivity(intentToStartStepActivity);
        }

    }

    private void resetSelected(ViewGroup listView) {
        int count = listView.getChildCount();
        // Iterate through all children, resetting selected state
        for (int i = 0; i < count; i++) {
            final View child = listView.getChildAt(i);
            child.findViewById(R.id.iv_step_selected).setVisibility(View.INVISIBLE);
            child.findViewById(R.id.item_circle).setBackground(getResources().getDrawable(R.drawable.ic_circle_black_24dp));
        }
    }


    private void updateStepsCount() {
        String stepsCountText = mStepsCountTextView.getResources().getString(R.string.steps_count_with_placeholder, String.valueOf(mCurrentStep), String.valueOf(mStepsList.size() - 1));
        mStepsCountTextView.setText(stepsCountText);
    }

    private boolean isTablet() {
        return this.getActivity().findViewById(R.id.step_container) != null;
    }

}
