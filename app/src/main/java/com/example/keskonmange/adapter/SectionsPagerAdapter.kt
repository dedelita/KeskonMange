package com.example.keskonmange.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.keskonmange.fragment.MaisonFragment
import com.example.keskonmange.fragment.BarFragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * A [FragmentStateAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */

class SectionsPagerAdapter(fa: FragmentActivity) :
    FragmentStateAdapter(fa) {
    private val numTabs = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> MaisonFragment()
            1 -> BarFragment()
            else -> Fragment()
        }
    }

    override fun getItemCount(): Int {
        return numTabs
    }
}