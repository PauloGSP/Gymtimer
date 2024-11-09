package com.example.gymtimer

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtimer.ui.main.Exercise
import com.example.gymtimer.ui.main.Rest

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
            val newExercise = Exercise("New Exercise", 30) // Placeholder; open a dialog to customize
            exercisesList.add(newExercise)
            adapter.notifyDataSetChanged()
        }

        addRestButton.setOnClickListener {
            val newRest = Rest(10) // Default to 10 seconds; allow customization through dialog
            exercisesList.add(newRest)
            adapter.notifyDataSetChanged()
        }

        saveWorkoutButton.setOnClickListener {
            // Save workout logic here
        }
    }
}
