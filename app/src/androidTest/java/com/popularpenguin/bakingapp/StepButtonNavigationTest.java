package com.popularpenguin.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.popularpenguin.bakingapp.data.Recipe;
import com.popularpenguin.bakingapp.data.Step;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/** Test the previous and next buttons on a phone in the RecipeFragment */
@RunWith(AndroidJUnit4.class)
public class StepButtonNavigationTest {

    private Recipe mRecipe;
    private Bundle args;

    // TODO: Fix args is null
    @Rule
    public FragmentTestRule<StepActivity, InstructionsFragment> mRule =
            new FragmentTestRule<StepActivity, InstructionsFragment>
                    (StepActivity.class, InstructionsFragment.class) {

                @Override
                protected Intent getActivityIntent() {
                    Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    mRecipe = createTestRecipe();

                    args = new Bundle();
                    args.putParcelable(MainActivity.RECIPE_EXTRA, mRecipe);
                    args.putInt(RecipeActivity.INDEX_EXTRA, 0);

                    Intent intent = new Intent(ctx, StepActivity.class);
                    intent.putExtra(RecipeActivity.BUNDLE_EXTRA, args);

                    return intent;
                }
            };

    public Recipe createTestRecipe() {
        // create a test recipe
        Recipe recipe = new Recipe(-1, "mock");

        // add 10 steps to the recipe for testing
        for (int i = 0; i < 10; i++) {
            Step step = new Step(i, String.valueOf(i), String.valueOf(i), "", "");
            recipe.addStep(step);
        }

        return recipe;
    }

    @Test
    public void testNextButton() {
        for (int i = 0; i < 10; i++) {
            onView(TestUtils.first(withId(R.id.tv_instructions)))
                    .check(matches(withText(String.valueOf(i))));

            onView(TestUtils.first(withId(R.id.btn_next)))
                    .perform(click());
        }
    }

    @Test
    public void testPreviousButton() {
        onView(TestUtils.first(withId(R.id.btn_previous)))
                .perform(click());
    }

}
