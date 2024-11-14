package com.example.gymtimer.ui.main

import org.junit.Assert.assertEquals
import org.junit.Test

class ExerciseTest {

    @Test
    fun testExerciseInitialization() {
        val exercise = Exercise("Push Up", 30)
        assertEquals("Push Up", exercise.name)
        assertEquals(30, exercise.duration)
    }
}