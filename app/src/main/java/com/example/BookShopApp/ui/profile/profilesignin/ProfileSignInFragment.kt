package com.example.BookShopApp.ui.profile.profilesignin

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.FragmentManager
import com.example.BookShopApp.R
import com.example.BookShopApp.databinding.FragmentProfileSigninBinding
import com.example.BookShopApp.ui.auth.signin.SignInFragment
import com.example.BookShopApp.ui.auth.signup.SignUpFragment

class ProfileSignInFragment : Fragment() {

    private var binding: FragmentProfileSigninBinding? = null
    private var doubleBackToExitPressedOnce = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileSigninBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            textBtnSignin.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, SignInFragment())
                    .commit()
            }
            textCreate.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, SignUpFragment())
                    .commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (doubleBackToExitPressedOnce) {
                requireActivity().finish()
            } else {
                doubleBackToExitPressedOnce = true
                Toast.makeText(requireContext(), "Nhấn back lần nữa để thoát", Toast.LENGTH_SHORT)
                    .show()
                Handler().postDelayed({
                    doubleBackToExitPressedOnce = false
                }, 2500)
            }
        }
    }
}