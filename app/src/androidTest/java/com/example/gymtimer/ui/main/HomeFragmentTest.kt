package com.example.gymtimer.ui.main

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gymtimer.R
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    @Before
    fun setup() {
        // No need to manually create FragmentActivity, using FragmentScenario handles that for us
    }

    @Test
    fun testFragmentCreation() {
        // Launch the fragment using `launchFragmentInContainer` with a specific theme
        val scenario = launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_Gymtimer)
        scenario.onFragment { fragment ->
            // Ensure the fragment is added to the FragmentManager and view is not null
            assertNotNull(fragment.view)
        }
    }
}
