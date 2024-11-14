package com.example.gymtimer.ui.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.fragment.app.FragmentActivity
import androidx.test.core.app.ApplicationProvider
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    private lateinit var fragmentManager: FragmentManager

    @Before
    fun setup() {
        val activity = FragmentActivity() // Create a new FragmentActivity instance
        activity.supportFragmentManager.beginTransaction() // Initialize FragmentManager
        fragmentManager = activity.supportFragmentManager
    }

    @Test
    fun testFragmentCreation() {
        val fragment = HomeFragment() // Create an instance of the fragment
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.add(fragment, "homeFragment")
        transaction.commit()

        // Ensure the fragment is added to the FragmentManager and view is not null
        assertNotNull(fragment.view)
    }
}
