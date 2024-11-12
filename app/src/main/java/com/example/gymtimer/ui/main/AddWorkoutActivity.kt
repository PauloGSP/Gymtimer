package com.example.gymtimer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtimer.ui.main.Exercise
import com.example.gymtimer.ui.main.Rest
import com.example.gymtimer.ui.main.Workout
import com.example.gymtimer.ui.main.WorkoutDeserializer
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class AddWorkoutActivity : AppCompatActivity() {
    private lateinit var workoutNameEditText: EditText
    private lateinit var addExerciseButton: Button
    private lateinit var addRestButton: Button
    private lateinit var saveWorkoutButton: Button
    private lateinit var exercisesRecyclerView: RecyclerView
    private val exercisesList = mutableListOf<Any>()
    private lateinit var adapter: ExercisesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_workout)

        workoutNameEditText = findViewById(R.id.etWorkoutName)
        addExerciseButton = findViewById(R.id.btnAddExercise)
        addRestButton = findViewById(R.id.btnAddRest)
        saveWorkoutButton = findViewById(R.id.btnSaveWorkout)
        exercisesRecyclerView = findViewById(R.id.recyclerViewExercises)

        exercisesRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ExercisesAdapter(
            items = exercisesList,
            onDeleteItem = { position ->
                exercisesList.removeAt(position)
                adapter.notifyItemRemoved(position)
            },
            onStartDrag = {} // No-op lambda for dragging since it's not used here
        )

        exercisesRecyclerView.adapter = adapter

        addExerciseButton.setOnClickListener {
            showAddExerciseDialog()
        }

        addRestButton.setOnClickListener {
            showAddRestDialog()
        }

        saveWorkoutButton.setOnClickListener {
            if (workoutNameEditText.text.isNotEmpty() && exercisesList.isNotEmpty()) {
                saveWorkout()
            } else {
                showErrorDialog("Please enter a workout name and add at least one exercise.")
            }
        }
    }

    private fun showAddExerciseDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_time_picker, null)
        val exerciseNameEditText = dialogView.findViewById<EditText>(R.id.etExerciseName)
        val minutePicker = dialogView.findViewById<NumberPicker>(R.id.minutePicker)
        val secondPicker = dialogView.findViewById<NumberPicker>(R.id.secondPicker)

        // Set up the NumberPickers programmatically
        minutePicker.minValue = 0
        minutePicker.maxValue = 99
        secondPicker.minValue = 0
        secondPicker.maxValue = 59

        AlertDialog.Builder(this)
            .setTitle("Add Exercise")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = exerciseNameEditText.text.toString()
                val minutes = minutePicker.value
                val seconds = secondPicker.value
                val duration = minutes * 60 + seconds

                if (name.isNotEmpty() && duration > 0) {
                    exercisesList.add(Exercise(name, duration))
                    adapter.notifyItemInserted(exercisesList.size - 1)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showAddRestDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_time_picker, null)
        val exerciseNameEditText = dialogView.findViewById<EditText>(R.id.etExerciseName)
        exerciseNameEditText.visibility = View.GONE // Hide the name input for rest
        val minutePicker = dialogView.findViewById<NumberPicker>(R.id.minutePicker)
        val secondPicker = dialogView.findViewById<NumberPicker>(R.id.secondPicker)

        // Set up the NumberPickers programmatically
        minutePicker.minValue = 0
        minutePicker.maxValue = 99
        secondPicker.minValue = 0
        secondPicker.maxValue = 59

        AlertDialog.Builder(this)
            .setTitle("Add Rest")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val minutes = minutePicker.value
                val seconds = secondPicker.value
                val duration = minutes * 60 + seconds

                if (duration > 0) {
                    exercisesList.add(Rest(duration))
                    adapter.notifyItemInserted(exercisesList.size - 1)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveWorkout() {
        if (exercisesList.isEmpty()) {
            showErrorDialog("A workout must contain at least one exercise or rest.")
            return
        }

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

        val workout = Workout(
            name = workoutNameEditText.text.toString(),
            exercises = exercisesList
        )

        workoutList.add(workout)
        val updatedWorkoutsJson = gson.toJson(workoutList)
        editor.putString("workout_list", updatedWorkoutsJson).apply()

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
