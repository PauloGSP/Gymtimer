package com.example.gymtimer.ui.main

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.gymtimer.R
import com.google.android.material.snackbar.Snackbar

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnClearWorkouts: Button = view.findViewById(R.id.btnClearWorkouts)
        btnClearWorkouts.setOnClickListener {
            clearWorkouts()
            Snackbar.make(view, "All workouts have been cleared", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun clearWorkouts() {
        val sharedPreferences = requireContext().getSharedPreferences("workouts_pref", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("workout_list").apply()

        // Notify user that workouts have been cleared
    }
}
