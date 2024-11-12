// MainActivity.kt
package com.example.gymtimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.gymtimer.databinding.ActivityMainBinding
import com.example.gymtimer.ui.main.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    private val workoutClearedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Refresh the HomeFragment when workouts are cleared
            sectionsPagerAdapter.getHomeFragment().refreshWorkoutList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Initialize the adapter and ViewPager
        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        // Setup tabs
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        viewPager.currentItem = 1

        // Register the receiver to listen for "workouts_cleared" broadcasts
        LocalBroadcastManager.getInstance(this).registerReceiver(
            workoutClearedReceiver,
            IntentFilter("com.example.gymtimer.WORKOUTS_CLEARED")
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(workoutClearedReceiver)
    }
}
