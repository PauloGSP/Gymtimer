package com.example.gymtimer.ui.main

import java.io.Serializable

data class Workout(
    val name: String,
    val exercises: List<Any>, // This can be a mix of Exercise and Rest objects
    val totalDuration: Int = 0, // Optional, calculated based on exercises and rests
    val description: String = "" // Optional description or notes
): Serializable {
    // You could add a method to calculate total duration based on the exercises list
    fun calculateTotalDuration(): Int {
        return exercises.sumOf {
            when (it) {
                is Exercise -> it.duration
                is Rest -> it.duration
                else -> 0
            }
        }
    }
}
