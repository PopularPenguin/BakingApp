package com.popularpenguin.bakingapp.Controller;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.popularpenguin.bakingapp.Data.NetworkUtils;
import com.popularpenguin.bakingapp.Data.Recipe;

import java.util.List;

public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    private static final String TAG = RecipeLoader.class.getSimpleName();

    private List<Recipe> mRecipes;

    public RecipeLoader(Context ctx) { super(ctx); }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Recipe> loadInBackground() {
        return NetworkUtils.getRecipes(getContext());
    }
}
