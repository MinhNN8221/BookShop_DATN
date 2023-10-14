package com.example.BookShopApp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.BookShopApp.R
import com.example.BookShopApp.databinding.FragmentMainMenuBinding
import com.example.BookShopApp.ui.adapter.ViewPager2Adapter
import com.example.BookShopApp.ui.main.home.HomeFragment
import com.example.BookShopApp.ui.main.search.SearchFragment
import com.example.BookShopApp.ui.main.cart.CartFragment
import com.example.BookShopApp.ui.main.wishlist.WishlistFragment

class MainMenuFragment : Fragment() {
    private lateinit var binding: FragmentMainMenuBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainMenuBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        loadFragment(HomeFragment())
        val fragments = listOf(
            HomeFragment(),
            SearchFragment(),
            WishlistFragment(),
            CartFragment()
        )
        val adapter = ViewPager2Adapter(requireActivity(), fragments)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> binding.navigation.menu.findItem(R.id.menu_home).isChecked = true
                    1 -> binding.navigation.menu.findItem(R.id.menu_search).isChecked = true
                    2 -> binding.navigation.menu.findItem(R.id.menu_wishlist).isChecked = true
                    3 -> binding.navigation.menu.findItem(R.id.menu_cart).isChecked = true
                }
            }
        })
        binding.apply {
            navigation.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_home -> viewPager.currentItem = 0
                    R.id.menu_search -> viewPager.currentItem = 1
                    R.id.menu_wishlist -> viewPager.currentItem = 2
                    R.id.menu_cart -> viewPager.currentItem = 3
                }
                false
            }
        }
    }
}