package com.example.gymtimer.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtimer.R

class WorkoutAdapter(private val workouts: List<Workout>) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {
    inner class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWorkoutName: TextView = itemView.findViewById(R.id.tvWorkoutName)
        val btnPlay: ImageButton = itemView.findViewById(R.id.btnPlay)
        val btnSettings: ImageButton = itemView.findViewById(R.id.btnSettings)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.workout_item, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workouts[position]
        holder.tvWorkoutName.text = workout.name

        holder.btnPlay.setOnClickListener {
            // Code to start TimerActivity
        }

        holder.btnSettings.setOnClickListener {
            // Code to open EditWorkoutActivity
        }
    }

    override fun getItemCount(): Int = workouts.size
}