package com.popularpenguin.bakingapp;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

class TestUtils {

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
}
