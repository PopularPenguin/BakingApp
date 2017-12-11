package com.popularpenguin.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.popularpenguin.bakingapp.controller.RecipeAdapter;
import com.popularpenguin.bakingapp.data.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/** This class handles the recipe's ingredients and steps list */
public class RecipeFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler,
    View.OnClickListener {

    private static final String TAG = RecipeFragment.class.getSimpleName();

    @BindView(R.id.btn_ingredients) Button mIngredients;
    @BindView(R.id.rv_recipe) RecyclerView mRecyclerView;

    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        ButterKnife.bind(this, view);

        Bundle args = getActivity().getIntent().getBundleExtra(RecipeActivity.BUNDLE_EXTRA);
        setData(args);

        mIngredients.setOnClickListener(this);

        return view;
    }

    private void setData(@NonNull Bundle args) {
        Recipe recipe;

        if (args.containsKey(MainActivity.RECIPE_EXTRA)) {
            recipe = args.getParcelable(MainActivity.RECIPE_EXTRA);
        }
        else {
            throw new RuntimeException("Recipe data was not passed to " + TAG);
        }

        RecipeAdapter adapter = new RecipeAdapter(getContext(), recipe, this);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    /** Notify the parent activity when a step is selected and pass the video URL and instructions */
    @Override
    public void onClick(int index) {
        try {
            ((OnStepSelectedListener) getActivity()).onStepSelected(index);
        }
        catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    /** For the button */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ingredients:
                Fragment fragment = new IngredientsFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment, RecipeActivity.INGREDIENTS_TAG)
                        .commit();

                break;

            default:
                throw new UnsupportedOperationException("Invalid view id");
        }
    }

    /** Implement this interface in the parent activity */
    public interface OnStepSelectedListener {
        void onStepSelected(int index);
    }
}
