package com.udacity.agostinocoppolino.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Step;
import com.udacity.agostinocoppolino.bakingapp.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class NavigationFragment extends Fragment {

    @BindView(R.id.imageButton_prev)
    ImageButton mPrevImageButton;

    @BindView(R.id.imageButton_next)
    ImageButton mNextImageButton;

    @BindView(R.id.tv_navigation)
    TextView mNavigationTextView;

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
    public NavigationFragment() {
    }

    OnStepSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnStepSelectedListener {
        void onStepSelected(int stepIndex);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnStepSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Timber.d("On Create View");
        boolean firstTime = false;

        // Load the saved state if there is one
        if (savedInstanceState != null) {

            mStepsList = savedInstanceState.getParcelableArrayList(STEPS_LIST);
            mCurrentStep = savedInstanceState.getInt(CURRENT_STEP);

            firstTime = getArguments().getBoolean(Constants.FIRST_TIME_KEY, false);
        }

        // Inflate the Navigation fragment layout
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);

        ButterKnife.bind(this, view);

        populateStep(firstTime);

        return view;
    }

    @OnClick(R.id.imageButton_prev)
    public void prev() {
        if (mCurrentStep > 0) {
            mCurrentStep--;
            populateStep(false);
        }
    }

    @OnClick(R.id.imageButton_next)
    public void next() {
        if ((mStepsList.size() - 1) > mCurrentStep) {
            mCurrentStep++;
            populateStep(false);
        }
    }

    private void populateStep(boolean firstTime) {

        int maxSteps = mStepsList.size() - 1;
        boolean nextVisible = mCurrentStep < maxSteps;
        boolean prevVisible = mCurrentStep > 0;

        if (prevVisible) {
            mPrevImageButton.setVisibility(View.VISIBLE);
        } else {
            mPrevImageButton.setVisibility(View.INVISIBLE);
        }

        if (nextVisible) {
            mNextImageButton.setVisibility(View.VISIBLE);
        } else {
            mNextImageButton.setVisibility(View.INVISIBLE);
        }

        mNavigationTextView.setText(String.valueOf(mCurrentStep).concat(" of ").concat(String.valueOf(mStepsList.size() - 1)));

        if (!firstTime) {
            // Send the event to the host activity
            mCallback.onStepSelected(mCurrentStep);
        }
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList(STEPS_LIST, (ArrayList<? extends Parcelable>) mStepsList);
        currentState.putInt(CURRENT_STEP, mCurrentStep);
    }
}
