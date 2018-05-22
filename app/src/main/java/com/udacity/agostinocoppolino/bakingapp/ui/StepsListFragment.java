package com.udacity.agostinocoppolino.bakingapp.ui;

import android.content.Context;
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

import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// This fragment displays all of the steps in one list

public class StepsListFragment extends Fragment implements StepAdapter.StepAdapterOnClickHandler {

    private static final String STEPS_LIST = "steps_list";

    //ButterKnife Binding
    @BindView(R.id.recyclerview_steps)
    RecyclerView mStepRecyclerView;

    private List<Step> mStepsList;

    private String mRecipeName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Load the saved state (the list of steps) if there is one
        if (savedInstanceState != null) {
            mStepsList = savedInstanceState.getParcelableArrayList(STEPS_LIST);
        }

        final View view = inflater.inflate(R.layout.fragment_steps_list, container, false);

        ButterKnife.bind(this, view);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mStepRecyclerView.setLayoutManager(horizontalLayoutManager);

        // Create the adapter
        // This adapter takes in the context and an ArrayList of Step to display
        StepAdapter mStepAdapter = new StepAdapter(getContext(), mStepsList, this);

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
        currentState.putParcelableArrayList(STEPS_LIST, (ArrayList<? extends Parcelable>) mStepsList);
    }

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param stepIndex The index of the step that was clicked
     */

    public void onClick(int stepIndex) {
        Context context = this.getContext();
        // Launch the StepActivity using an explicit Intent
        Class destinationClass = StepActivity.class;
        Intent intentToStartStepActivity = new Intent(context, destinationClass);
        intentToStartStepActivity.putExtra("StepIndex", stepIndex);
        intentToStartStepActivity.putExtra("RecipeName", mRecipeName);
        intentToStartStepActivity.putParcelableArrayListExtra("StepsList", (ArrayList<? extends Parcelable>) mStepsList);
        startActivity(intentToStartStepActivity);
    }
}
