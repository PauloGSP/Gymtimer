package com.example.gymtimer.ui.main

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gymtimer.ui.main.TimerActivity
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TimerActivityTest {

    @Test
    fun testActivityLaunch() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), TimerActivity::class.java)
        val scenario = ActivityScenario.launch<TimerActivity>(intent)
        scenario.onActivity { activity ->
            assertNotNull(activity)
        }
    }
}
