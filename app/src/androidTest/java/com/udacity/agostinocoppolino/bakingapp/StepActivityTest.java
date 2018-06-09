package com.udacity.agostinocoppolino.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.agostinocoppolino.bakingapp.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class StepActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickOnFirstRecipe_ClickOnIntroductionStep_ClickNextStepButton() {

        // Open the first recipe
        onView(withId(R.id.recyclerview_main)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Open the first step
        onView(withId(R.id.recyclerview_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Check the step is zero
        onView((withId(R.id.tv_navigation))).check(matches(withText("0 of 6")));

        // Click on next step button
        onView((withId(R.id.imageButton_next))).perform(click());

        // Verify that the step button increments the steps by 1
        onView(withId(R.id.tv_navigation)).check(matches(withText("1 of 6")));

    }

}
