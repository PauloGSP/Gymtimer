// SectionsPagerAdapter.kt
package com.example.gymtimer.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.gymtimer.R

private val TAB_TITLES = arrayOf(
    R.string.tab_text_2,  // Clock (left)
    R.string.tab_text_1,  // Home (middle)
    R.string.tab_text_3   // Settings (right)
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val homeFragment = HomeFragment() // Store HomeFragment instance
    private val settingsFragment = SettingsFragment() // Store SettingsFragment instance

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ClockFragment()       // Left tab (Clock)
            1 -> homeFragment          // Middle tab (Home)
            2 -> settingsFragment      // Right tab (Settings)
            else -> throw IllegalStateException("Unexpected tab position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3 // Show 3 total pages
    }

    fun getHomeFragment(): HomeFragment {
        return homeFragment
    }

    fun getSettingsFragment(): SettingsFragment {
        return settingsFragment
    }
}
