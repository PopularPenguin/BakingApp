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
import com.popularpenguin.bakingapp.Data.Step;

import java.util.ArrayList;

import static com.popularpenguin.bakingapp.MainActivity.RECIPE_EXTRA;

/**
 * Recipe steps, video, and instructions
 */
public class RecipeActivity extends AppCompatActivity implements
        RecipeFragment.OnStepSelectedListener {

    private static final String TAG = RecipeActivity.class.getSimpleName();

    public static final String INDEX_EXTRA = "index";
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

        isPhoneLayout = getResources().getBoolean(R.bool.isPhone);

        Bundle args = new Bundle();
        args.putParcelable(RECIPE_EXTRA, mRecipe);

        RecipeFragment fragment = RecipeFragment.newInstance(args);

        mFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }

    /**
     * Callback that passes the video URL and instructions to InstructionsFragment
     * Called from RecipeFragment
     */
    @Override
    public void onStepSelected(int index) {
        Step step = mRecipe.getSteps().get(index);
        Log.d(TAG, "url = " + step.getVideoURL());
        Log.d(TAG, "instructions = " + step.getDescription());

        Bundle args = new Bundle();
        args.putParcelable(MainActivity.RECIPE_EXTRA, mRecipe);
        args.putInt(INDEX_EXTRA, index);
        //args.putString(VIDEO_URL_EXTRA, videoURL);
        //args.putString(INSTRUCTIONS_EXTRA, instructions);

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
