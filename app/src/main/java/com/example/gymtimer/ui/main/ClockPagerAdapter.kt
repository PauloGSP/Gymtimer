package com.example.gymtimer.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ClockPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ChronometerFragment() // Left tab
            1 -> SimpleTimerFragment() // Right tab
            else -> ChronometerFragment()
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
