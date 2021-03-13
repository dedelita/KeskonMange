package com.example.keskonmange.main

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.keskonmange.R
import com.example.keskonmange.fragment.BarFragment
import com.example.keskonmange.fragment.MaisonFragment
import com.example.keskonmange.fragment.NewRestoDialogFragment
import com.example.keskonmange.adapter.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), BarFragment.OnListFragmentInteractionListener, MaisonFragment.OnListFragmentInteractionListener, NewRestoDialogFragment.OnFragmentInteractionListener, NewRestoDialogFragment.DialogListener {

    private val tabTitles = listOf(
        R.string.tab_text_2,
        R.string.tab_text_1
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        viewPager.isUserInputEnabled = false
        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = getString(tabTitles[position])
        }.attach()
    }

    override fun onListFragmentInteraction(item: Resto?) {
    }

    override fun onFinishEditDialog(inputText: String) {
    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}