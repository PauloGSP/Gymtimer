package com.example.gymtimer.ui.main

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WorkoutTest {

    private lateinit var workout: Workout

    @Before
    fun setup() {
        workout = Workout("Morning Workout", listOf(Exercise("Push Up", 30), Rest(10)))
    }

    @Test
    fun testWorkoutName() {
        assertEquals("Morning Workout", workout.name)
    }

    @Test
    fun testWorkoutExercises() {
        assertEquals(2, workout.exercises.size)
    }
}
