package com.popularpenguin.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/** This activity is for phones only */
public class StepActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "instructions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager manager = getSupportFragmentManager();

        if (manager.findFragmentByTag(FRAGMENT_TAG) == null) {
            InstructionsFragment fragment = new InstructionsFragment();

            manager.beginTransaction()
                    .add(R.id.fragment_container_step, fragment, FRAGMENT_TAG)
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
                NavUtils.navigateUpFromSameTask(this);

                return true;

            case R.id.action_recipe:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
