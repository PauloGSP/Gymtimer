package com.example.gymtimer.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ClockPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ChronometerFragment() // Left tab
            1 -> SimpleTimerFragment() // Right tab
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }

    override fun getCount(): Int {
        return 2 // Two tabs: Chronometer and Timer
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Chronometer"
            1 -> "Timer"
            else -> null
        }
    }
}
