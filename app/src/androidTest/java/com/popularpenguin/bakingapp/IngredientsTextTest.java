package com.popularpenguin.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.popularpenguin.bakingapp.data.Ingredients;
import com.popularpenguin.bakingapp.data.Recipe;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/** Test the Ingredients  */
@RunWith(AndroidJUnit4.class)
public class IngredientsTextTest {

    private Recipe mRecipe;

    // From the Fragment Test Rule external library
    @Rule
    public FragmentTestRule<RecipeActivity, RecipeFragment> mFragRule =
            new FragmentTestRule<RecipeActivity, RecipeFragment>(
                    RecipeActivity.class, RecipeFragment.class) {

                @Override
                protected Intent getActivityIntent() {
                    Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    mRecipe = new Recipe(-1, "mock");
                    mRecipe.addIngredient("5", "CUP", "nuts");

                    Intent intent = new Intent(ctx, RecipeActivity.class);
                    intent.putExtra(MainActivity.RECIPE_EXTRA, mRecipe);

                    return intent;
                }
            };

    /** Test to check whether navigation between the steps and ingredients fragments works */
    @Test
    public void clickIngredientsButton() {
        onView(TestUtils.first(withId(R.id.btn_ingredients)))
                .perform(click());

        onView(TestUtils.first(withId(R.id.tv_ingredients_header)))
                .check(matches(withText(R.string.text_ingredients)));

        onView(TestUtils.first(withId(R.id.btn_steps)))
                .perform(click());

        onView(TestUtils.first(withId(R.id.tv_step_header)))
                .check(matches(withText(R.string.text_select_step)));
    }

    /** Check if the ingredients text matches what is in the intent */
    @Test
    public void checkIngredients() {
        onView(TestUtils.first(withId(R.id.btn_ingredients)))
                .perform(click());

        Ingredients ingredient = mRecipe.getIngredients().get(0);

        String servingsString = String.format("(%s %s)%n%n", mRecipe.getServings(),
                mFragRule.getActivity().getResources().getString(R.string.text_servings));
        String quantityText = ingredient.getQuantity();
        String ingredientText = ingredient.getIngredient();
        String expected = String.format("%s%s %s%n%n", servingsString, quantityText, ingredientText);

        onView(TestUtils.first(withId(R.id.tv_ingredients)))
                .check(matches(withText(expected)));
    }
}
