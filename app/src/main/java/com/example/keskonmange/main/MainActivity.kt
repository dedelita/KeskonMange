package com.example.keskonmange.main

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.keskonmange.R
import com.example.keskonmange.fragment.BarFragment
import com.example.keskonmange.fragment.MaisonFragment
import com.example.keskonmange.fragment.NewRestoDialogFragment
import com.example.keskonmange.adapter.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), BarFragment.OnListFragmentInteractionListener, MaisonFragment.OnListFragmentInteractionListener, NewRestoDialogFragment.OnFragmentInteractionListener, NewRestoDialogFragment.DialogListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

    override fun onListFragmentInteraction(item: Resto?) {
    }

    override fun onFinishEditDialog(inputText: String) {
    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}