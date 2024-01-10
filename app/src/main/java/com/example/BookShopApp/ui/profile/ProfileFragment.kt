package com.example.BookShopApp.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.BookShopApp.R
import com.example.BookShopApp.data.model.Customer
import com.example.BookShopApp.databinding.FragmentProfileBinding
import com.example.BookShopApp.databinding.LayoutAlertBinding
import com.example.BookShopApp.ui.order.orderhistory.OrderHistoryFragment
import com.example.BookShopApp.ui.profile.receiver.receiverinfo.ReceiverInfoFragment
import com.example.BookShopApp.ui.profile.changepass.ChangePassFragment
import com.example.BookShopApp.ui.profile.profilesignin.ProfileSignInFragment
import com.example.BookShopApp.ui.profile.updateprofile.UpdateProfileFragment
import com.example.BookShopApp.utils.AlertMessageViewer
import com.example.BookShopApp.utils.LoadingProgressBar
import com.example.BookShopApp.utils.MySharedPreferences

class ProfileFragment : Fragment() {
    private var binding: FragmentProfileBinding? = null

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var loadingProgressBar: LoadingProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this, ProfileViewModelFactory(requireActivity().application)
        )[ProfileViewModel::class.java]
        loadingProgressBar = LoadingProgressBar(requireContext())
        val bindingAlert = LayoutAlertBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogTheme)
            .setView(bindingAlert.root)
        val dialog = builder.create()
        dialog.setCancelable(false)
        loadingProgressBar.show()
        var email = ""
        activity?.let { MySharedPreferences.init(it.applicationContext) }
        viewModel.profile.observe(viewLifecycleOwner, Observer {
            bindData(it)
            email = it.email.toString()
        })
        viewModel.getCustomer()
        binding?.apply {
            imageLeft.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            imageUpdate.setOnClickListener {
                val fragmentUpdate = UpdateProfileFragment()
                parentFragmentManager.beginTransaction().replace(R.id.container, fragmentUpdate)
                    .addToBackStack("profile").commit()
            }
            textClear.setOnClickListener {
                bindingAlert.textDescription.text = resources.getString(R.string.clearData)
                bindingAlert.textConfirm.setOnClickListener {
                    MySharedPreferences.clearDataCache()
                    viewModel.clearDatabase()
                    Toast.makeText(context, "CLEAR SUCCESSFUL", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                bindingAlert.textCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
            textLogout.setOnClickListener {
                bindingAlert.textDescription.text = resources.getString(R.string.sign_out_message)
                bindingAlert.textConfirm.setOnClickListener {
                    MySharedPreferences.clearPreferences()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, ProfileSignInFragment()).commit()
                    dialog.dismiss()
                }
                bindingAlert.textCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
            textChangePassword.setOnClickListener {
                val fragmentChangePass = ChangePassFragment()
                val bundle = Bundle()
                bundle.putString("email", email)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, fragmentChangePass.apply { arguments = bundle })
                    .addToBackStack("profile").commit()
            }
            linearReceiverAddress.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, ReceiverInfoFragment()).addToBackStack("profile")
                    .commit()
            }
            linearMyOrder.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, OrderHistoryFragment()).addToBackStack("profile")
                    .commit()
            }
            linearFeedback.setOnClickListener {
                AlertMessageViewer.showAlertDialogMessage(
                    requireContext(),
                    "Chức năng đang được phát triển"
                )
            }
        }
    }

    private fun bindData(profile: Customer) {
        val imgAvatar = MySharedPreferences.getString("imageAvatar", "")
        if (imgAvatar != "") {
            binding?.apply {
                Glide.with(root).load(imgAvatar).centerCrop().into(imageAvatar)
            }
        } else {
            if (profile.avatar == null) {
                binding?.imageAvatar?.setImageResource(R.drawable.account_profile)
            } else {
                binding?.apply {
                    Glide.with(root).load(profile.avatar).centerCrop().into(imageAvatar)
                }
            }
        }
        binding?.textName?.text = profile.name
        binding?.textMail?.text = profile.email
        loadingProgressBar.cancel()
    }
}