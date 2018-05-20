package com.udacity.agostinocoppolino.bakingapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder> {

    // Keeps track of the context and list of steps to display
    private final Context mContext;
    private final List<Step> mStepsList;

    public StepAdapter(Context context, List<Step> stepsList) {
        mContext = context;
        mStepsList = stepsList;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @return A new StepAdapterViewHolder that holds the View for each list item
     */
    @Override
    public StepAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.step_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new StepAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the step
     * for this particular position, using the "position" argument that is conveniently
     * passed into us.
     */
    @Override
    public void onBindViewHolder(StepAdapter.StepAdapterViewHolder holder, int position) {
        Step stepForThisPosition = mStepsList.get(position);

        holder.mStepShortDescriptionTextView.setText(stepForThisPosition.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return (mStepsList == null) ? 0 : mStepsList.size();
    }

    public class StepAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_short_description)
        TextView mStepShortDescriptionTextView;

        public StepAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
