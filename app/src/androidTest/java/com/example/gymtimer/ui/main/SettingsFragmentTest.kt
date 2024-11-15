package com.example.gymtimer.ui.main

import androidx.fragment.app.testing.launchFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsFragmentTest {

    @Test
    fun testFragmentCreation() {
        val scenario = launchFragment<SettingsFragment>()
        scenario.onFragment { fragment ->
            assertNotNull(fragment.view)
        }
    }
}
