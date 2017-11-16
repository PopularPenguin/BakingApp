package com.popularpenguin.bakingapp;

import android.content.Intent;
import android.os.Bundle;
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
import com.popularpenguin.bakingapp.Data.Ingredients;
import com.popularpenguin.bakingapp.Data.Recipe;
import com.popularpenguin.bakingapp.Data.Step;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/** This class handles the recipe's ingredients and steps list */
public class RecipeFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final String TAG = RecipeFragment.class.getSimpleName();

    @BindView(R.id.rv_recipe) RecyclerView mRecyclerView;

    private RecipeAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        ButterKnife.bind(this, view);

        // TODO: Mock a recipe, add a list of steps and video urls to display
        // just display the video urls for now and the mocked instruction steps
        Recipe recipe = new Recipe(0, "My recipe");

        Ingredients in1 = new Ingredients("1", "C", "Cocoa");
        Ingredients in2 = new Ingredients("20", "C", "Nuts");
        Step s1 = new Step(0, "Ingredients", "List of ingredients...",
                "", "");
        Step s2 = new Step(1, "Step 1", "Description 1...",
                "", "");
        Step s3 = new Step(2, "Step 2", "Description 2...",
                "", "");

        recipe.addIngredient(in1, in2);
        recipe.addStep(s1, s2, s3);

        mAdapter = new RecipeAdapter(getContext(), recipe, this);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        return view;
    }


    @Override
    public void onClick(String video, String instructions) {
        // TODO: Display the video and the instructions in the instructions fragment
        // for a phone, need to use the fragmentManager to swap the currently displayed fragment

        // TODO: Pass the selection back to RecipeActivity for the FragmentManager to handle


        Toast.makeText(getContext(), instructions, Toast.LENGTH_SHORT).show();
    }
}
