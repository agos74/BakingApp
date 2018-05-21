package com.udacity.agostinocoppolino.bakingapp.ui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder> {

    // Keeps track of the context and list of steps to display
    private final Context mContext;
    private final List<Step> mStepsList;

    /**
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final StepAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface StepAdapterOnClickHandler {
        void onClick(Step step);
    }


    public StepAdapter(Context context, List<Step> stepsList, StepAdapterOnClickHandler mClickHandler) {
        this.mContext = context;
        this.mStepsList = stepsList;
        this.mClickHandler = mClickHandler;
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
    public void onBindViewHolder(StepAdapter.StepAdapterViewHolder holder, final int position) {
        Step stepForThisPosition = mStepsList.get(position);

        holder.mStepShortDescriptionTextView.setText((position + 1) + " - " + stepForThisPosition.getShortDescription());

    }

    @Override
    public int getItemCount() {
        return (mStepsList == null) ? 0 : mStepsList.size();
    }

    public class StepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_short_description)
        TextView mStepShortDescriptionTextView;

        @BindView(R.id.cardview_step_item)
        CardView mStepItemCardView;

        public StepAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Step step = mStepsList.get(adapterPosition);
            mClickHandler.onClick(step);

        }
    }
}
