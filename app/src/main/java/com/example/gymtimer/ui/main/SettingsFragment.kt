// SettingsFragment.kt
package com.example.gymtimer.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.gymtimer.R
import com.google.android.material.snackbar.Snackbar

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var streakTextView: TextView
    private lateinit var resetStreakButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        streakTextView = view.findViewById(R.id.streakTextView)
        resetStreakButton = view.findViewById(R.id.resetStreakButton)

        // Display the current streak
        displayCurrentStreak()

        // Set up button to clear all workouts
        val btnClearWorkouts: Button = view.findViewById(R.id.btnClearWorkouts)
        btnClearWorkouts.setOnClickListener {
            clearWorkouts()
            Snackbar.make(view, "All workouts have been cleared", Snackbar.LENGTH_SHORT).show()

            // Send broadcast to update the main page
            val intent = Intent("com.example.gymtimer.WORKOUTS_CLEARED")
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }

        // Reset streak button functionality
        resetStreakButton.setOnClickListener {
            resetStreak()
            displayCurrentStreak()
            Snackbar.make(view, "Streak has been reset", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun displayCurrentStreak() {
        val sharedPreferences = requireContext().getSharedPreferences("streak_pref", Context.MODE_PRIVATE)
        val currentStreak = sharedPreferences.getInt("streak_count", 0)
        streakTextView.text = "Current Streak: $currentStreak"
    }

    private fun resetStreak() {
        val sharedPreferences = requireContext().getSharedPreferences("streak_pref", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("streak_count", 0) // Reset streak count
            remove("last_workout_date") // Remove the last workout date to allow streak increase
            apply()
        }
    }

    private fun clearWorkouts() {
        val sharedPreferences = requireContext().getSharedPreferences("workouts_pref", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("workout_list").apply()
    }
}
