package com.popularpenguin.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/** This activity is for phones only */
public class StepActivity extends AppCompatActivity {

    private static final String TAG = StepActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        Bundle args = new Bundle();
        Intent intent = getIntent();
        if (intent.hasExtra(RecipeActivity.BUNDLE_EXTRA)) {
            args = intent.getBundleExtra(RecipeActivity.BUNDLE_EXTRA);
        }

        InstructionsFragment fragment = InstructionsFragment.newInstance(args);

        FragmentManager manager = getSupportFragmentManager();

        manager.beginTransaction()
                .replace(R.id.fragment_container_step, fragment)
                .commit();
    }
}
