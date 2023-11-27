package com.example.BookShopApp.ui.order.orderhistory

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.BookShopApp.R
import com.example.BookShopApp.data.model.Order
import com.example.BookShopApp.data.model.response.order.OrderHistory
import com.example.BookShopApp.databinding.FragmentOrderHistoryBinding
import com.example.BookShopApp.ui.adapter.OnItemClickListener
import com.example.BookShopApp.ui.adapter.OrderHistoryAdapter
import com.example.BookShopApp.ui.order.orderdetail.OrderDetailFragment
import com.example.BookShopApp.ui.order.ratingorder.RatingOrderFragment
import com.example.BookShopApp.utils.format.FormatDate
import java.time.LocalDateTime

class OrderHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = OrderHistoryFragment()
    }

    private var binding: FragmentOrderHistoryBinding? = null
    private lateinit var viewModel: OrderHistoryViewModel
    private lateinit var adapter: OrderHistoryAdapter
    private var formatDate = FormatDate()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentOrderHistoryBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[OrderHistoryViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = OrderHistoryAdapter()
        initViewModel()
        navToOrderDetail()
        binding?.apply {
            loadingLayout.root.visibility = View.VISIBLE
            viewModel.getOrderHistory()
            recyclerOrderHistory.layoutManager = LinearLayoutManager(context)
            recyclerOrderHistory.adapter = adapter
            imageLeftOrder.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViewModel() {
        val list = mutableListOf<OrderHistory>()
        val currentDate = formatDate.formatDate(LocalDateTime.now().toString())
        val mapOrder: MutableMap<String, MutableList<Order>> = mutableMapOf()
        viewModel.orderHistory.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mapOrder.clear()
                for (order in it) {
                    val date = formatDate.formatDate(order.createdOn)
                    if (date == currentDate) {
                        mapOrder.computeIfAbsent("Hôm nay") { mutableListOf() }.add(order)
                    } else {
                        mapOrder.computeIfAbsent(date) { mutableListOf() }.add(order)
                    }
                }
                list.clear()
                for ((key, value) in mapOrder) {
                    list.add(OrderHistory(key, null))
                    for (values in value) {
                        list.add(OrderHistory(null, values))
                    }
                }
                if (list.isEmpty()) {
                    binding?.textOrderHistory?.visibility = View.VISIBLE
                }
                adapter.setData(list)
                binding?.loadingLayout?.root?.visibility = View.INVISIBLE
            }
            navOrderRating(list)
        })
    }

    private fun navToOrderDetail() {
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val orderId = adapter.getOrder(position)?.orderId
                val orderStatus = adapter.getOrder(position)?.orderStatus
                val bundle = Bundle()
                bundle.putString("orderId", orderId.toString())
                bundle.putString("orderStatus", orderStatus)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, OrderDetailFragment().apply { arguments = bundle })
                    .addToBackStack("Orderhistory")
                    .commit()
            }
        })
    }

    private fun navOrderRating(listOrder:List<OrderHistory>) {
        adapter.setOnRatingItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val orderId = adapter.getOrder(position)?.orderId
                val bundle = Bundle()
                bundle.putString("orderId", orderId.toString())
                bundle.putString("position", position.toString())
                bundle.putSerializable("listOrder", ArrayList(listOrder))
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, RatingOrderFragment().apply { arguments = bundle })
                    .addToBackStack("OrderDetail").commit()
            }
        })
    }
}