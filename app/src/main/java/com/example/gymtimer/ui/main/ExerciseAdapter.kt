package com.example.gymtimer

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtimer.ui.main.Exercise
import com.example.gymtimer.ui.main.Rest
class ExercisesAdapter(private val items: MutableList<Any>, private val onDeleteItem: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_EXERCISE = 0
        private const val TYPE_REST = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Exercise -> TYPE_EXERCISE
            is Rest -> TYPE_REST
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EXERCISE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
                ExerciseViewHolder(view)
            }
            TYPE_REST -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rest, parent, false)
                RestViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ExerciseViewHolder -> {
                val exercise = items[position] as Exercise
                holder.bind(exercise)
                holder.deleteButton.setOnClickListener {
                    onDeleteItem(position)
                }
            }
            is RestViewHolder -> {
                val rest = items[position] as Rest
                holder.bind(rest)
                holder.deleteButton.setOnClickListener {
                    onDeleteItem(position)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val exerciseName: TextView = itemView.findViewById(R.id.tvExerciseName)
        private val exerciseDuration: TextView = itemView.findViewById(R.id.tvExerciseDuration)
        val deleteButton: ImageButton = itemView.findViewById(R.id.btnDeleteExercise)

        fun bind(exercise: Exercise) {
            exerciseName.text = exercise.name
            exerciseDuration.text = "Duration: ${exercise.duration} sec"
        }
    }

    inner class RestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val restDuration: TextView = itemView.findViewById(R.id.tvRestDuration)
        val deleteButton: ImageButton = itemView.findViewById(R.id.btnDeleteRest)

        fun bind(rest: Rest) {
            restDuration.text = "Duration: ${rest.duration} sec"
        }
    }
}


