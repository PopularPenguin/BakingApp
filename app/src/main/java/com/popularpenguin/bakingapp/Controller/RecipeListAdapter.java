package com.popularpenguin.bakingapp.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.popularpenguin.bakingapp.Data.Recipe;
import com.popularpenguin.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ListViewHolder> {

    private static final String TAG = RecipeListAdapter.class.getSimpleName();

    private final RecipeListAdapterOnClickHandler mClickHandler;

    public interface RecipeListAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    private final Context ctx;
    private final List<Recipe> mRecipeList;

    public RecipeListAdapter(Context ctx,
                             List<Recipe> recipeList,
                             RecipeListAdapterOnClickHandler handler) {

        this.ctx = ctx;
        mRecipeList = recipeList;
        mClickHandler = handler;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.list_item;

        LayoutInflater inflater = LayoutInflater.from(ctx);

        View view = inflater.inflate(layout, parent, false);

        return new ListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecipeListAdapter.ListViewHolder holder, int position) {
        Recipe recipe = mRecipeList.get(position);

        holder.bind(recipe);
    }

    @Override
    public int getItemCount() { return mRecipeList.size(); }

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_list_item) TextView mItemText;

        ListViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            ButterKnife.bind(this, itemView);
        }

        void bind(Recipe recipe) {
            mItemText.setText(recipe.getName());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            mClickHandler.onClick(mRecipeList.get(position));
        }
    }
}
