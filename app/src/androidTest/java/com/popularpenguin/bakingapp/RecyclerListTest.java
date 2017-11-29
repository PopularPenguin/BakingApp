package com.popularpenguin.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;


// TODO: Make test(s) functional, add more
/** Test that all recipes display after being clicked */
@RunWith(AndroidJUnit4.class)
public class RecyclerListTest {

    @Rule public ActivityTestRule<MainActivity> mRule =
            new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    /*
    @Test
    public void clickFirstItem() { clickList(0); }

    @Test
    public void clickSecondItem() { clickList(1); }

    @Test
    public void clickThirdItem() { clickList(2); } */

    @Test
    public void clickFourthItem() { clickList(3); }

    private void clickList(int position) {
        onData(instanceOf(ListFragment.class))
                .inAdapterView(withId(R.id.rv_list))
                .atPosition(position)
                .perform(click());
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
