package com.example.gymtimer.ui.main

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gymtimer.customviews.CircularTimerView
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class CircularTimerViewTest {

    private lateinit var circularTimerView: CircularTimerView

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        circularTimerView = CircularTimerView(context)
    }

    @Test
    fun testUpdateAndResetTimer() {
        // Arrange
        val expectedRemainingTime: Long = 0L

        // Act
        circularTimerView.reset()

        // Assert
        assertEquals(expectedRemainingTime, circularTimerView.getRemainingTime())
    }
}
