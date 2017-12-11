package com.popularpenguin.bakingapp;

import android.content.res.Resources;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class TestUtils {

    // https://stackoverflow.com/questions/32387137/espresso-match-first-element-found-when-many-are-in-hierarchy
    static <T> Matcher<T> first(Matcher<T> matcher) {
        return new BaseMatcher<T>() {
            boolean isFirst = true;

            @Override
            public boolean matches(Object item) {
                if (isFirst && matcher.matches(item)) {
                    isFirst = false;
                    return true;
                }

                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("returns first matching item");
            }
        };
    }

    // TODO: Delete if not needed
    static void sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        }
        catch (InterruptedException e) {
            // ...
        }
    }

    // TODO: Delete if not needed
    static Matcher<View> withText(String expected) {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            private String expectedText = null;

            @Override
            public void describeTo(Description description) {
                description.appendText("String");
            }

            @Override
            public boolean matchesSafely(TextView textView) {
                if (this.expectedText == null) {
                        this.expectedText = expected;
                }
                if (this.expectedText != null) {
                    return expected.equals(textView.getText()
                            .toString());
                } else {
                    return false;
                }
            }
        };
    }
}
