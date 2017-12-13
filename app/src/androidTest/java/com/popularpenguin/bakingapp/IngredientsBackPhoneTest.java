package com.popularpenguin.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.runner.AndroidJUnit4;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.popularpenguin.bakingapp.data.Recipe;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/** Check that the back button navigates from IngredientsFragment to RecipeFragment
 * PHONES ONLY */
@RunWith(AndroidJUnit4.class)
public class IngredientsBackPhoneTest {

    // From the Fragment Test Rule external library
    @Rule
    public FragmentTestRule<RecipeActivity, RecipeFragment> mFragRule =
            new FragmentTestRule<RecipeActivity, RecipeFragment>(RecipeActivity.class, RecipeFragment.class) {

        @Override
        protected Intent getActivityIntent() {
            Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Recipe recipe = new Recipe(-1, "mock");
            recipe.addIngredient("5", "CUP", "nuts");

            Intent intent = new Intent(ctx, RecipeActivity.class);
            intent.putExtra(MainActivity.RECIPE_EXTRA, recipe);

            return intent;
        }
    };

    @Test
    public void checkBackButton() {
        onView(TestUtils.first(withId(R.id.btn_ingredients)))
                .perform(click());

        onView(TestUtils.first(withId(R.id.tv_ingredients_header)))
                .check(matches(withText(R.string.text_ingredients)));

        Espresso.pressBack();

        onView(TestUtils.first(withId(R.id.tv_step_header)))
                .check(matches(withText(R.string.text_select_step)));
    }
}
