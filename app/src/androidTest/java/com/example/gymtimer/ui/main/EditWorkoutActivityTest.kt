package com.example.gymtimer.ui.main

import android.content.Context
import android.widget.EditText
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gymtimer.R
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditWorkoutActivityTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testEditWorkoutActivity_UIElements() {
        ActivityScenario.launch(EditWorkoutActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val editText = activity.findViewById<EditText>(R.id.etWorkoutName)
                editText.setText("New Workout")
                assertEquals("New Workout", editText.text.toString())
            }
        }
    }
}