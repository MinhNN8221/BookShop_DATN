package com.example.BookShopApp.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.BookShopApp.R
import com.example.BookShopApp.databinding.FragmentOnboardingBinding
import com.example.BookShopApp.ui.auth.signin.SignInFragment

class OnboardingFragment : Fragment() {

    companion object {
        fun newInstance() = OnboardingFragment()
    }

    private var binding:FragmentOnboardingBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentOnboardingBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.textStart?.setOnClickListener {
            val fragment= SignInFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
        }
    }
}