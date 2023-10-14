package com.example.BookShopApp.ui.profile.receiver.receiver

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.BookShopApp.R
import com.example.BookShopApp.data.model.Receiver
import com.example.BookShopApp.databinding.FragmentReceiversBinding
import com.example.BookShopApp.ui.adapter.OnItemClickListener
import com.example.BookShopApp.ui.adapter.ReceiversAdapter
import com.example.BookShopApp.ui.profile.receiver.receiverinfo.ReceiverInfoFragment

class ReceiversFragment : Fragment() {
    companion object {
        fun newInstance() = ReceiversFragment()
    }

    private lateinit var viewModel: ReceiversViewModel
    private var binding: FragmentReceiversBinding? = null
    private var receivers = mutableListOf<Receiver>()
    private lateinit var adapter: ReceiversAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentReceiversBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ReceiversViewModel::class.java]
        adapter = ReceiversAdapter()
        initViewModel()
        viewModel.getReceivers()
        binding?.apply {
            recyclerReceivers.layoutManager = LinearLayoutManager(context)
            recyclerReceivers.adapter = adapter
            imageLeft.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, ReceiverInfoFragment())
                    .addToBackStack("Receivers")
                    .commit()
            }
        })
    }

    private fun initViewModel() {
        viewModel.receivers.observe(viewLifecycleOwner) {
            receivers.addAll(it)
            adapter.setData(it)
        }
    }
}