package com.example.gymtimer.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtimer.ExercisesAdapter
import com.example.gymtimer.R
import com.example.gymtimer.ui.main.Exercise
import com.example.gymtimer.ui.main.Rest
import com.example.gymtimer.ui.main.Workout
import com.example.gymtimer.ui.main.WorkoutDeserializer
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
        adapter = ExercisesAdapter(
            items = exercisesList,
            onDeleteItem = { position ->
                exercisesList.removeAt(position)
                adapter.notifyItemRemoved(position)
            },
            onStartDrag = {} // This will be set up below with ItemTouchHelper
        )

        exercisesRecyclerView.adapter = adapter

        // Load the workout data with the custom deserializer
        val gson = GsonBuilder().registerTypeAdapter(Workout::class.java, WorkoutDeserializer()).create()
        val workoutJson = intent.getStringExtra("workout_data")
        workout = gson.fromJson(workoutJson, Workout::class.java)

        workoutNameEditText.setText(workout.name)
        exercisesList.addAll(workout.exercises)
        adapter.notifyDataSetChanged()

        addExerciseButton.setOnClickListener { showAddExerciseDialog() }
        addRestButton.setOnClickListener { showAddRestDialog() }
        saveWorkoutButton.setOnClickListener { saveWorkoutToStorage() }
        deleteWorkoutButton.setOnClickListener { confirmDeleteWorkout() }

        // Set up ItemTouchHelper for drag-and-drop
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                // Update list and notify adapter
                exercisesList.add(toPosition, exercisesList.removeAt(fromPosition))
                adapter.notifyItemMoved(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Not used as swipe actions are not needed here
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(exercisesRecyclerView)
    }

    private fun showAddExerciseDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_time_picker, null)
        val exerciseNameEditText = dialogView.findViewById<EditText>(R.id.etExerciseName)
        val minutePicker = dialogView.findViewById<NumberPicker>(R.id.minutePicker)
        val secondPicker = dialogView.findViewById<NumberPicker>(R.id.secondPicker)

        // Set up time picker values
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
        val minutePicker = dialogView.findViewById<NumberPicker>(R.id.minutePicker)
        val secondPicker = dialogView.findViewById<NumberPicker>(R.id.secondPicker)
        dialogView.findViewById<EditText>(R.id.etExerciseName).visibility = View.GONE // Hide name input for rest

        // Set up time picker values
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

    private fun saveWorkoutToStorage() {
        // Check if the workout has at least one exercise or rest
        if (exercisesList.isEmpty()) {
            showErrorDialog("A workout must contain at least one exercise or rest.")
            return
        }

        val sharedPreferences = getSharedPreferences("workouts_pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = GsonBuilder().registerTypeAdapter(Workout::class.java, WorkoutDeserializer()).create()

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

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun confirmDeleteWorkout() {
        AlertDialog.Builder(this)
            .setTitle("Delete Workout")
            .setMessage("Are you sure you want to delete this workout?")
            .setPositiveButton("Yes") { _, _ -> deleteWorkoutFromStorage() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteWorkoutFromStorage() {
        val sharedPreferences = getSharedPreferences("workouts_pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = GsonBuilder().registerTypeAdapter(Workout::class.java, WorkoutDeserializer()).create()

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
}
