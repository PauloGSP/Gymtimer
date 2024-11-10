package com.example.gymtimer.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtimer.R

class LapAdapter(private val lapList: List<Pair<String, String>>) : RecyclerView.Adapter<LapAdapter.LapViewHolder>() {
    inner class LapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lapTextView: TextView = itemView.findViewById(R.id.lapTextView)
        val lapTimeTextView: TextView = itemView.findViewById(R.id.lapTimeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lap_item, parent, false)
        return LapViewHolder(view)
    }

    override fun onBindViewHolder(holder: LapViewHolder, position: Int) {
        holder.lapTextView.text = lapList[position].first
        holder.lapTimeTextView.text = lapList[position].second
    }

    override fun getItemCount(): Int = lapList.size
}
