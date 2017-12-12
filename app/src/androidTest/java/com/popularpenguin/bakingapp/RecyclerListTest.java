package com.popularpenguin.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android21buttons.fragmenttestrule.FragmentTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

// TODO: Check idling resources
/** Test that all recipes display after being clicked */
@RunWith(AndroidJUnit4.class)
public class RecyclerListTest {

    @Rule
    public FragmentTestRule<MainActivity, ListFragment> mRule =
            new FragmentTestRule<>(MainActivity.class, ListFragment.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mRule.getFragment().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }


    @Test
    public void clickFirstItem() { clickList(0); }

    @Test
    public void clickSecondItem() { clickList(1); }

    @Test
    public void clickThirdItem() { clickList(2); }

    @Test
    public void clickFourthItem() { clickList(3); }

    private void clickList(int position) {
        onView(TestUtils.first(withId(R.id.rv_list)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position, click()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
