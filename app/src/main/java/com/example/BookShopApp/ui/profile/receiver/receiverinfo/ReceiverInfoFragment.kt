package com.example.BookShopApp.ui.profile.receiver.receiverinfo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import com.example.BookShopApp.R
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
        val receiver = arguments?.getSerializable("receiver") as Receiver?
        receiver?.let {
            bindData(it)
            binding?.textUpdate?.text = resources.getString(R.string.update)
            binding?.textAddressInfo?.text="Địa chỉ"
        }
        binding?.apply {
            switchAddressDefalut.setOnCheckedChangeListener { buttonView, isChecked ->
                if(receiver?.isSelected==1) {
                    AlertMessageViewer.showAlertDialogMessage(
                        requireContext(),
                        "Để hủy địa chỉ mặc định này cần thêm địa chỉ khác làm địa chỉ mặc đinh!"
                    )
                    switchAddressDefalut.isChecked = true
                }
            }
            textUpdate.setOnClickListener {
                val name = editFullname.text.toString()
                val address = editAddress.text.toString()
                val mobPhone = editPhoneNumber.text.toString()
                var isDefault=0;
                if(switchAddressDefalut.isChecked)
                    isDefault = 1
                if (receiver == null) {
                    val receiverInfo = Receiver(
                        receiverName = name,
                        receiverPhone = mobPhone,
                        receiverAddress = address,
                        isDefault = isDefault
                    )
                    viewModel.checkFields(receiverInfo, false)
                } else {
                    val receiverInfo = Receiver(
                        receiverId = receiver?.receiverId,
                        receiverName = name,
                        receiverPhone = mobPhone,
                        receiverAddress = address,
                        isDefault = isDefault,
                        isSelected = receiver?.isSelected
                    )
                    viewModel.checkFields(receiverInfo, true)
                }
                parentFragmentManager.popBackStack()
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

    private fun bindData(receiver: Receiver?) {
        binding?.apply {
            receiver?.let {
                editFullname.setText(it.receiverName)
                editAddress.setText(it.receiverAddress)
                editPhoneNumber.setText(it.receiverPhone)
                if (it.isDefault == 1) {
                    switchAddressDefalut.isChecked = true
                }
            }
        }
    }
}