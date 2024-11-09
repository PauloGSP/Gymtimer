package com.example.gymtimer.ui.main
import java.io.Serializable

data class Exercise(
    val name: String,
    val duration: Int // Duration in seconds
) : Serializable