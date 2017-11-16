package com.popularpenguin.bakingapp.Data;

import android.support.annotation.NonNull;

public class Ingredients {
    private String mQuantity;
    private String mIngredient;

    public Ingredients(@NonNull String quantity,
                       @NonNull String measure,
                       @NonNull String ingredient) {

        mQuantity = quantity + " " + measure;
        mIngredient = ingredient;
    }

    public String getQuantity() { return mQuantity; }
    public String getIngredient() { return mIngredient; }
}
