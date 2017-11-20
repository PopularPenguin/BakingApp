package com.popularpenguin.bakingapp.Data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Recipe implements Parcelable {
    private int mId;
    private String mName;
    private List<Ingredients> mIngredients;
    private List<Step> mSteps;

    public Recipe(int id, @NonNull String name) {
        this(id, name, new ArrayList<>(), new ArrayList<>());
    }

    public Recipe(int id,
                  @NonNull String name,
                  @NonNull List<Ingredients> ingredients,
                  @NonNull List<Step> steps) {

        mId = id;
        mName = name;
        mIngredients = ingredients;
        mSteps = steps;
    }

    private Recipe(Parcel in) {
        mId = in.readInt();
        mName = in.readString();

        Ingredients[] ingredients = in.createTypedArray(Ingredients.CREATOR);
        mIngredients = new ArrayList<>(Arrays.asList(ingredients));

        Step[] steps = in.createTypedArray(Step.CREATOR);
        mSteps = new ArrayList<>(Arrays.asList(steps));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);

        // from https://stackoverflow.com/questions/10071502/read-writing-arrays-of-parcelable-objects
        Ingredients[] ingredients = mIngredients.toArray(new Ingredients[mIngredients.size()]);
        dest.writeTypedArray(ingredients, 0);

        Step[] steps = mSteps.toArray(new Step[mSteps.size()]);
        dest.writeTypedArray(steps, 0);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) { return new Recipe[size]; }
    };

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
