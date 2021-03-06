package com.popularpenguin.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.popularpenguin.bakingapp.data.Recipe;

public class MainActivity extends AppCompatActivity implements
        ListFragment.OnRecipeSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String RECIPE_EXTRA = "recipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.list_container, new ListFragment())
                .commit();
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RECIPE_EXTRA, recipe);

        startActivity(intent);
    }
}
