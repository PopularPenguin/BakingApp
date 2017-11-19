package com.popularpenguin.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.popularpenguin.bakingapp.Controller.RecipeListAdapter;
import com.popularpenguin.bakingapp.Data.Ingredients;
import com.popularpenguin.bakingapp.Data.Recipe;
import com.popularpenguin.bakingapp.Data.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment implements
        RecipeListAdapter.RecipeListAdapterOnClickHandler {

    private static final String TAG = ListFragment.class.getSimpleName();

    private final String RECIPE_EXTRA = "recipe";

    @BindView(R.id.rv_list) RecyclerView mRecyclerView;

    private RecipeListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Recipe> mRecipeList;

    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        ButterKnife.bind(this, view);

        mRecipeList = getTestList(); // TODO: Remove when networking is complete

        mAdapter = new RecipeListAdapter(getContext(), mRecipeList, this);
        mRecyclerView.setAdapter(mAdapter);

        // TODO: Set to a grid layout for tablets
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        return view;
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

    /** Implement in ListActivity to get the recipe to put in an intent */
    public interface OnRecipeSelectedListener {
        void onRecipeSelected(Recipe recipe);
    }

    private List<Recipe> getTestList() {
        // TODO: Remove mock list and replace with network call

        Recipe recipe1 = new Recipe(0, "My recipe");

        Ingredients in1 = new Ingredients("1", "C", "Cocoa");
        Ingredients in2 = new Ingredients("20", "C", "Nuts");
        Step s1 = new Step(0, "Ingredients", "List of ingredients...",
                "", "");
        Step s2 = new Step(1, "Step 1", "Description 1...",
                "", "");
        Step s3 = new Step(2, "Step 2", "Description 2...",
                "", "");

        recipe1.addIngredient(in1, in2);
        recipe1.addStep(s1, s2, s3);

        Recipe recipe2 = new Recipe(1, "Recipe 2");

        Ingredients in3 = new Ingredients("1", "oz", "Butter");
        Ingredients in4 = new Ingredients("10", "ml", "Milk");
        Step s4 = new Step(0, "Ingredients", "List of ingredients...",
                "", "");
        Step s5 = new Step(1, "Step 1 (Recipe 2)", "Desc 1...",
                "", "");

        recipe2.addIngredient(in3, in4);
        recipe2.addStep(s4, s5);

        List<Recipe> list = new ArrayList<>();
        list.add(recipe1);
        list.add(recipe2);

        return list;
    }
}
