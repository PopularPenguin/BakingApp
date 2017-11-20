package com.popularpenguin.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.popularpenguin.bakingapp.Controller.RecipeAdapter;
import com.popularpenguin.bakingapp.Data.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/** This class handles the recipe's ingredients and steps list */
public class RecipeFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final String TAG = RecipeFragment.class.getSimpleName();

    @BindView(R.id.rv_recipe) RecyclerView mRecyclerView;

    public static RecipeFragment newInstance(@NonNull Bundle args) {
        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        setData(args);

        return view;
    }

    public void setData(@NonNull Bundle args) {
        if (args.containsKey(MainActivity.RECIPE_EXTRA)) {
            Recipe recipe = args.getParcelable(MainActivity.RECIPE_EXTRA);

            RecipeAdapter adapter = new RecipeAdapter(getContext(), recipe, this);
            mRecyclerView.setAdapter(adapter);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
        }
        else {
            throw new RuntimeException("Recipe data was not passed to " + TAG);
        }
    }

    /** Notify the parent activity when a step is selected and pass the video URL and instructions */
    @Override
    public void onClick(String videoURL, String instructions) {
        try {
            ((OnStepSelectedListener) getActivity()).onStepSelected(videoURL, instructions);
        }
        catch (ClassCastException e) {
            e.printStackTrace();
        }

        Toast.makeText(getContext(), instructions, Toast.LENGTH_SHORT).show();
    }

    /** Implement this interface in the parent activity */
    public interface OnStepSelectedListener {
        void onStepSelected(String videoURL, String instructions);
    }
}
