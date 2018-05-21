package com.udacity.agostinocoppolino.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment implements ExoPlayer.EventListener {

    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;

    @BindView(R.id.tv_short_description)
    TextView mShortDescriptionTextView;

    @BindView(R.id.tv_description)
    TextView mDescriptionTextView;

    @BindView(R.id.imageButton_prev)
    ImageButton mPrevImageButton;

    @BindView(R.id.imageButton_next)
    ImageButton mNextImageButton;

    @BindView(R.id.tv_navigation)
    TextView mNavigationTextView;

    private Step mStep;

    private int mTotalSteps;

    public void setStep(Step step) {
        this.mStep = step;
    }

    public void setTotalSteps(int totalSteps) {
        this.mTotalSteps = totalSteps;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public StepFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Load the saved state (step) if there is one
        if (savedInstanceState != null) {
            mStep = savedInstanceState.getParcelable("Step");
        }

        // Inflate the Android-Me fragment layout
        View view = inflater.inflate(R.layout.fragment_step, container, false);

        ButterKnife.bind(this, view);

        mShortDescriptionTextView.setText(mStep.getShortDescription());
        mDescriptionTextView.setText(mStep.getDescription());

        int currentStep = Integer.valueOf(mStep.getId()) + 1;
        mNavigationTextView.setText(String.valueOf(currentStep).concat("/").concat(String.valueOf(mTotalSteps)));

        return view;
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelable("Step", mStep);
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

