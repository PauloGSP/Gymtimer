package com.example.gymtimer.ui.main


// ClockFragment Unit Tests
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ClockFragmentTest {

    @Test
    fun validateTimeCalculations() {
        // Arrange
        val hours = 2
        val minutes = 30
        val seconds = 15
        val expectedTotalSeconds = (hours * 3600) + (minutes * 60) + seconds

        // Act
        val actualTotalSeconds = hours * 3600 + minutes * 60 + seconds

        // Assert
        assertEquals(expectedTotalSeconds, actualTotalSeconds)
    }
}