package com.example.gymtimer.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gymtimer.ExerciseAdapter
import com.example.gymtimer.R
import com.example.gymtimer.ui.main.Exercise
import com.example.gymtimer.ui.main.Rest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExerciseAdapterTest {

    private lateinit var context: Context
    private lateinit var exerciseAdapter: ExerciseAdapter

    @Before
    fun setup() {
        // Launch a FragmentActivity to apply the theme
        val scenario = ActivityScenario.launch(FragmentActivity::class.java)
        scenario.onActivity { activity ->
            activity.setTheme(R.style.Theme_Gymtimer) // Set the correct theme to the activity
        }

        // Initialize context and adapter
        context = ApplicationProvider.getApplicationContext()

        // Define the list of exercises/rests
        val exercises: MutableList<Any> = mutableListOf(
            Exercise("Push Up", 30),
            Rest(15),
            Exercise("Squat", 20)
        )

        // Initialize the ExerciseAdapter with the list
        exerciseAdapter = ExerciseAdapter(exercises)
    }

    @Test
    fun testGetItemCount() {
        assertEquals(3, exerciseAdapter.itemCount)
    }

    @Test
    fun testOnBindViewHolderForExercise() {
        // Create a mock parent view (as a FrameLayout) to simulate the RecyclerView
        val parent = FrameLayout(context)

        // Inflate the layout for the ViewHolder to properly test the binding
        val view = LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false)

        // Create ViewHolder instance
        val viewHolder = exerciseAdapter.ExerciseViewHolder(view)

        // Bind the data
        exerciseAdapter.onBindViewHolder(viewHolder, 0)

        // Assertions to verify if the ViewHolder was correctly bound
        val exerciseNameTextView = viewHolder.itemView.findViewById<TextView>(R.id.tvExerciseName)
        val exerciseDurationTextView = viewHolder.itemView.findViewById<TextView>(R.id.tvExerciseDuration)
        assertEquals("Push Up", exerciseNameTextView.text.toString())
        assertEquals("Duration: 30 sec", exerciseDurationTextView.text.toString())
    }

    @Test
    fun testOnBindViewHolderForRest() {
        // Create a mock parent view (as a FrameLayout) to simulate the RecyclerView
        val parent = FrameLayout(context)

        // Inflate the layout for the ViewHolder to properly test the binding
        val view = LayoutInflater.from(context).inflate(R.layout.item_rest, parent, false)

        // Create ViewHolder instance
        val viewHolder = exerciseAdapter.RestViewHolder(view)

        // Bind the data
        exerciseAdapter.onBindViewHolder(viewHolder, 1)

        // Assertions to verify if the ViewHolder was correctly bound
        val restDurationTextView = viewHolder.itemView.findViewById<TextView>(R.id.tvRestDuration)
        assertEquals("Duration: 15 sec", restDurationTextView.text.toString())
    }

    @Test
    fun testMoveItem() {
        // Since items are private, we need to validate using the adapter's behavior
        exerciseAdapter.moveItem(0, 1)

        // Verify if the item was successfully moved by checking the updated positions through binding
        val parent = FrameLayout(context)
        val view = LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false)
        val viewHolder = exerciseAdapter.ExerciseViewHolder(view)

        exerciseAdapter.onBindViewHolder(viewHolder, 1)
        val exerciseNameTextView = viewHolder.itemView.findViewById<TextView>(R.id.tvExerciseName)
        assertEquals("Push Up", exerciseNameTextView.text.toString())
    }
}
