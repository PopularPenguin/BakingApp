package com.popularpenguin.bakingapp.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.popularpenguin.bakingapp.data.Recipe;
import com.popularpenguin.bakingapp.data.Step;
import com.popularpenguin.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private static final String TAG = RecipeAdapter.class.getSimpleName();

    private final RecipeAdapterOnClickHandler mClickHandler;

    public interface RecipeAdapterOnClickHandler {
        void onClick(int index);
    }

    private final Context ctx;
    private final Recipe mRecipe;

    public RecipeAdapter(Context ctx, Recipe recipe, RecipeAdapterOnClickHandler handler) {
        this.ctx = ctx;
        mRecipe = recipe;
        mClickHandler = handler;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.recipe_item;
        LayoutInflater inflater = LayoutInflater.from(ctx);

        View view = inflater.inflate(layout, parent, false);

        return new RecipeViewHolder(view);
    }

    // Bind the text of the step to the view holder
    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        List<Step> list = mRecipe.getSteps();
        String stepDescription = list.get(position).getShortDescription();

        holder.bindStep(stepDescription);
    }

    @Override
    public int getItemCount() { return mRecipe.getSteps().size(); }

    /** The ViewHolder for the adapter */
    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_recipe_steps) TextView stepName;

        RecipeViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            ButterKnife.bind(this, itemView);
        }

        void bindStep(String stepDescription) {
            Log.d(TAG, stepDescription);

            stepName.setText(stepDescription);
        }

        // Pass the text string to the handler's onClick method
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            mClickHandler.onClick(position);
        }
    }
}
