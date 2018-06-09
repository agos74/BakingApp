package com.udacity.agostinocoppolino.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import com.udacity.agostinocoppolino.bakingapp.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    private static final String STEP_INTRODUCTION_SHORT_DESCRIPTION = "Recipe Introduction";


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);


    @Test
    public void clickOnFirstRecipe_ClickOnIntroductionStep() {

        // Open the first recipe
        onView(withId(R.id.recyclerview_main)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Open the first step
        onView(withId(R.id.recyclerview_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Check if step short description match
        onView(withId(R.id.tv_short_description))
                .check(matches(withText(STEP_INTRODUCTION_SHORT_DESCRIPTION)));

        // Check if video is displayed
        onView(withId(R.id.playerView))
                .check(matches(isDisplayed()));
    }


}
