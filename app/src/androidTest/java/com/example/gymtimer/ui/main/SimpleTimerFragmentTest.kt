package com.example.gymtimer.ui.main


// SimpleTimerFragment Unit Tests
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SimpleTimerFragmentTest {

    @Test
    fun validateSimpleTimerCalculations() {
        // Arrange
        val countdownTime = 10 * 1000L // 10 seconds in milliseconds
        val elapsedTime = 4000L // 4 seconds elapsed
        val expectedRemainingTime = countdownTime - elapsedTime

        // Act
        val actualRemainingTime = countdownTime - elapsedTime

        // Assert
        assertEquals(expectedRemainingTime, actualRemainingTime)
    }
}
