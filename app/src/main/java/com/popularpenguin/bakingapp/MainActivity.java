package com.popularpenguin.bakingapp;

import android.content.Intent;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.popularpenguin.bakingapp.Data.Recipe;
import com.popularpenguin.bakingapp.Data.SimpleIdlingResource;

public class MainActivity extends AppCompatActivity implements
        ListFragment.OnRecipeSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String RECIPE_EXTRA = "recipe";

    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mIdlingResource = (SimpleIdlingResource) getIdlingResource();
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RECIPE_EXTRA, recipe);

        Log.d(TAG, recipe.getName());

        startActivity(intent);
    }

    @VisibleForTesting
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }

        return mIdlingResource;
    }
}
