package com.udacity.agostinocoppolino.bakingapp.ui;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import timber.log.Timber;


public class StepFragment extends Fragment {

    private static final String KEY_STEPS_LIST = "steps_list";
    private static final String KEY_CURRENT_STEP = "current_step";
    private static final String KEY_POSITION = "position";
    private static final String KEY_CURRENT_WINDOW = "current_window";
    private static final String KEY_PLAY_WHEN_READY = "play_when_ready";

    @BindView(R.id.playerView)
    PlayerView mPlayerView;

    @BindView(R.id.iv_step)
    AppCompatImageView mStepImageView;

    @BindView(R.id.tv_short_description)
    TextView mShortDescriptionTextView;

    @BindView(R.id.tv_description)
    TextView mDescriptionTextView;

    @BindView(R.id.constraint_layout_step_fragment)
    ConstraintLayout mConstraintLayout;

    private List<Step> mStepsList;
    private int mCurrentStep;

    private SimpleExoPlayer mExoPlayer;
    private long mStartPosition;
    private int mCurrentWindow;
    private boolean mPlayWhenReady;

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

    private void clearStartPosition() {
        mCurrentWindow = 0;
        mStartPosition = 0;
        mPlayWhenReady = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the StepFragment layout
        View view = inflater.inflate(R.layout.fragment_step, container, false);

        ButterKnife.bind(this, view);

        Timber.d("OnCreateView START start position: ".concat(String.valueOf(mStartPosition)));
        if (savedInstanceState != null) {
            mStepsList = savedInstanceState.getParcelableArrayList(KEY_STEPS_LIST);
            mCurrentStep = savedInstanceState.getInt(KEY_CURRENT_STEP);
            mStartPosition = savedInstanceState.getLong(KEY_POSITION);
            mCurrentWindow = savedInstanceState.getInt(KEY_CURRENT_WINDOW);
            mPlayWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY);
        } else {
            clearStartPosition();
        }
        Timber.d("OnCreateView END start position: ".concat(String.valueOf(mStartPosition)));

        populateStep();

        return view;
    }

    /**
     * Initialize ExoPlayer.
     */
    private void initializePlayer() {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer

            Timber.d("InitializePlayer start position: ".concat(String.valueOf(mStartPosition)));

            DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this.getContext());
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(); //Provides estimates of the currently available bandwidth.
            AdaptiveTrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Prepare the MediaSource
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this.getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mStepsList.get(mCurrentStep).getVideoURL()));

            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(mCurrentWindow, mStartPosition);
            mExoPlayer.setPlayWhenReady(mPlayWhenReady);

        }
    }

    private void populateStep() {

        Timber.d("PopulateStep: " + mStartPosition);
        Step step = mStepsList.get(mCurrentStep);

        mShortDescriptionTextView.setText(step.getShortDescription());
        mDescriptionTextView.setText(step.getDescription());

        if (!step.getVideoURL().equals("")) {

            // Initialize the player
            initializePlayer();
            // Hide imageView
            mStepImageView.setVisibility(View.GONE);
        } else if (!step.getThumbnailURL().equals("")) {
            Picasso.with(mStepImageView.getContext()).load(step.getThumbnailURL())
                    .error(R.drawable.ic_launcher_foreground)
                    .placeholder(R.drawable.progress_animation)
                    .into(mStepImageView);

            mStepImageView.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.GONE);
        } else {
            // Load default image if no video and no thumbnail are present
            mStepImageView.setImageResource(R.drawable.ic_launcher_foreground);
            mStepImageView.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.GONE);
        }


    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList(KEY_STEPS_LIST, (ArrayList<? extends Parcelable>) mStepsList);
        currentState.putInt(KEY_CURRENT_STEP, mCurrentStep);
        if (mExoPlayer != null) {
            updateStartPosition();
            currentState.putLong(KEY_POSITION, mStartPosition);
            currentState.putInt(KEY_CURRENT_WINDOW, mCurrentWindow);
            currentState.putBoolean(KEY_PLAY_WHEN_READY, mPlayWhenReady);
        }
        Timber.d("OnSaveInstanceState start position: ".concat(String.valueOf(mStartPosition)));
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.d("Exoplayer onStart.");
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("Exoplayer onResume.");
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializePlayer();
            Timber.d("OnResume start position: ".concat(String.valueOf(mStartPosition)));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("Exoplayer onPause.");
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
        Timber.d("Exoplayer onStop.");
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            updateStartPosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void updateStartPosition() {
        mStartPosition = mExoPlayer.getContentPosition();
        mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
        mPlayWhenReady = mExoPlayer.getPlayWhenReady();
    }

    /**
     * Release the player when the fragment is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Timber.d("OnDestroyView start position: ".concat(String.valueOf(mStartPosition)));
        releasePlayer();
    }
}



