package com.udacity.agostinocoppolino.bakingapp.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class StepFragment extends Fragment implements ExoPlayer.EventListener {

    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;

    @BindView(R.id.tv_short_description)
    TextView mShortDescriptionTextView;

    @BindView(R.id.tv_description)
    TextView mDescriptionTextView;

    private static final String STEPS_LIST = "steps_list";
    private static final String CURRENT_STEP = "current_step";

    private List<Step> mStepsList;
    private int mCurrentStep;

    public void setStepsList(List<Step> stepsList) {
        this.mStepsList = stepsList;
    }

    public void setStepIndex(int stepIndex) {
        this.mCurrentStep = stepIndex;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public StepFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Load the saved state if there is one
        if (savedInstanceState != null) {
            mStepsList = savedInstanceState.getParcelableArrayList(STEPS_LIST);
            mCurrentStep = savedInstanceState.getInt(CURRENT_STEP);
        }

        // Inflate the Android-Me fragment layout
        View view = inflater.inflate(R.layout.fragment_step, container, false);

        ButterKnife.bind(this, view);

        populateStep();

        return view;
    }

    private void populateStep() {

        Step step = mStepsList.get(mCurrentStep);

        mShortDescriptionTextView.setText(step.getShortDescription());
        mDescriptionTextView.setText(step.getDescription());

    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList(STEPS_LIST, (ArrayList<? extends Parcelable>) mStepsList);
        currentState.putInt(CURRENT_STEP, mCurrentStep);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

}

