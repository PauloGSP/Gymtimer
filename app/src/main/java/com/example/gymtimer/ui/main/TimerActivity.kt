package com.example.gymtimer.ui.main

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gymtimer.MainActivity
import com.example.gymtimer.R
import com.example.gymtimer.customviews.CircularTimerView
import com.google.gson.GsonBuilder
import java.text.SimpleDateFormat
import java.util.*

class TimerActivity : AppCompatActivity() {
    private lateinit var workoutNameTextView: TextView
    private lateinit var exerciseNameTextView: TextView
    private lateinit var remainingTimeTextView: TextView
    private lateinit var streakTextView: TextView
    private lateinit var circularTimerView: CircularTimerView
    private lateinit var pauseButton: Button
    private lateinit var stopButton: Button
    private lateinit var skipButton: Button
    private lateinit var mainPageButton: Button
    private var mediaPlayer: MediaPlayer? = null
    private var countDownTimer: CountDownTimer? = null
    private var isPaused = false
    private var remainingTime: Long = 0L
    private var currentIndex = 0
    private var currentWorkout: Workout? = null
    private var isRest = false
    private var workoutCompletedWithoutSkips = true

    companion object {
        private const val PREFS_NAME = "streak_pref"
        private const val KEY_STREAK_COUNT = "streak_count"
        private const val KEY_LAST_WORKOUT_DATE = "last_workout_date"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        workoutNameTextView = findViewById(R.id.workoutNameTextView)
        exerciseNameTextView = findViewById(R.id.exerciseNameTextView)
        remainingTimeTextView = findViewById(R.id.remainingTimeTextView)
        streakTextView = findViewById(R.id.streakTextView)
        circularTimerView = findViewById(R.id.circularTimerView)
        pauseButton = findViewById(R.id.pauseButton)
        stopButton = findViewById(R.id.stopButton)
        skipButton = findViewById(R.id.skipButton)
        mainPageButton = findViewById(R.id.mainPageButton)

        pauseButton.visibility = View.GONE
        stopButton.visibility = View.GONE
        skipButton.visibility = View.GONE
        mainPageButton.visibility = View.GONE
        streakTextView.visibility = View.GONE

        val workoutJson = intent.getStringExtra("workout_data")
        val gson = GsonBuilder().registerTypeAdapter(Workout::class.java, WorkoutDeserializer()).create()
        currentWorkout = gson.fromJson(workoutJson, Workout::class.java)

        currentWorkout?.let {
            workoutNameTextView.text = it.name
            startInitialCountdown()
        }

        pauseButton.setOnClickListener { togglePauseResume() }
        stopButton.setOnClickListener { finishWorkout() }
        skipButton.setOnClickListener {
            workoutCompletedWithoutSkips = false
            skipToNextExerciseOrRest()
        }
        mainPageButton.setOnClickListener { goToMainPage() }
    }

    private fun startInitialCountdown() {
        remainingTimeTextView.text = "Starting in 5 seconds..."
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTimeTextView.text = "Starting in: ${millisUntilFinished / 1000} sec"
            }

            override fun onFinish() {
                pauseButton.visibility = View.VISIBLE
                stopButton.visibility = View.VISIBLE
                skipButton.visibility = View.VISIBLE
                startWorkoutTimer()
            }
        }.start()
    }

    private fun startWorkoutTimer() {
        currentWorkout?.let { workout ->
            val exerciseList = workout.exercises

            if (currentIndex < exerciseList.size) {
                val currentItem = exerciseList[currentIndex]
                isRest = currentItem is Rest
                val duration = if (!isRest) {
                    playBeep() // Play beep sound only at the start of exercises
                    (currentItem as Exercise).duration * 1000L
                } else {
                    (currentItem as Rest).duration * 1000L
                }

                exerciseNameTextView.text = if (!isRest) (currentItem as Exercise).name else "Rest"
                remainingTime = duration

                if (!isRest) {
                    circularTimerView.setTotalTime(duration)
                    circularTimerView.visibility = View.VISIBLE
                    remainingTimeTextView.visibility = View.VISIBLE
                } else {
                    circularTimerView.visibility = View.GONE
                    remainingTimeTextView.visibility = View.VISIBLE
                    remainingTimeTextView.text = "Rest for ${remainingTime / 1000} seconds"
                }

                startCountdownTimer(::onExerciseComplete) // Pass onExerciseComplete to handle end beep after exercise
            } else {
                playEndBeep() // Play end beep for workout completion
                finishWorkout()
            }
        }
    }

    private fun startCountdownTimer(onFinishCallback: (() -> Unit)?) {
        countDownTimer = object : CountDownTimer(remainingTime, 10) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                if (!isPaused) {
                    if (!isRest) {
                        circularTimerView.updateProgress(millisUntilFinished)
                        val seconds = (millisUntilFinished / 1000) % 60
                        val minutes = (millisUntilFinished / 1000) / 60
                        remainingTimeTextView.text = String.format("%02d:%02d", minutes, seconds)
                    } else {
                        remainingTimeTextView.text = "Rest for ${millisUntilFinished / 1000} seconds"
                    }
                }
            }

            override fun onFinish() {
                onFinishCallback?.invoke() // Handle end beep after exercise
                currentIndex++
                startWorkoutTimer()
            }
        }.start()
    }

    private fun playBeep() {
        stopSound() // Ensure no overlapping sounds
        mediaPlayer = MediaPlayer.create(this, R.raw.beep)
        mediaPlayer?.start()
    }

    private fun onExerciseComplete() {
        if (!isRest) playEndBeep() // Only play end beep after exercises, not rests
    }

    private fun playEndBeep() {
        stopSound() // Ensure no overlapping sounds
        mediaPlayer = MediaPlayer.create(this, R.raw.ending)
        mediaPlayer?.start()
    }

    private fun togglePauseResume() {
        if (isPaused) {
            isPaused = false
            pauseButton.text = "Pause"
            startCountdownTimer(if (!isRest) ::onExerciseComplete else null)
        } else {
            isPaused = true
            pauseButton.text = "Resume"
            countDownTimer?.cancel()
            stopSound()
        }
    }

    private fun skipToNextExerciseOrRest() {
        countDownTimer?.cancel()
        stopSound()
        currentIndex++
        isPaused = false
        pauseButton.text = "Pause"
        startWorkoutTimer()
    }

    private fun finishWorkout() {
        countDownTimer?.cancel()
        remainingTimeTextView.visibility = View.VISIBLE
        exerciseNameTextView.visibility = View.GONE
        circularTimerView.visibility = View.GONE
        pauseButton.visibility = View.GONE
        stopButton.visibility = View.GONE
        skipButton.visibility = View.GONE
        mainPageButton.visibility = View.VISIBLE
        remainingTimeTextView.text = "Workout Complete!"

        if (workoutCompletedWithoutSkips) {
            incrementStreak()
        } else {
            Toast.makeText(this, "Workout completed, but streak not increased due to skips.", Toast.LENGTH_LONG).show()
        }
    }

    private fun incrementStreak() {
        val sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
        val lastWorkoutDate = sharedPrefs.getString(KEY_LAST_WORKOUT_DATE, "")
        var streakCount = sharedPrefs.getInt(KEY_STREAK_COUNT, 0)

        if (currentDate != lastWorkoutDate) {
            streakCount = if (lastWorkoutDate == getYesterdayDate()) streakCount + 1 else 1

            with(sharedPrefs.edit()) {
                putInt(KEY_STREAK_COUNT, streakCount)
                putString(KEY_LAST_WORKOUT_DATE, currentDate)
                apply()
            }

            streakTextView.visibility = View.VISIBLE
            streakTextView.text = "Current Streak: $streakCount"
            Toast.makeText(this, "Streak increased! Current streak: $streakCount", Toast.LENGTH_LONG).show()
        }
    }

    private fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }

    private fun goToMainPage() {
        stopSound() // Stop any ongoing sound before navigating to main page
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
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

    override fun onBackPressed() {
        countDownTimer?.cancel() // Cancel timer
        stopSound() // Stop any playing sound
        finish() // Exit activity
    }

    override fun onPause() {
        super.onPause()
        countDownTimer?.cancel() // Cancel the timer if the activity is paused
        stopSound() // Stop any ongoing sound
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        stopSound()
    }
}
