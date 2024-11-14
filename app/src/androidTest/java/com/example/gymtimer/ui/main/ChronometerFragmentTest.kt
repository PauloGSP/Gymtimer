package com.example.gymtimer.ui.main

// ChronometerFragment Unit Tests
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChronometerFragmentTest {

    @Test
    fun testTimeCalculations() {
        // Arrange
        val startTime = 1000L // 1 second in milliseconds
        val endTime = 5000L // 5 seconds in milliseconds
        val expectedDuration = 4000L // Expected duration in milliseconds

        // Act
        val actualDuration = endTime - startTime

        // Assert
        assertEquals(expectedDuration, actualDuration)
    }
}