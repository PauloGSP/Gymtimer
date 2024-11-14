package com.example.gymtimer.data

import com.example.gymtimer.ui.main.Rest
import org.junit.Assert.assertEquals
import org.junit.Test

class RestTest {

    @Test
    fun testRestInitialization() {
        val rest = Rest( 60)

        assertEquals(60, rest.duration)
    }
}
