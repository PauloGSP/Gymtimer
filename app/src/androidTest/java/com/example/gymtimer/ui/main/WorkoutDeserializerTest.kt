package com.example.gymtimer.ui.main

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WorkoutDeserializerTest {

    private lateinit var gson: Gson

    @Before
    fun setup() {
        gson = GsonBuilder().registerTypeAdapter(Workout::class.java, WorkoutDeserializer()).create()
    }

    @Test
    fun testDeserializeWorkout() {
        val workoutJson = """
            {
                "name": "Morning Workout",
                "exercises": [
                    { "name": "Push Up", "duration": 30 },
                    { "duration": 10 }
                ]
            }
        """.trimIndent()

        val workout = gson.fromJson(workoutJson, Workout::class.java)
        assertEquals("Morning Workout", workout.name)
        assertEquals(2, workout.exercises.size)
        assert(workout.exercises[0] is Exercise)
        assert(workout.exercises[1] is Rest)
    }
}
