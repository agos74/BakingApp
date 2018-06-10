package com.udacity.agostinocoppolino.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.udacity.agostinocoppolino.bakingapp.ui.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final String RECIPE_NAME = "Cheesecake";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void loadRecipesAndClickOnFourth() {
        // Scroll to fourth recipe
        Espresso.onView(withId(R.id.recyclerview_main)).perform(RecyclerViewActions.scrollToPosition(3));
        // Open the fourth recipe
        Espresso.onView(withId(R.id.recyclerview_main)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        // Checks that the DetailActivity opens with the correct recipe name displayed on the toolbar
        matchToolbarTitle();

    }

    private static void matchToolbarTitle() {
        onView(isAssignableFrom(Toolbar.class))
                .check(matches(withToolbarTitle(is((CharSequence) MainActivityTest.RECIPE_NAME))));
    }

    private static Matcher<? super View> withToolbarTitle(final Matcher<CharSequence> charSequenceMatcher) {
        return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {
            @Override
            public boolean matchesSafely(Toolbar toolbar) {
                return charSequenceMatcher.matches(toolbar.getTitle());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                charSequenceMatcher.describeTo(description);
            }
        };
    }


}
