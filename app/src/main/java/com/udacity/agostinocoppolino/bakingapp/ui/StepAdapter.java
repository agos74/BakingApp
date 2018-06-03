package com.udacity.agostinocoppolino.bakingapp.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder> {

    private static final int VIEW_TYPE_TOP = 0;
    private static final int VIEW_TYPE_MIDDLE = 1;
    private static final int VIEW_TYPE_BOTTOM = 2;

    // Keeps track of the context and list of steps to display
    private final Context mContext;
    private final List<Step> mStepsList;

    /**
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final StepAdapterOnClickHandler mClickHandler;

    private final boolean isTablet;
    private final int mStepSelected;

    /**
     * The interface that receives onClick messages.
     */
    public interface StepAdapterOnClickHandler {
        void onClick(int stepIndex, View currentSelectedView, ViewGroup mListView);
    }


    public StepAdapter(Context context, List<Step> stepsList, StepAdapterOnClickHandler mClickHandler, boolean isTablet, int stepSelected) {
        this.mContext = context;
        this.mStepsList = stepsList;
        this.mClickHandler = mClickHandler;
        this.isTablet = isTablet;
        this.mStepSelected = stepSelected;
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
        return new StepAdapterViewHolder(view, parent);
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

        holder.mStepShortDescriptionTextView.setText((position) + " - " + stepForThisPosition.getShortDescription());

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_TOP:
                // The top of the line has to be rounded
                holder.mItemLine.setBackground(holder.mItemLine.getResources().getDrawable(R.drawable.line_bg_top));
                break;
            case VIEW_TYPE_MIDDLE:
                // Only the color could be enough
                // but a drawable can be used to make the cap rounded also here
                holder.mItemLine.setBackground(holder.mItemLine.getResources().getDrawable(R.drawable.line_bg_middle));
                break;
            case VIEW_TYPE_BOTTOM:
                holder.mItemLine.setBackground(holder.mItemLine.getResources().getDrawable(R.drawable.line_bg_bottom));
                break;
        }

        if (isTablet) {
            if (mStepSelected == position) {
                holder.mStepSelectedCardView.setVisibility(View.VISIBLE);
            } else {
                holder.mStepSelectedCardView.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_TOP;
        } else if (position == mStepsList.size() - 1) {
            return VIEW_TYPE_BOTTOM;
        }
        return VIEW_TYPE_MIDDLE;
    }

    @Override
    public int getItemCount() {
        return (mStepsList == null) ? 0 : mStepsList.size();
    }

    public class StepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_short_description)
        TextView mStepShortDescriptionTextView;

        @BindView(R.id.item_line)
        FrameLayout mItemLine;

        @BindView(R.id.cardview_step_selected)
        CardView mStepSelectedCardView;

        private ViewGroup mListView;

        public StepAdapterViewHolder(View itemView, ViewGroup listView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mListView = listView;
        }

        @Override
        public void onClick(View currentSelectedView) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition, currentSelectedView, mListView);
        }
    }


}
