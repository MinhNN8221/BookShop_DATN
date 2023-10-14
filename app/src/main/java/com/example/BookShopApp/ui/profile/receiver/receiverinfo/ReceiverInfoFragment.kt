package com.example.BookShopApp.ui.profile.receiver.receiverinfo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.BookShopApp.data.model.Receiver
import com.example.BookShopApp.databinding.FragmentReceiverInfoBinding
import com.example.BookShopApp.utils.AlertMessageViewer

class ReceiverInfoFragment : Fragment() {

    companion object {
        fun newInstance() = ReceiverInfoFragment()
    }

    private lateinit var viewModel: ReceiverInfoViewModel
    private var binding: FragmentReceiverInfoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentReceiverInfoBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ReceiverInfoViewModel::class.java]
        initViewModel()
//        viewModel.getCustomer()
        binding?.apply {
            textUpdate.setOnClickListener {
                val name = editFullname.text.toString()
                val address = editAddress.text.toString()
                val mobPhone = editPhoneNumber.text.toString()
                val receiverInfo = Receiver(
                    receiverName = name,
                    receiverPhone = mobPhone,
                    receiverAddress = address
                )
                viewModel.checkFields(receiverInfo)
            }
            imageLeft.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun initViewModel() {
        viewModel.messageAddReceiver.observe(viewLifecycleOwner) {
            AlertMessageViewer.showAlertDialogMessage(requireContext(), it)
        }
//        viewModel.profile.observe(viewLifecycleOwner) {
//            it?.let {
//                binding?.apply {
//                    editFullname.setText(it.name)
//                    editAddress.setText(it.address)
//                    editPhoneNumber.setText(it.mobPhone)
//                }
//            }
//            loadingProgressBar.cancel()
//        }
    }
}