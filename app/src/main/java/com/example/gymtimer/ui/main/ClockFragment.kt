package com.example.gymtimer.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.gymtimer.R
import com.google.android.material.tabs.TabLayout

class ClockFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_clock, container, false)

        val viewPager: ViewPager = view.findViewById(R.id.viewPagerClock)
        val tabs: TabLayout = view.findViewById(R.id.tabsClock)

        val clockPagerAdapter = ClockPagerAdapter(childFragmentManager)
        viewPager.adapter = clockPagerAdapter
        tabs.setupWithViewPager(viewPager)

        return view
    }
}
