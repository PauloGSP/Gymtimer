package com.example.gymtimer.ui.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.example.gymtimer.ui.main.Workout
import com.example.gymtimer.ui.main.WorkoutAdapter
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkoutAdapterTest {

    private lateinit var workoutAdapter: WorkoutAdapter

    @Before
    fun setup() {
        val workouts: List<Workout> = listOf(Workout("Morning Workout", listOf()))
        workoutAdapter = WorkoutAdapter(workouts)
    }

    @Test
    fun testGetItemCount() {
        assertEquals(1, workoutAdapter.itemCount)
    }

    @Test
    fun testOnBindViewHolder() {
        // Create RecyclerView and set a LayoutManager
        val parent = RecyclerView(ApplicationProvider.getApplicationContext())
        parent.layoutManager = LinearLayoutManager(ApplicationProvider.getApplicationContext())

        // Create a ViewHolder using the adapter
        val viewHolder = workoutAdapter.onCreateViewHolder(parent, 0)

        // Bind the ViewHolder
        workoutAdapter.onBindViewHolder(viewHolder, 0)

        // Assert the correct binding of data
        assertEquals("Morning Workout", viewHolder.tvWorkoutName.text)
    }
}
