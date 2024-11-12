package com.example.gymtimer.ui.main

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
import com.example.gymtimer.ExercisesAdapter
import com.example.gymtimer.MainActivity
import com.example.gymtimer.R
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class EditWorkoutActivity : AppCompatActivity() {
    private lateinit var workoutNameEditText: EditText
    private lateinit var addExerciseButton: Button
    private lateinit var addRestButton: Button
    private lateinit var saveWorkoutButton: Button
    private lateinit var deleteWorkoutButton: Button
    private lateinit var exercisesRecyclerView: RecyclerView
    private var exercisesList = mutableListOf<Any>()
    private lateinit var workout: Workout
    private lateinit var adapter: ExercisesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_workout)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        workoutNameEditText = findViewById(R.id.etWorkoutName)
        addExerciseButton = findViewById(R.id.btnAddExercise)
        addRestButton = findViewById(R.id.btnAddRest)
        saveWorkoutButton = findViewById(R.id.btnSaveWorkout)
        deleteWorkoutButton = findViewById(R.id.btnDeleteWorkout)
        exercisesRecyclerView = findViewById(R.id.recyclerViewExercises)

        exercisesRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ExercisesAdapter(exercisesList) { position ->
            exercisesList.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
        exercisesRecyclerView.adapter = adapter

        // Load the workout data with the custom deserializer
        val gson = GsonBuilder()
            .registerTypeAdapter(Workout::class.java, WorkoutDeserializer())
            .create()

        val workoutJson = intent.getStringExtra("workout_data")
        workout = gson.fromJson(workoutJson, Workout::class.java)

        workoutNameEditText.setText(workout.name)
        exercisesList.addAll(workout.exercises)
        adapter.notifyDataSetChanged()

        addExerciseButton.setOnClickListener {
            showAddExerciseDialog(adapter)
        }

        addRestButton.setOnClickListener {
            showAddRestDialog(adapter)
        }

        saveWorkoutButton.setOnClickListener {
            if (workoutNameEditText.text.isNotEmpty() && exercisesList.isNotEmpty()) {
                saveWorkoutToStorage()
            } else {
                showErrorDialog("Please enter a workout name and add at least one exercise.")
            }
        }

        deleteWorkoutButton.setOnClickListener {
            confirmDeleteWorkout()
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
                val duration = exerciseDurationEditText.text.toString().toIntOrNull() ?: 30
                if (name.isNotEmpty() && duration > 0) {
                    exercisesList.add(Exercise(name, duration))
                    adapter.notifyItemInserted(exercisesList.size - 1)
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
                val duration = restDurationEditText.text.toString().toIntOrNull() ?: 10
                if (duration > 0) {
                    exercisesList.add(Rest(duration))
                    adapter.notifyItemInserted(exercisesList.size - 1)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveWorkoutToStorage() {
        val sharedPreferences = getSharedPreferences("workouts_pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = GsonBuilder()
            .registerTypeAdapter(Workout::class.java, WorkoutDeserializer())
            .create()

        val updatedWorkout = Workout(workoutNameEditText.text.toString(), exercisesList)
        val existingWorkoutsJson = sharedPreferences.getString("workout_list", null)
        val type = object : TypeToken<MutableList<Workout>>() {}.type
        val workoutList: MutableList<Workout> = existingWorkoutsJson?.let {
            gson.fromJson(it, type)
        } ?: mutableListOf()

        // Update the workout in the list
        workoutList.removeIf { it.name == workout.name }
        workoutList.add(updatedWorkout)

        val updatedWorkoutsJson = gson.toJson(workoutList)
        editor.putString("workout_list", updatedWorkoutsJson).apply()

        finish()
    }

    private fun confirmDeleteWorkout() {
        AlertDialog.Builder(this)
            .setTitle("Delete Workout")
            .setMessage("Are you sure you want to delete this workout?")
            .setPositiveButton("Yes") { _, _ ->
                deleteWorkoutFromStorage()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteWorkoutFromStorage() {
        val sharedPreferences = getSharedPreferences("workouts_pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = GsonBuilder()
            .registerTypeAdapter(Workout::class.java, WorkoutDeserializer())
            .create()

        val existingWorkoutsJson = sharedPreferences.getString("workout_list", null)
        val type = object : TypeToken<MutableList<Workout>>() {}.type
        val workoutList: MutableList<Workout> = existingWorkoutsJson?.let {
            gson.fromJson(it, type)
        } ?: mutableListOf()

        // Remove the workout from the list
        workoutList.removeIf { it.name == workout.name }

        val updatedWorkoutsJson = gson.toJson(workoutList)
        editor.putString("workout_list", updatedWorkoutsJson).apply()

        finish() // Return to the main screen after deleting
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
