package com.example.gymtimer.ui.main

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ClockPagerAdapterTest {

    private lateinit var clockPagerAdapter: ClockPagerAdapter
    private lateinit var fragmentManager: FragmentManager

    @Before
    fun setup() {
        // Launch FragmentActivity using ActivityScenario to provide a FragmentManager
        val scenario = ActivityScenario.launch(FragmentActivity::class.java)
        scenario.onActivity { activity ->
            fragmentManager = activity.supportFragmentManager
            clockPagerAdapter = ClockPagerAdapter(fragmentManager)
        }
    }

    @Test
    fun testGetCount() {
        val expectedCount = 2
        assertEquals(expectedCount, clockPagerAdapter.count)
    }

    @Test
    fun testGetItem() {
        val position = 0
        val fragment = clockPagerAdapter.getItem(position)
        assert(fragment is ChronometerFragment)
    }
}
