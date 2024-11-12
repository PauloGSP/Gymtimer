package com.example.gymtimer.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.gymtimer.R

class CircularTimerView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var totalTime: Long = 0L
    private var remainingTime: Long = 0L
    private var initialStrokeWidth = 50f // Initial width for the timer circle
    private var minStrokeWidth = 25f // Minimum width for the timer circle

    private var primaryPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.primaryPurple) // Primary color
        style = Paint.Style.STROKE
        strokeWidth = initialStrokeWidth
        isAntiAlias = true
    }

    private var secondaryPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.secondaryLightPurple) // Secondary color for thinner stroke
        style = Paint.Style.STROKE
        strokeWidth = minStrokeWidth
        isAntiAlias = true
    }

    private var textPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.text) // Dynamic text color based on theme
        textSize = 60f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    fun setTotalTime(totalTime: Long) {
        this.totalTime = totalTime
        invalidate()
    }

    fun updateProgress(remainingTime: Long) {
        this.remainingTime = remainingTime
        invalidate()
    }

    fun reset() {
        remainingTime = 0L
        primaryPaint.strokeWidth = initialStrokeWidth
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (totalTime > 0) {
            val fractionPassed = 1 - (remainingTime.toFloat() / totalTime)

            // Calculate the angle of the circle that should have the thinner stroke width
            val thinnerStrokeAngle = 360 * fractionPassed

            // Draw the part with the secondary (thinner) stroke width
            canvas.drawArc(
                50f, 50f, width - 50f, height - 50f,
                -90f, thinnerStrokeAngle, false, secondaryPaint
            )

            // Draw the remaining part with the initial (thicker) stroke width
            canvas.drawArc(
                50f, 50f, width - 50f, height - 50f,
                -90f + thinnerStrokeAngle, 360 - thinnerStrokeAngle, false, primaryPaint
            )

            // Draw remaining time at the center of the circle
            //val remainingTimeText = formatTime(remainingTime)
            //canvas.drawText(remainingTimeText, width / 2f, height / 2f + textPaint.textSize / 3, textPaint)
        }
    }

    private fun formatTime(timeMillis: Long): String {
        val totalSeconds = timeMillis / 1000
        val seconds = totalSeconds % 60
        val minutes = (totalSeconds / 60) % 60
        val hours = totalSeconds / 3600
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
