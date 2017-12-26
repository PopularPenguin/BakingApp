package com.popularpenguin.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.popularpenguin.bakingapp.data.Recipe;

import static com.popularpenguin.bakingapp.MainActivity.RECIPE_EXTRA;

/**
 * Recipe steps, video, and instructions
 */
public class RecipeActivity extends AppCompatActivity implements
        RecipeFragment.OnStepSelectedListener {

    public static final String INDEX_EXTRA = "index";
    public static final String BUNDLE_EXTRA = "bundle";
    public static final String RECIPE_BROADCAST_EXTRA = "recipeBroadcast";

    public static final String INGREDIENTS_TAG = "IngredientsFragment";

    private FragmentManager mFragmentManager;
    private Recipe mRecipe;
    private boolean isPhoneLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(RECIPE_EXTRA);
        }
        else {
            mRecipe = getIntent().getParcelableExtra(RECIPE_EXTRA);
        }

        mFragmentManager = getSupportFragmentManager();

        isPhoneLayout = getResources().getBoolean(R.bool.isPhone);

        Bundle args = new Bundle();
        args.putParcelable(RECIPE_EXTRA, mRecipe);
        args.putInt(INDEX_EXTRA, 0);

        Intent intent = new Intent();
        intent.putExtra(RecipeActivity.BUNDLE_EXTRA, args);
        setIntent(intent);

        // don't add a fragment if there is already one inside the container
        Fragment containerFragment = mFragmentManager.findFragmentById(R.id.fragment_container);
        if (containerFragment == null) {
            RecipeFragment fragment = new RecipeFragment();

            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        // send the selected recipe to the widget
        broadcastRecipe();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE_EXTRA, mRecipe);

        super.onSaveInstanceState(outState);
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
            SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("index", index);
            editor.commit();

            Intent intent = new Intent(this, StepActivity.class);
            intent.putExtra(BUNDLE_EXTRA, args);
            startActivity(intent);
        }
        // tablet, replace InstructionsFragment
        else {
            Intent intent = new Intent();
            intent.putExtra(BUNDLE_EXTRA, args);
            setIntent(intent);

            InstructionsFragment fragment = new InstructionsFragment();

            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_step, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;

            case R.id.action_recipe:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /** If in IngredientFragment on a phone, back will bring you back to the RecipeFragment
     * otherwise without this method it would bring you back to MainActivity/ListFragment */
    @Override
    public void onBackPressed() {
        if (isPhoneLayout) {
            if (mFragmentManager.findFragmentByTag(INGREDIENTS_TAG) != null) {
                Fragment fragment = new RecipeFragment();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();

                return;
            }
        }

        super.onBackPressed();
    }

    /** Send the broadcast for the widget */
    private void broadcastRecipe() {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(RECIPE_BROADCAST_EXTRA, mRecipe);

        sendBroadcast(intent);
    }
}
