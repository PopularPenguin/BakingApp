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

/** Test the previous and next buttons on a phone in the RecipeFragment
 * FOR PHONES ONLY, TABLETS DON'T HAVE THESE 2 BUTTONS */
@RunWith(AndroidJUnit4.class)
public class StepButtonNavigationPhoneTest {

    private Recipe mRecipe;

    @Rule
    public FragmentTestRule<StepActivity, InstructionsFragment> mRule =
            new FragmentTestRule<StepActivity, InstructionsFragment>
                    (StepActivity.class, InstructionsFragment.class) {

                @Override
                protected Intent getActivityIntent() {
                    Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    mRecipe = createTestRecipe();

                    Bundle args = new Bundle();
                    args.putParcelable(MainActivity.RECIPE_EXTRA, mRecipe);
                    args.putInt(RecipeActivity.INDEX_EXTRA, 0);

                    Intent intent = new Intent(ctx, StepActivity.class);
                    intent.putExtra(RecipeActivity.BUNDLE_EXTRA, args);

                    return intent;
                }
            };

    // simple Recipe object that just has 10 steps (named 0 - 9)
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

    /** Click the next button 9 times and check the step text
     * then click the previous button until back at step 0 */
    @Test
    public void testNavigationButtons() {
        // check the step's text and then click the next button to go to the next step
        for (int i = 0; i < 9; i++) {
            onView(TestUtils.first(withId(R.id.tv_instructions)))
                    .check(matches(withText(String.valueOf(i))));

            onView(TestUtils.first(withId(R.id.btn_next)))
                    .perform(click());
        }

        // check TextView for last step
        onView(TestUtils.first(withId(R.id.tv_instructions)))
                .check(matches(withText(String.valueOf(mRecipe.getSteps().size() - 1))));

        // reverse through the steps by clicking previous and checking each step's TextView
        for (int i = 9; i >= 0; i--) {
            onView(TestUtils.first(withId(R.id.tv_instructions)))
                    .check(matches(withText(String.valueOf(i))));

            onView(TestUtils.first(withId(R.id.btn_previous)))
                    .perform(click());
        }
    }
}
