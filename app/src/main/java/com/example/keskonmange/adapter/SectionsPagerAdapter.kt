package com.example.keskonmange.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import com.example.keskonmange.fragment.MaisonFragment
import com.example.keskonmange.fragment.BarFragment
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */

class SectionsPagerAdapter(fa: FragmentActivity) :
    FragmentStateAdapter(fa) {
    private val NUM_TABS = 2

    override fun createFragment(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        when(position) {
            0 -> return MaisonFragment()
            1 -> return BarFragment()
            else -> return Fragment()
        }
    }

    override fun getItemCount(): Int {
        return NUM_TABS
    }
}