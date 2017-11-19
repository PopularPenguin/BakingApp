package com.popularpenguin.bakingapp;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.popularpenguin.bakingapp.Data.Recipe;

import static com.popularpenguin.bakingapp.MainActivity.RECIPE_EXTRA;

/** Recipe steps, video, and instructions */
public class RecipeActivity extends AppCompatActivity implements
        RecipeFragment.OnStepSelectedListener {

    private static final String TAG = RecipeActivity.class.getSimpleName();

    public static final String VIDEO_URL_EXTRA = "videoURL";
    public static final String INSTRUCTIONS_EXTRA = "instructions";
    public static final String BUNDLE_EXTRA = "bundle";

    private FragmentManager mFragmentManager;
    private Recipe mRecipe;
    private boolean isPhoneLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mRecipe = getIntent().getParcelableExtra(RECIPE_EXTRA);

        Log.d(TAG, mRecipe.getName());

        mFragmentManager = getSupportFragmentManager();

        if (mFragmentManager.findFragmentById(R.id.fragment_instructions) == null) {
            isPhoneLayout = true;
        }

        Bundle args = new Bundle();
        args.putParcelable(RECIPE_EXTRA , mRecipe);

        if (isPhoneLayout) {
            // we are on a phone so add the fragment and it's data in a Bundle
            RecipeFragment fragment = RecipeFragment.newInstance(args);

            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
        else {
            RecipeFragment fragment = (RecipeFragment)
                    mFragmentManager.findFragmentById(R.id.fragment_recipe);

            Recipe r = args.getParcelable(MainActivity.RECIPE_EXTRA);
            Log.d(TAG, "From bundle: " + r.getName());

            fragment.setData(args);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isPhoneLayout", isPhoneLayout);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        isPhoneLayout = savedInstanceState.getBoolean("isPhoneLayout", false);

        super.onRestoreInstanceState(savedInstanceState);
    }

    /** Callback that passes the video URL and instructions to InstructionsFragment
     * Called from RecipeFragment */
    @Override
    public void onStepSelected(String videoURL, String instructions) {
        Log.d(TAG, "url = " + videoURL);
        Log.d(TAG, "instructions = " + instructions);

        Bundle args = new Bundle();
        args.putString(VIDEO_URL_EXTRA, videoURL);
        args.putString(INSTRUCTIONS_EXTRA, instructions);

        // if there is no instructions fragment, we are on a phone, so replace the current fragment
        if (isPhoneLayout) {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putExtra(BUNDLE_EXTRA, args);
            startActivity(intent);
        }
        // tablet
        else {
            InstructionsFragment fragment = (InstructionsFragment)
                    mFragmentManager.findFragmentById(R.id.fragment_instructions);

            fragment.setData(args);
        }
    }
}
