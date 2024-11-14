package com.example.gymtimer.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.gymtimer.R

class CircularTimerView : View {

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

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

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
        }
    }

    fun getRemainingTime(): Long {
        return remainingTime
    }
}
