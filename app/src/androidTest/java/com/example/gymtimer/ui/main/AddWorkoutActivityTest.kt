package com.example.gymtimer.ui.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import android.widget.NumberPicker
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import com.example.gymtimer.R
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Test

class AddWorkoutActivityTest {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        // Use actual SharedPreferences and clear data before each test
        sharedPreferences = context.getSharedPreferences("workouts_pref", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    @Test
    fun testAddWorkout_Success() {
        // Launch the activity
        val scenario = ActivityScenario.launch(AddWorkoutActivity::class.java)

        // Ensure the activity is resumed
        scenario.moveToState(Lifecycle.State.RESUMED)

        // Set workout name
        onView(withId(R.id.etWorkoutName)).perform(typeText("Morning Workout"), closeSoftKeyboard())

        // Add an exercise
        onView(withId(R.id.btnAddExercise)).perform(click())
        onView(withId(R.id.etExerciseName)).perform(typeText("Push Ups"), closeSoftKeyboard())
        onView(withId(R.id.minutePicker)).perform(setNumberPickerValue(0))
        onView(withId(R.id.secondPicker)).perform(setNumberPickerValue(30))
        onView(withText("Add")).perform(click())

        // Save the workout
        onView(withId(R.id.btnSaveWorkout)).perform(click())

        // Verify that the activity is finishing
        scenario.onActivity { activity ->
            assert(activity.isFinishing)
        }
    }



    private fun setNumberPickerValue(value: Int) = object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(NumberPicker::class.java)
        }

        override fun getDescription(): String {
            return "Set the value of a NumberPicker"
        }

        override fun perform(uiController: UiController, view: View) {
            val picker = view as NumberPicker
            picker.value = value
        }
    }
}
