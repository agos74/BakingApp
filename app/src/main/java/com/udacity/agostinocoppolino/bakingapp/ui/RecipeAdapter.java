package com.udacity.agostinocoppolino.bakingapp.ui;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.agostinocoppolino.bakingapp.R;
import com.udacity.agostinocoppolino.bakingapp.model.Recipe;
import com.udacity.agostinocoppolino.bakingapp.utils.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private List<Recipe> mRecipesList;

    /**
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final RecipeAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    /**
     * Creates a RecipeAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public RecipeAdapter(RecipeAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @return A new RecipeAdapterViewHolder that holds the View for each list item
     */
    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new RecipeAdapterViewHolder(view);
    }


    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the poster
     * for this particular position, using the "position" argument that is conveniently
     * passed into us.
     */
    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder holder, int position) {
        Recipe recipeForThisPosition = mRecipesList.get(position);

        Uri imgUri = ImageUtils.getImage(recipeForThisPosition, holder.mRecipeImageView.getContext());
        if (recipeForThisPosition.getImage().equals("")) { //image not available, get drawable ResourceId
            int imageResourceId = Integer.valueOf(imgUri.toString());
            Picasso.with(holder.mRecipeImageView.getContext()).load(imageResourceId)
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.drawable.progress_animation)
                    .into(holder.mRecipeImageView);
        } else {
            Picasso.with(holder.mRecipeImageView.getContext()).load(imgUri)
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.drawable.progress_animation)
                    .into(holder.mRecipeImageView);
        }

        holder.mRecipeImageView.setContentDescription(recipeForThisPosition.getName());
        holder.mRecipeNameTextView.setText(recipeForThisPosition.getName());
        String servingText = holder.mRecipeImageView.getContext().getString(R.string.servings_text_with_placeholder, String.valueOf(recipeForThisPosition.getServings()));
        holder.mRecipeServingsTextView.setText(servingText);
    }

    @Override
    public int getItemCount() {
        return (mRecipesList == null) ? 0 : mRecipesList.size();
    }

    /**
     * Cache of the children views for a recipe grid item.
     */
    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_recipe)
        ImageView mRecipeImageView;

        @BindView(R.id.tv_recipe_name)
        TextView mRecipeNameTextView;

        @BindView(R.id.tv_recipe_servings)
        TextView mRecipeServingsTextView;

        RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = mRecipesList.get(adapterPosition);
            mClickHandler.onClick(recipe);
        }
    }

    /**
     * This method is used to set the recipe on a RecipeAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new RecipeAdapter to display it.
     *
     * @param recipesList The new recipes list to be displayed.
     */
    public void setRecipesList(List<Recipe> recipesList) {
        mRecipesList = recipesList;
        notifyDataSetChanged();
    }

}

