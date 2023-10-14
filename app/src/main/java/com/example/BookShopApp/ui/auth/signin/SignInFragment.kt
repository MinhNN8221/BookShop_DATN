package com.example.BookShopApp.ui.auth.signin

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.addCallback
import com.example.BookShopApp.R
import com.example.BookShopApp.data.api.RetrofitClient
import com.example.BookShopApp.data.model.Customer
import com.example.BookShopApp.data.model.response.auth.AuthResponse
import com.example.BookShopApp.databinding.FragmentSignInBinding
import com.example.BookShopApp.ui.auth.forgot.ForgotPasswordFragment
import com.example.BookShopApp.ui.auth.signup.SignUpFragment
import com.example.BookShopApp.ui.main.MainMenuFragment
import com.example.BookShopApp.ui.profile.profilesignin.ProfileSignInFragment
import com.example.BookShopApp.utils.AlertMessageViewer
import com.example.BookShopApp.utils.LoadingProgressBar
import com.example.BookShopApp.utils.MySharedPreferences

class SignInFragment : Fragment() {

    companion object {
        fun newInstance() = SignInFragment()
    }

    private lateinit var viewModel: SignInViewModel
    private var binding: FragmentSignInBinding? = null
    private var checkVisible = false
    private lateinit var loadingProgressBar: LoadingProgressBar
    private var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SignInViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        val accessToken = MySharedPreferences.getAccessToken(null)
        val expiresIn = MySharedPreferences.getLogInTime("ExpiresIn", 0)
        val loginTimeFirst = MySharedPreferences.getLogInTime("FirstTime", 0)
        if (accessToken != null) {
            val loginTime = System.currentTimeMillis()
            navToMainScreen()
            RetrofitClient.updateAccessToken(accessToken)
//            Log.d("LoginTimeFIRST", loginTimeFirst.toString())
//            Log.d("LoginTIME", loginTime.toString())
//            Log.d("LoginTime", (loginTime - loginTimeFirst).toString())
            if ((loginTime - loginTimeFirst) > expiresIn) {
                MySharedPreferences.clearPreferences()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, ProfileSignInFragment()).commit()
                AlertMessageViewer.showAlertDialogMessage(
                    requireContext(),
                    resources.getString(R.string.messageExpiresIn)
                )
            }
        }
        loadingProgressBar = LoadingProgressBar(requireContext())
        binding?.apply {
            layoutSignIn.setOnTouchListener { view, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    val event =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    event.hideSoftInputFromWindow(requireView().windowToken, 0)
                }
                false
            }
            buttonLogin.setOnClickListener {
                val username = editUsername.text.toString()
                val password = editPassword.text.toString()
                val customer = Customer(email = username, password = password)
                val user = AuthResponse(customer = customer)
                viewModel.checkFields(user)
                loadingProgressBar.show()
            }
            textForgotpass.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, ForgotPasswordFragment())
                    .addToBackStack("SignIn")
                    .commit()
            }
            textRegister.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, SignUpFragment())
                    .addToBackStack("SignIn")
                    .commit()
            }
            imageEye.setOnClickListener {
                val cursorPosition = editPassword.selectionEnd
                if (!checkVisible) {
                    editPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    checkVisible = true
                    imageEye.setImageResource(R.drawable.ic_hide_eye)
                } else {
                    editPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    checkVisible = false
                    imageEye.setImageResource(R.drawable.ic_visible_eye)
                }
                if (cursorPosition >= 0) {
                    editPassword.setSelection(cursorPosition)
                }
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

    fun initViewModel() {
        viewModel.loginResponse.observe(viewLifecycleOwner) {
            loadingProgressBar.cancel()
            if (it?.loginResponse == null) {
                it.error.message.let { it1 ->
                    AlertMessageViewer.showAlertDialogMessage(
                        requireContext(),
                        it1
                    )
                }
            } else {
                navToMainScreen()
                MySharedPreferences.putAccessToken(it.loginResponse.accessToken)
                val expiresIn = it.loginResponse.expiresIn.split(" ")[0].toLong()
                MySharedPreferences.putLogInTime("ExpiresIn", expiresIn * 60 * 60 * 1000)
                MySharedPreferences.putLogInTime("FirstTime", System.currentTimeMillis())
                RetrofitClient.updateAccessToken(it.loginResponse.accessToken)
                it.loginResponse.customer.customerId?.let { idCustomer ->
                    MySharedPreferences.putInt(
                        "idCustomer",
                        idCustomer
                    )
                }
            }
        }
    }

    fun navToMainScreen() {
        parentFragmentManager.beginTransaction().replace(R.id.container, MainMenuFragment())
            .commit()
    }
}