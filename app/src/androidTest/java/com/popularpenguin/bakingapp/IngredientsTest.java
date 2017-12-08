package com.popularpenguin.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.popularpenguin.bakingapp.data.Recipe;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/** Test the navigation between RecipeFragment and IngredientsFragment */
@RunWith(AndroidJUnit4.class)
public class IngredientsTest {

    // From the Fragment Test Rule library
    @Rule
    public FragmentTestRule<RecipeActivity, RecipeFragment> mFragRule =
            new FragmentTestRule<RecipeActivity, RecipeFragment>(RecipeActivity.class, RecipeFragment.class) {

        @Override
        protected Intent getActivityIntent() {
            Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent(ctx, RecipeActivity.class);
            intent.putExtra(MainActivity.RECIPE_EXTRA, new Recipe(-1, "mock"));

            return intent;
        }
    };

    /** make a dummy recipe for the fragment argument as this data isn't under test */
    @Before
    public void setArgs() {
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.RECIPE_EXTRA, new Recipe(0, "mock"));

        mFragRule.getFragment().setArguments(args);
    }

    /** Test to check whether navigation between the steps and ingredients fragments works */
    @Test
    public void clickIngredientsButton() {
        onView(TestUtils.first(withId(R.id.btn_ingredients)))
                .perform(click());

        onView(TestUtils.first(withId(R.id.btn_steps)))
                .perform(click());
    }
}
