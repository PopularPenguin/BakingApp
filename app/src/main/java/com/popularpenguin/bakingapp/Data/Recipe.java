package com.popularpenguin.bakingapp.Data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Recipe {
    private int mId;
    private String mName;
    private List<Ingredients> mIngredients;
    private List<Step> mSteps;

    public Recipe(int id, @NonNull String name) {
        mId = id;
        mName = name;
        mIngredients = new ArrayList<>();
        mSteps = new ArrayList<>();
    }

    public int getId() { return mId; }

    public String getName() { return mName; }

    public List<Ingredients> getIngredients() { return new ArrayList<>(mIngredients); }
    public void addIngredient(@NonNull Ingredients... ingredients) {
        Collections.addAll(mIngredients, ingredients);
    }
    public void addIngredient(@NonNull String quantity,
                              @NonNull String measure,
                              @NonNull String ingredient) {

        Ingredients ingredients = new Ingredients(quantity, measure, ingredient);
        mIngredients.add(ingredients);
    }

    public List<Step> getSteps() { return new ArrayList<>(mSteps); }
    public void addStep(@NonNull Step... step) { Collections.addAll(mSteps, step); }
    public void addStep(int id,
                        @NonNull String shortDescription,
                        @NonNull String description,
                        @NonNull String videoURL,
                        @NonNull String thumbnailURL) {

        Step step = new Step(id, shortDescription, description, videoURL, thumbnailURL);
        mSteps.add(step);
    }

    @Override
    public String toString() {
        return mName + getIngredients().get(0).getIngredient();
    }
}
