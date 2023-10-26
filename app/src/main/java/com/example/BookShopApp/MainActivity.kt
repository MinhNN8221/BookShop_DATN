package com.example.BookShopApp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.BookShopApp.databinding.ActivityMainBinding
import com.example.BookShopApp.ui.auth.signin.SignInFragment
import com.example.BookShopApp.ui.checkout.CheckOutFragment
import com.example.BookShopApp.ui.onboarding.OnboardingFragment
import com.example.BookShopApp.utils.MySharedPreferences


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        MySharedPreferences.init(this)
        val support = supportFragmentManager.beginTransaction()
        if (isFirstLaunch()) {
            val fragmentOnboard = OnboardingFragment()
            support.replace(R.id.container, fragmentOnboard).commit()
            setFirstLaunch(false)
        } else {
            val fragmentSignIn = SignInFragment()
            support.replace(R.id.container, fragmentSignIn).commit()
        }
    }

    private fun isFirstLaunch(): Boolean {
        return MySharedPreferences.getBoolean("firstLaunch", true)
    }

    private fun setFirstLaunch(isFirstTime: Boolean) {
        MySharedPreferences.putBoolean("firstLaunch", isFirstTime)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val fragment: CheckOutFragment? =
            supportFragmentManager.findFragmentById(R.id.container) as CheckOutFragment?
        fragment?.let {
            intent?.let { it1 -> fragment.handleNewIntent(it1) }
        }
    }
}