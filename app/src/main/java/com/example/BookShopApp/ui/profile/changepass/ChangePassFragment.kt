package com.example.BookShopApp.ui.profile.changepass

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.example.BookShopApp.R
import com.example.BookShopApp.data.model.response.auth.AuthResponse
import com.example.BookShopApp.data.model.Customer
import com.example.BookShopApp.databinding.FragmentChangePassBinding
import com.example.BookShopApp.utils.AlertMessageViewer
import com.example.BookShopApp.utils.LoadingProgressBar

class ChangePassFragment : Fragment() {

    companion object {
        fun newInstance() = ChangePassFragment()
    }

    private lateinit var viewModel: ChangePassViewModel
    private var binding: FragmentChangePassBinding? = null
    private var checkVisible = false
    private var checkVisibleNewPass = false
    private var checkVisibleConfirmPass = false
    private lateinit var loadingProgressBar: LoadingProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentChangePassBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChangePassViewModel::class.java)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.imageLeft?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        loadingProgressBar=LoadingProgressBar(requireContext())
        initViewModel()
        binding?.apply {
            layoutChangePassword.setOnTouchListener { view, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    val event =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    event.hideSoftInputFromWindow(requireView().windowToken, 0)
                }
                false
            }
            textUpdatePassword.setOnClickListener {
                val email = arguments?.getString("email").toString()
                val oldPass = editCurrentPass.text.toString()
                val newPass = editNewPass.text.toString()
                val confirmPass = editConfirm.text.toString()
                val user = AuthResponse(
                    customer = Customer(
                        email = email,
                        password = oldPass,
                        newPassword = newPass,
                        passwordAgain = confirmPass
                    )
                )
                viewModel.checkFields(user)
                loadingProgressBar.show()
            }
            imageEyeCurrentPass.setOnClickListener {
                val cursorPosition = editCurrentPass.selectionEnd
                if (!checkVisible) {
                    editCurrentPass.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    checkVisible = true
                    imageEyeCurrentPass.setImageResource(R.drawable.ic_hide_eye)
                } else {
                    editCurrentPass.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    checkVisible = false
                    imageEyeCurrentPass.setImageResource(R.drawable.ic_visible_eye)
                }
                if (cursorPosition >= 0) {
                    editCurrentPass.setSelection(cursorPosition)
                }
            }
            imageEyeNewPass.setOnClickListener {
                val cursorPosition = editNewPass.selectionEnd
                if (!checkVisibleNewPass) {
                    editNewPass.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    checkVisibleNewPass = true
                    imageEyeNewPass.setImageResource(R.drawable.ic_hide_eye)
                } else {
                    editNewPass.transformationMethod = PasswordTransformationMethod.getInstance()
                    checkVisibleNewPass = false
                    imageEyeNewPass.setImageResource(R.drawable.ic_visible_eye)
                }
                if (cursorPosition >= 0) {
                    editNewPass.setSelection(cursorPosition)
                }
            }
            imageEyeConfirmPass.setOnClickListener {
                val cursorPosition = editConfirm.selectionEnd
                if (!checkVisibleConfirmPass) {
                    editConfirm.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    checkVisibleConfirmPass = true
                    imageEyeConfirmPass.setImageResource(R.drawable.ic_hide_eye)
                } else {
                    editConfirm.transformationMethod = PasswordTransformationMethod.getInstance()
                    checkVisibleConfirmPass = false
                    imageEyeConfirmPass.setImageResource(R.drawable.ic_visible_eye)
                }
                if (cursorPosition >= 0) {
                    editConfirm.setSelection(cursorPosition)
                }
            }
        }
    }

    private fun initViewModel() {
        viewModel.message.observe(viewLifecycleOwner){ message ->
            AlertMessageViewer.showAlertDialogMessage(requireContext(), message)
            loadingProgressBar.cancel()
        }
    }
}