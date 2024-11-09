package com.example.gymtimer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtimer.ui.main.Exercise
import com.example.gymtimer.ui.main.Rest

class ExercisesAdapter(private val exercisesList: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val EXERCISE_TYPE = 0
    private val REST_TYPE = 1

    override fun getItemViewType(position: Int): Int {
        return when (exercisesList[position]) {
            is Exercise -> EXERCISE_TYPE
            is Rest -> REST_TYPE
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == EXERCISE_TYPE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
            ExerciseViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rest, parent, false)
            RestViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = exercisesList[position]
        if (holder is ExerciseViewHolder && item is Exercise) {
            holder.bind(item)
        } else if (holder is RestViewHolder && item is Rest) {
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int = exercisesList.size

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val exerciseName: TextView = itemView.findViewById(R.id.tvExerciseName)
        private val exerciseDuration: TextView = itemView.findViewById(R.id.tvExerciseDuration)

        fun bind(exercise: Exercise) {
            exerciseName.text = exercise.name
            exerciseDuration.text = "${exercise.duration} secs"
        }
    }

    inner class RestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val restDuration: TextView = itemView.findViewById(R.id.tvRestDuration)

        fun bind(rest: Rest) {
            restDuration.text = "${rest.duration} secs"
        }
    }
}
