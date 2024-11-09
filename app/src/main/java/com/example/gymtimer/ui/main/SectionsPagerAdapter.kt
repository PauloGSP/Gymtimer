package com.example.gymtimer.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.gymtimer.R
import com.example.gymtimer.ui.main.HomeFragment
import com.example.gymtimer.ui.main.ClockFragment
import com.example.gymtimer.ui.main.SettingsFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,  // Home
    R.string.tab_text_2,  // Settings
    R.string.tab_text_3   // Another tab, e.g., Profile or Logs
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()  // Replace with your actual home screen fragment class
            1 -> ClockFragment()  // Replace with your actual settings fragment class
            2 -> SettingsFragment()
            else -> HomeFragment.newInstance(position + 1)
        }
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 3
    }
}