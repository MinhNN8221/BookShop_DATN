package com.example.BookShopApp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPager2Adapter(activity: FragmentActivity, private val fragements: List<Fragment>) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = fragements.size

    override fun createFragment(position: Int): Fragment = fragements[position]
}