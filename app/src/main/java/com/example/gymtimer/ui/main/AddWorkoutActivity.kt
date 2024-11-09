package com.example.gymtimer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtimer.ui.main.Exercise
import com.example.gymtimer.ui.main.Rest
import com.example.gymtimer.ui.main.Workout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class AddWorkoutActivity : AppCompatActivity() {
    private lateinit var workoutNameEditText: EditText
    private lateinit var addExerciseButton: Button
    private lateinit var addRestButton: Button
    private lateinit var saveWorkoutButton: Button
    private lateinit var exercisesRecyclerView: RecyclerView
    private val exercisesList = mutableListOf<Any>() // Can hold Exercise or Rest objects

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_workout)

        workoutNameEditText = findViewById(R.id.etWorkoutName)
        addExerciseButton = findViewById(R.id.btnAddExercise)
        addRestButton = findViewById(R.id.btnAddRest)
        saveWorkoutButton = findViewById(R.id.btnSaveWorkout)
        exercisesRecyclerView = findViewById(R.id.recyclerViewExercises)

        exercisesRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ExercisesAdapter(exercisesList)
        exercisesRecyclerView.adapter = adapter

        addExerciseButton.setOnClickListener {
            showAddExerciseDialog(adapter)
        }

        addRestButton.setOnClickListener {
            showAddRestDialog(adapter)
        }

        saveWorkoutButton.setOnClickListener {
            if (workoutNameEditText.text.isNotEmpty() && exercisesList.isNotEmpty()) {
                saveWorkout()
            } else {
                // Show a warning message if the workout name or exercises are empty
                showErrorDialog("Please enter a workout name and add at least one exercise.")
            }
        }
    }

    private fun showAddExerciseDialog(adapter: ExercisesAdapter) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_exercise, null)
        val exerciseNameEditText = dialogView.findViewById<EditText>(R.id.etExerciseName)
        val exerciseDurationEditText = dialogView.findViewById<EditText>(R.id.etExerciseDuration)

        AlertDialog.Builder(this)
            .setTitle("Add Exercise")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = exerciseNameEditText.text.toString()
                val duration = exerciseDurationEditText.text.toString().toIntOrNull() ?: 30 // Default 30 seconds
                if (name.isNotEmpty() && duration > 0) {
                    exercisesList.add(Exercise(name, duration))
                    adapter.notifyDataSetChanged()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showAddRestDialog(adapter: ExercisesAdapter) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_rest, null)
        val restDurationEditText = dialogView.findViewById<EditText>(R.id.etRestDuration)

        AlertDialog.Builder(this)
            .setTitle("Add Rest")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val duration = restDurationEditText.text.toString().toIntOrNull() ?: 10 // Default 10 seconds
                if (duration > 0) {
                    exercisesList.add(Rest(duration))
                    adapter.notifyDataSetChanged()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveWorkout() {
        val sharedPreferences = getSharedPreferences("workouts_pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()

        // Retrieve existing workouts
        val existingWorkoutsJson = sharedPreferences.getString("workout_list", null)
        val type = object : TypeToken<MutableList<Workout>>() {}.type
        val workoutList: MutableList<Workout> = existingWorkoutsJson?.let {
            gson.fromJson(it, type)
        } ?: mutableListOf()

        // Create a new Workout object
        val workout = Workout(
            name = workoutNameEditText.text.toString(),
            exercises = exercisesList
        )

        // Add the new workout to the list and save it
        workoutList.add(workout)
        val updatedWorkoutsJson = gson.toJson(workoutList)
        editor.putString("workout_list", updatedWorkoutsJson)
        editor.apply()

        // Finish activity and return to main screen
        finish()
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}