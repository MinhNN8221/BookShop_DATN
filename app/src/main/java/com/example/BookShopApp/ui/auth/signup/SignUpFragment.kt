package com.example.BookShopApp.ui.auth.signup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import com.example.BookShopApp.R
import com.example.BookShopApp.data.model.response.auth.AuthResponse
import com.example.BookShopApp.data.model.Customer
import com.example.BookShopApp.databinding.FragmentSignUpBinding
import com.example.BookShopApp.ui.auth.signin.SignInFragment
import com.example.BookShopApp.utils.AlertMessageViewer
import com.example.BookShopApp.utils.LoadingProgressBar

class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var viewModel: SignUpViewModel
    private var binding: FragmentSignUpBinding? = null
    private var doubleBackToExitPressedOnce = false
    private var checkVisiblePass = false
    private var checkVisibleConfirm = false
    private lateinit var loadingProgressBar: LoadingProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        initViewModel()
        loadingProgressBar = LoadingProgressBar(requireContext())
        binding?.apply {
            textLogin.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, SignInFragment())
                    .commit()
            }
            buttonRegister.setOnClickListener {
                val customer = Customer(
                    email = editEmail.text.toString(),
                    name = editName.text.toString(),
                    password = editPassword.text.toString(),
                    passwordAgain = editConfirmPassword.text.toString()
                )
                loadingProgressBar.show()
                val user = AuthResponse(customer = customer)
                viewModel.checkFields(user)
            }
            imageEyePassword.setOnClickListener {
                val cursorPosition = editPassword.selectionEnd
                if (!checkVisiblePass) {
                    editPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    checkVisiblePass = true
                    imageEyePassword.setImageResource(R.drawable.ic_hide_eye)
                } else {
                    editPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    checkVisiblePass = false
                    imageEyePassword.setImageResource(R.drawable.ic_visible_eye)
                }
                if (cursorPosition >= 0) {
                    editPassword.setSelection(cursorPosition)
                }
            }
            imageEyeConfirmPassword.setOnClickListener {
                val cursorPosition = editConfirmPassword.selectionEnd
                if (!checkVisibleConfirm) {
                    editConfirmPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    checkVisibleConfirm = true
                    imageEyeConfirmPassword.setImageResource(R.drawable.ic_hide_eye)
                } else {
                    editConfirmPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    imageEyeConfirmPassword.setImageResource(R.drawable.ic_visible_eye)
                    checkVisibleConfirm = false
                }
                if (cursorPosition >= 0) {
                    editConfirmPassword.setSelection(cursorPosition)
                }
            }
            textLogin.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, SignInFragment())
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

    private fun initViewModel() {
        viewModel.registerResponse.observe(viewLifecycleOwner) {
            loadingProgressBar.cancel()
            it?.let { authState ->
                AlertMessageViewer.showAlertDialogMessage(
                    requireContext(),
                    authState.error.message
                )
            }
        }
    }

}