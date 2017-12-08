package com.popularpenguin.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.popularpenguin.bakingapp.data.Recipe;

import static com.popularpenguin.bakingapp.MainActivity.RECIPE_EXTRA;

/**
 * Recipe steps, video, and instructions
 */
public class RecipeActivity extends AppCompatActivity implements
        RecipeFragment.OnStepSelectedListener {

    private static final String TAG = RecipeActivity.class.getSimpleName();

    public static final String INDEX_EXTRA = "index";
    public static final String BUNDLE_EXTRA = "bundle";
    public static final String RECIPE_BROADCAST_EXTRA = "recipeBroadcast";

    private FragmentManager mFragmentManager;
    private Recipe mRecipe;
    private boolean isPhoneLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mRecipe = getIntent().getParcelableExtra(RECIPE_EXTRA);

        mFragmentManager = getSupportFragmentManager();

        isPhoneLayout = getResources().getBoolean(R.bool.isPhone);

        Bundle args = new Bundle();
        args.putParcelable(RECIPE_EXTRA, mRecipe);

        // don't add a fragment if there is already one inside the container
        Fragment containerFragment = mFragmentManager.findFragmentById(R.id.fragment_container);
        if (containerFragment == null) {
            RecipeFragment fragment = RecipeFragment.newInstance(args);

            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        // send the selected recipe to the widget
        broadcastRecipe();
    }

    /**
     * Callback that passes the video URL and instructions to InstructionsFragment
     * Called from RecipeFragment
     */
    @Override
    public void onStepSelected(int index) {
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.RECIPE_EXTRA, mRecipe);
        args.putInt(INDEX_EXTRA, index);

        // we are on a phone, so start StepActivity
        if (isPhoneLayout) {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putExtra(BUNDLE_EXTRA, args);
            startActivity(intent);
        }
        // tablet, replace InstructionsFragment
        else {
            InstructionsFragment fragment = InstructionsFragment.newInstance(args);

            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_step, fragment)
                    .commit();
        }
    }

    /** Send the broadcast for the widget */
    private void broadcastRecipe() {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(RECIPE_BROADCAST_EXTRA, mRecipe);

        sendBroadcast(intent);
    }
}
