package com.example.gymtimer.ui.main

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.gymtimer.R
import com.example.gymtimer.customviews.CircularTimerView

class SimpleTimerFragment : Fragment() {
    private lateinit var thour: TextView
    private lateinit var tminute: TextView
    private lateinit var tsecond: TextView
    private lateinit var hourPicker: NumberPicker
    private lateinit var minutePicker: NumberPicker
    private lateinit var secondPicker: NumberPicker
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private lateinit var stopButton: Button
    private lateinit var circularTimerView: CircularTimerView
    private lateinit var initialTimeTextView: TextView
    private lateinit var remainingTimeTextView: TextView
    private var mediaPlayer: MediaPlayer? = null

    private var totalTimeInMillis: Long = 0L
    private var timerRunning = false
    private var countDownTimer: CountDownTimer? = null
    private var remainingTime: Long = 0L
    private var pausedTime: Long = 0L // Store paused time for resuming

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_simple_timer, container, false)

        hourPicker = view.findViewById(R.id.hourPicker)
        minutePicker = view.findViewById(R.id.minutePicker)
        secondPicker = view.findViewById(R.id.secondPicker)
        startButton = view.findViewById(R.id.btnStartTimer)
        pauseButton = view.findViewById(R.id.btnPauseTimer)
        stopButton = view.findViewById(R.id.btnStopTimer)
        circularTimerView = view.findViewById(R.id.circularTimerView)
        initialTimeTextView = view.findViewById(R.id.initialTimeTextView)
        remainingTimeTextView = view.findViewById(R.id.remainingTimeTextView)
        thour = view.findViewById(R.id.thours)
        tminute = view.findViewById(R.id.tminutes)
        tsecond = view.findViewById(R.id.tsecond)

        hourPicker.maxValue = 99
        hourPicker.minValue = 0
        minutePicker.maxValue = 59
        minutePicker.minValue = 0
        secondPicker.maxValue = 59
        secondPicker.minValue = 0

        startButton.setOnClickListener {
            startTimer()
        }

        pauseButton.setOnClickListener {
            if (timerRunning) {
                pauseTimer()
            } else {
                resumeTimer()
            }
        }

        stopButton.setOnClickListener {
            stopTimer()
        }

        return view
    }

    private fun startTimer() {
        stopSound() // Ensure any previous sound is stopped
        val hours = hourPicker.value
        val minutes = minutePicker.value
        val seconds = secondPicker.value

        totalTimeInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000L
        if (totalTimeInMillis == 0L) return
        thour.visibility = View.GONE
        tminute.visibility = View.GONE
        tsecond.visibility = View.GONE
        hourPicker.visibility = View.GONE
        minutePicker.visibility = View.GONE
        secondPicker.visibility = View.GONE
        circularTimerView.visibility = View.VISIBLE

        // Display initial time
        val formattedInitialTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        initialTimeTextView.text = "Initial Time: $formattedInitialTime"
        initialTimeTextView.visibility = View.VISIBLE
        remainingTimeTextView.visibility = View.VISIBLE
        pauseButton.text = "Pause"

        circularTimerView.setTotalTime(totalTimeInMillis)
        timerRunning = true
        startButton.visibility = View.GONE
        pauseButton.visibility = View.VISIBLE
        stopButton.visibility = View.VISIBLE

        countDownTimer = createCountDownTimer(totalTimeInMillis).start()
    }

    private fun resumeTimer() {
        countDownTimer = createCountDownTimer(remainingTime).start()
        timerRunning = true
        pauseButton.text = "Pause"
        startButton.visibility = View.GONE
        pauseButton.visibility = View.VISIBLE
        stopButton.visibility = View.VISIBLE
    }

    private fun createCountDownTimer(timeInMillis: Long): CountDownTimer {
        return object : CountDownTimer(timeInMillis, 10) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                circularTimerView.updateProgress(millisUntilFinished)

                // Update the remaining time display
                val hours = (millisUntilFinished / (1000 * 60 * 60)) % 24
                val minutes = (millisUntilFinished / (1000 * 60)) % 60
                val seconds = (millisUntilFinished / 1000) % 60
                val milliseconds = (millisUntilFinished % 1000) / 10

                remainingTimeTextView.text = String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, milliseconds)
            }

            override fun onFinish() {
                resetToInitialLayout()
                playAlarmSound()
            }
        }
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
        pausedTime = remainingTime
        timerRunning = false
        pauseButton.text = "Resume"
        pauseButton.visibility = View.VISIBLE
        stopButton.visibility = View.VISIBLE
        startButton.visibility = View.GONE
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
        stopSound() // Stop any currently playing sound
        resetToInitialLayout()
        pausedTime = 0L
    }

    private fun resetToInitialLayout() {
        timerRunning = false
        startButton.text = "Start"
        startButton.visibility = View.VISIBLE
        pauseButton.visibility = View.GONE
        stopButton.visibility = View.GONE

        circularTimerView.reset()
        remainingTimeTextView.text = "00:00:00.00"

        // Reset visibility of time pickers and labels
        hourPicker.visibility = View.VISIBLE
        minutePicker.visibility = View.VISIBLE
        secondPicker.visibility = View.VISIBLE
        thour.visibility = View.VISIBLE
        tminute.visibility = View.VISIBLE
        tsecond.visibility = View.VISIBLE

        // Hide initial time and remaining time display
        initialTimeTextView.visibility = View.GONE
        remainingTimeTextView.visibility = View.GONE
        circularTimerView.visibility = View.GONE
    }

    private fun playAlarmSound() {
        stopSound() // Stop any currently playing sound
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm_clock)
        var playCount = 0
        val maxPlays = 3

        mediaPlayer?.apply {
            start()
            setOnCompletionListener {
                playCount++
                if (playCount < maxPlays) {
                    start()
                } else {
                    release()
                    mediaPlayer = null
                }
            }
        }
    }

    private fun stopSound() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayer = null
        }
    }

    override fun onPause() {
        super.onPause()
        stopSound() // Stop any playing sound when fragment is paused
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSound() // Release media player resources when fragment is destroyed
    }
}
