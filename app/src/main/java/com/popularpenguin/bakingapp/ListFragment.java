package com.popularpenguin.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.popularpenguin.bakingapp.controller.RecipeListAdapter;
import com.popularpenguin.bakingapp.controller.RecipeLoader;
import com.popularpenguin.bakingapp.data.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment implements
        RecipeListAdapter.RecipeListAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Recipe>> {

    private static final String TAG = ListFragment.class.getSimpleName();

    public static final String RECIPES_UPDATED = "com.popularpenguin.bakingapp.RECIPES_UPDATED";

    public static final String RECIPE_LIST_EXTRA = "recipes";

    @BindView(R.id.rv_list) RecyclerView mRecyclerView;

    private List<Recipe> mRecipeList;

    /** Implement in MainActivity to get the recipe to put in an intent */
    public interface OnRecipeSelectedListener {
        void onRecipeSelected(Recipe recipe);
    }

    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        ButterKnife.bind(this, view);

        mRecipeList = new ArrayList<>();

        // START: Code to get rid of logcat error due to recycler view being initialized late
        // https://stackoverflow.com/questions/29141729/recyclerview-no-adapter-attached-skipping-layout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        }); // END code block

        getActivity().getSupportLoaderManager().restartLoader(0, null, this);

        return view;
    }

    private void setupRecyclerView() {
        RecipeListAdapter adapter = new RecipeListAdapter(getContext(), mRecipeList, this);
        RecyclerView.LayoutManager layoutManager;

        mRecyclerView.setAdapter(adapter);

        boolean isPhone = getContext().getResources().getBoolean(R.bool.isPhone);
        int orientation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        // use a linear layout if on a phone
        if (isPhone) {
            layoutManager = new LinearLayoutManager(getContext());
        }
        // use a 2 column grid layout if the tablet is in portrait or reverse portrait mode
        else if (orientation == Surface.ROTATION_0 || orientation == Surface.ROTATION_180) {
            layoutManager = new GridLayoutManager(getContext(), 2);
        }
        // tablet is in landscape or reverse landscape mode
        else {
            layoutManager = new GridLayoutManager(getContext(), 3);
        }

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onClick(Recipe recipe) {
        // pass the selected recipe back to the parent activity
        try {
            ((OnRecipeSelectedListener) getActivity()).onRecipeSelected(recipe);
        }
        catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    /** Loader callbacks */
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return new RecipeLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        if (data == null || data.isEmpty()) {
            Toast.makeText(getContext(), R.string.con_error, Toast.LENGTH_LONG).show();
        }
        else {
            mRecipeList = data;

            setupRecyclerView();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) { /* Not implemented */ }
}
