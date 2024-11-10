package com.example.gymtimer.ui.main

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtimer.R

class ChronometerFragment : Fragment() {
    private lateinit var timerTextView: TextView
    private lateinit var startButton: Button
    private lateinit var resetButton: Button
    private lateinit var lapRecyclerView: RecyclerView
    private val lapList = mutableListOf<Pair<String, String>>()
    private lateinit var lapAdapter: LapAdapter

    private var startTime = 0L
    private var timerRunning = false
    private var elapsedMillis = 0L
    private var countDownTimer: CountDownTimer? = null
    private var lapCounter = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chronometer, container, false)

        timerTextView = view.findViewById(R.id.timerTextView)
        startButton = view.findViewById(R.id.btnStart)
        resetButton = view.findViewById(R.id.btnReset)
        lapRecyclerView = view.findViewById(R.id.lapRecyclerView)

        lapAdapter = LapAdapter(lapList)
        lapRecyclerView.layoutManager = LinearLayoutManager(context)
        lapRecyclerView.adapter = lapAdapter

        startButton.setOnClickListener {
            if (!timerRunning) {
                startTimer()
            } else {
                recordLap()
            }
        }

        resetButton.setOnClickListener {
            if (timerRunning) {
                pauseTimer()
            } else {
                resetTimer()
            }
        }

        return view
    }

    private fun startTimer() {
        startTime = System.currentTimeMillis() - elapsedMillis
        timerRunning = true
        startButton.text = "Lap"
        resetButton.text = "Pause"

        countDownTimer = object : CountDownTimer(Long.MAX_VALUE, 10) {
            override fun onTick(millisUntilFinished: Long) {
                elapsedMillis = System.currentTimeMillis() - startTime
                timerTextView.text = formatTime(elapsedMillis)
            }

            override fun onFinish() {
                // Not used, since we are using a timer that doesn't stop
            }
        }.start()
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
        timerRunning = false
        startButton.text = "Resume"
        resetButton.text = "Reset"
    }

    private fun resetTimer() {
        countDownTimer?.cancel()
        elapsedMillis = 0L
        timerRunning = false
        timerTextView.text = formatTime(elapsedMillis)
        lapList.clear()
        lapCounter = 1
        lapAdapter.notifyDataSetChanged()
        startButton.text = "Start"
        resetButton.text = "Reset"
    }

    private fun recordLap() {
        val formattedTime = formatTime(elapsedMillis)
        lapList.add(0, "Lap $lapCounter" to formattedTime)
        lapCounter++
        lapAdapter.notifyDataSetChanged()
    }

    private fun formatTime(elapsedMillis: Long): String {
        val milliseconds = (elapsedMillis % 1000) / 10
        val seconds = (elapsedMillis / 1000) % 60
        val minutes = (elapsedMillis / (1000 * 60)) % 60
        val hours = (elapsedMillis / (1000 * 60 * 60))
        return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, milliseconds)
    }
}
