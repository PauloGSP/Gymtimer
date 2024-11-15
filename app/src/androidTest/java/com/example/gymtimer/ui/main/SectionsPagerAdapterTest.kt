package com.example.gymtimer.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gymtimer.R
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SectionsPagerAdapterTest {

    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    private lateinit var fragmentManager: FragmentManager
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()

        // Use ActivityScenario to provide a valid FragmentActivity with a prepared Looper
        val scenario = ActivityScenario.launch(FragmentActivity::class.java)
        scenario.onActivity { activity ->
            fragmentManager = activity.supportFragmentManager
            sectionsPagerAdapter = SectionsPagerAdapter(context, fragmentManager)
        }
    }

    @Test
    fun testGetCount() {
        val expectedCount = 3
        assertEquals(expectedCount, sectionsPagerAdapter.count)
    }

    @Test
    fun testGetItem() {
        val position = 0
        val fragment: Fragment = sectionsPagerAdapter.getItem(position)
        assert(fragment is ClockFragment)
    }

    @Test
    fun testGetPageTitle() {
        val position = 1
        val expectedTitle = context.resources.getString(R.string.tab_text_1)
        assertEquals(expectedTitle, sectionsPagerAdapter.getPageTitle(position))
    }

    @Test
    fun testGetHomeFragment() {
        val homeFragment = sectionsPagerAdapter.getHomeFragment()
        assert(homeFragment is HomeFragment)
    }

    @Test
    fun testGetSettingsFragment() {
        val settingsFragment = sectionsPagerAdapter.getSettingsFragment()
        assert(settingsFragment is SettingsFragment)
    }
}
