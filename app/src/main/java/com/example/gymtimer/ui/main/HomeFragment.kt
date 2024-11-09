package com.example.gymtimer.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtimer.AddWorkoutActivity
import com.example.gymtimer.R
import com.example.gymtimer.ui.main.Workout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WorkoutAdapter
    private val workoutList = mutableListOf<Workout>()

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
    private fun clearWorkouts() {
        val sharedPreferences = requireContext().getSharedPreferences("workouts_pref", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("workout_list").apply()

        workoutList.clear()
        adapter.notifyDataSetChanged()
    }

    private fun loadWorkoutsFromStorage() {
        val sharedPreferences = requireContext().getSharedPreferences("workouts_pref", Context.MODE_PRIVATE)
        val gson = Gson()
        val existingWorkoutsJson = sharedPreferences.getString("workout_list", null)
        val type = object : TypeToken<MutableList<Workout>>() {}.type

        val loadedWorkouts: MutableList<Workout> = existingWorkoutsJson?.let {
            gson.fromJson(it, type)
        } ?: mutableListOf()

        workoutList.clear()
        workoutList.addAll(loadedWorkouts)
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
