package com.example.gymtimer.ui.main

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtimer.ExerciseAdapter
import com.example.gymtimer.R
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class ViewWorkoutActivity : AppCompatActivity() {

    private lateinit var workoutNameTextView: TextView
    private lateinit var exercisesRecyclerView: RecyclerView
    private lateinit var adapter: ExerciseAdapter
    private var exercisesList = mutableListOf<Any>()
    private lateinit var workout: Workout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_workout)

        workoutNameTextView = findViewById(R.id.workoutNameTextView)
        exercisesRecyclerView = findViewById(R.id.recyclerViewExercises)

        // Set up RecyclerView
        exercisesRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ExerciseAdapter(exercisesList, viewOnly = true)
        exercisesRecyclerView.adapter = adapter

        // Load workout data from intent
        val gson = GsonBuilder()
            .registerTypeAdapter(Workout::class.java, WorkoutDeserializer())
            .create()
        val workoutJson = intent.getStringExtra("workout_data")
        workout = gson.fromJson(workoutJson, Workout::class.java)

        workoutNameTextView.text = workout.name
        exercisesList.addAll(workout.exercises)
        adapter.notifyDataSetChanged()
    }

    // Custom method for the back button in the XML
    fun backButtonClick(view: View) {
        onBackPressed()
    }
}
