package com.popularpenguin.bakingapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/** This activity is for phones only */
public class StepActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "instructions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        FragmentManager manager = getSupportFragmentManager();

        if (manager.findFragmentByTag(FRAGMENT_TAG) == null) {
            InstructionsFragment fragment = new InstructionsFragment();

            manager.beginTransaction()
                    .add(R.id.fragment_container_step, fragment, FRAGMENT_TAG)
                    .commit();
        }
    }
}
