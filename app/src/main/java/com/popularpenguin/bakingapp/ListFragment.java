package com.popularpenguin.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.popularpenguin.bakingapp.Controller.RecipeListAdapter;
import com.popularpenguin.bakingapp.Controller.RecipeLoader;
import com.popularpenguin.bakingapp.Data.Ingredients;
import com.popularpenguin.bakingapp.Data.Recipe;
import com.popularpenguin.bakingapp.Data.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment implements
        RecipeListAdapter.RecipeListAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Recipe>> {

    private static final String TAG = ListFragment.class.getSimpleName();

    @BindView(R.id.rv_list) RecyclerView mRecyclerView;

    private RecipeListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        getActivity().getSupportLoaderManager().initLoader(0, null, this);

        return view;
    }

    private void setupRecyclerView() {
        mAdapter = new RecipeListAdapter(getContext(), mRecipeList, this);
        mRecyclerView.setAdapter(mAdapter);

        // TODO: Set to a grid layout for tablets

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
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
