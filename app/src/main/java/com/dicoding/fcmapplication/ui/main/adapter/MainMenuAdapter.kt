package com.dicoding.fcmapplication.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainMenuAdapter(
    private val fragments: List<Fragment>,
    fa: FragmentActivity
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}