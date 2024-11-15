package com.example.gymtimer.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gymtimer.R
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LapAdapterTest {

    private lateinit var context: Context
    private lateinit var lapAdapter: LapAdapter

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        val lapList = listOf(
            Pair("Lap 1", "00:45"),
            Pair("Lap 2", "01:10"),
            Pair("Lap 3", "00:55")
        )
        lapAdapter = LapAdapter(lapList)
    }

    @Test
    fun testGetItemCount() {
        // Verify the item count
        assertEquals(3, lapAdapter.itemCount)
    }

    @Test
    fun testOnBindViewHolder() {
        // Create a parent layout to host the view holder
        val parent = FrameLayout(context)

        // Inflate the layout for the ViewHolder to properly test the binding
        val view = LayoutInflater.from(context).inflate(R.layout.lap_item, parent, false)

        // Create ViewHolder instance
        val viewHolder = lapAdapter.LapViewHolder(view)

        // Bind the data
        lapAdapter.onBindViewHolder(viewHolder, 0)

        // Assertions to verify if the ViewHolder was correctly bound
        assertEquals("Lap 1", viewHolder.lapTextView.text.toString())
        assertEquals("00:45", viewHolder.lapTimeTextView.text.toString())
    }
}
