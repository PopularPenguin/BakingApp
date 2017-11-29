package com.popularpenguin.bakingapp.Data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Ingredients implements Parcelable {
    private final String mQuantity;
    private final String mIngredient;

    public Ingredients(@NonNull String quantity,
                       @NonNull String measure,
                       @NonNull String ingredient) {

        mQuantity = quantity + " " + measure;
        mIngredient = ingredient;
    }

    private Ingredients(Parcel in) {
        mQuantity = in.readString();
        mIngredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mQuantity);
        dest.writeString(mIngredient);
    }

    static final Parcelable.Creator<Ingredients> CREATOR =
            new Parcelable.Creator<Ingredients>() {

        @Override
        public Ingredients createFromParcel(Parcel source) {
            return new Ingredients(source);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };

    public String getQuantity() { return mQuantity; }
    public String getIngredient() { return mIngredient; }
}
