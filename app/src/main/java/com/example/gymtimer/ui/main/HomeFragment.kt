package com.example.gymtimer.ui.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtimer.AddWorkoutActivity
import com.example.gymtimer.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WorkoutAdapter
    private val workoutList = mutableListOf<Workout>()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = requireContext().getSharedPreferences("workouts_pref", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewWorkouts)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = WorkoutAdapter(workoutList)
        recyclerView.adapter = adapter

        loadWorkoutsFromStorage()

        // Floating action button to add a new workout
        val fabAddWorkout = view.findViewById<FloatingActionButton>(R.id.fabAddWorkout)
        fabAddWorkout.setOnClickListener {
            val intent = Intent(activity, AddWorkoutActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        loadWorkoutsFromStorage()
    }

    // Refreshes the workout list by reloading data from SharedPreferences
    fun refreshWorkoutList() {
        loadWorkoutsFromStorage()
    }

    private fun loadWorkoutsFromStorage() {
        // Retrieve the workouts from SharedPreferences
        val existingWorkoutsJson = sharedPreferences.getString("workout_list", null)
        val gson = Gson()
        val type = object : TypeToken<MutableList<Workout>>() {}.type
        val loadedWorkouts: MutableList<Workout> = existingWorkoutsJson?.let {
            gson.fromJson(it, type)
        } ?: mutableListOf()

        // Clear the existing workout list and add the new data
        workoutList.clear()
        workoutList.addAll(loadedWorkouts)

        // Notify the adapter of the data change
        adapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(sectionNumber: Int): HomeFragment {
            return HomeFragment().apply {
                arguments = Bundle().apply {
                    putInt("section_number", sectionNumber)
                }
            }
        }
    }
}
