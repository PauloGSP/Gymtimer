package com.example.gymtimer.ui.main
import com.example.gymtimer.ui.main.WorkoutDeserializer
import com.google.gson.Gson

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gymtimer.R
import com.google.gson.GsonBuilder

class TimerActivity : AppCompatActivity() {
    private lateinit var timerTextView: TextView
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        timerTextView = findViewById(R.id.timerTextView)

        // Get workout data from intent
        val workoutJson = intent.getStringExtra("workout_data")
        val gson = GsonBuilder()
            .registerTypeAdapter(Workout::class.java, WorkoutDeserializer())
            .create()

        val workout = gson.fromJson(workoutJson, Workout::class.java)

        workout?.let {
            startWorkoutTimer(it)
        }
    }

    private fun startWorkoutTimer(workout: Workout) {
        val exerciseList = workout.exercises
        var currentIndex = 0

        fun playBeep() {
            mediaPlayer = MediaPlayer.create(this, R.raw.beep)
            mediaPlayer.start()
        }

        fun endBeep(){
            mediaPlayer = MediaPlayer.create(this, R.raw.ending)
            mediaPlayer.start()
        }

        fun startNextTimer() {
            if (currentIndex < exerciseList.size) {
                val currentItem = exerciseList[currentIndex]
                val duration = if (currentItem is Exercise) {
                    playBeep() // Play sound at the start of an exercise
                    currentItem.duration
                } else {
                    (currentItem as Rest).duration
                }

                object : CountDownTimer((duration * 1000).toLong(), 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        timerTextView.text = "Time left: ${millisUntilFinished / 1000} sec"
                    }

                    override fun onFinish() {
                        if (currentItem is Exercise) {
                            endBeep() // Play sound at the end of an exercise
                        }
                        currentIndex++
                        startNextTimer()
                    }
                }.start()
            } else {
                endBeep() // Optional: final beep to indicate the workout is complete
                timerTextView.text = "Workout Complete!"
            }
        }


        // Start the initial timer after a 5-second countdown
        timerTextView.text = "Starting in 5 seconds..."
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerTextView.text = "Starting in: ${millisUntilFinished / 1000} sec"
            }

            override fun onFinish() {
                startNextTimer()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
