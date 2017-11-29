package com.popularpenguin.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.popularpenguin.bakingapp.Data.Ingredients;
import com.popularpenguin.bakingapp.Data.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsFragment extends Fragment implements View.OnClickListener {

    @BindView((R.id.btn_steps)) Button mSteps;
    @BindView(R.id.tv_ingredients) TextView mIngredients;

    private Recipe mRecipe;

    public static IngredientsFragment newInstance(Bundle args) {
        IngredientsFragment fragment = new IngredientsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);

        ButterKnife.bind(this, view);

        mRecipe = getArguments().getParcelable(MainActivity.RECIPE_EXTRA);

        mSteps.setOnClickListener(this);

        setIngredientsText();

        return view;
    }

    /** Set the TextView to display the entire list of ingredients */
    private void setIngredientsText() {
        List<Ingredients> ingredients = mRecipe.getIngredients();

        for (Ingredients ingredient : ingredients) {
            String quantityText = ingredient.getQuantity();
            String ingredientText = ingredient.getIngredient();
            String displayText = String.format("%s %s%n%n", quantityText, ingredientText);

            mIngredients.append(displayText);
        }
    }

    /** Switch back to the Recipe step list when button is clicked */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_steps:
                Bundle args = getArguments();

                Fragment fragment = RecipeFragment.newInstance(args);

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();

                break;

            default:
                throw new UnsupportedOperationException("Invalid view id");
        }
    }
}
