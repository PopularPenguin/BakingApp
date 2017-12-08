package com.popularpenguin.bakingapp;

import android.app.ListActivity;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.android21buttons.fragmenttestrule.FragmentTestRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

// TODO: Make test(s) functional, add more
/** Test that all recipes display after being clicked */
@RunWith(AndroidJUnit4.class)
public class RecyclerListTest {

    @Rule
    public ActivityTestRule<MainActivity> mRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Before
    public void addFragment() {
        ListFragment fragment = new ListFragment();

        mRule.getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.list_container, fragment)
                .commit();
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
        onView(allOf(withId(R.id.rv_list)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position, click()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    static class MyViewAction {
        public static ViewAction clickChildViewId(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return "Click child view";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    View v = view.findViewById(id);
                    v.performClick();
                }
            };
        }
    }
}
