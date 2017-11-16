package com.popularpenguin.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/** Recipe steps, video, and instructions */
public class RecipeActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        FragmentManager manager = getSupportFragmentManager();

        // TODO: Pass data between fragments here
        // TODO: Code to handle phone layout


    }

}
