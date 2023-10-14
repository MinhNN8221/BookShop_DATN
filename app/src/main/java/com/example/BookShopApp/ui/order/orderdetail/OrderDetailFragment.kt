package com.example.BookShopApp.ui.order.orderdetail

import android.annotation.SuppressLint
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.BookShopApp.data.model.OrderDetail
import com.example.BookShopApp.databinding.FragmentOrderDetailBinding
import com.example.BookShopApp.ui.adapter.OrderDetailAdapter
import com.example.BookShopApp.utils.format.FormatDate
import com.example.BookShopApp.utils.format.FormatMoney

class OrderDetailFragment : Fragment() {

    private var binding: FragmentOrderDetailBinding? = null
    private var formatDate = FormatDate()
    private lateinit var viewModel: OrderDetailViewModel
    private lateinit var adapter: OrderDetailAdapter
    private val formatMoney = FormatMoney()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentOrderDetailBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OrderDetailViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = OrderDetailAdapter()
        binding?.loadingLayout?.root?.visibility = View.VISIBLE
        val orderId = arguments?.getString("orderId")?.toInt()
        val orderStatus = arguments?.getString("orderStatus")
        orderId?.let { orderId ->
            viewModel.orderDetailList.observe(viewLifecycleOwner, Observer { orderDetail ->
                adapter.setData(orderDetail.products)
                bindData(orderDetail, orderStatus.toString())
            })
            viewModel.getOrderDetails(orderId)
        }
        binding?.apply {
            recyclerOrderDetail.layoutManager = LinearLayoutManager(context)
            recyclerOrderDetail.adapter = adapter
            imageLeftOrder.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun bindData(it: OrderDetail, orderStatus: String) {
        binding?.apply {
            textIdOrder.text = "#Order" + it.orderId
            textOrderDate.text =
                textOrderDate.text.toString() + " " + formatDate.formatDate(it.createdOn)
            textOrderAddress.text =
                textOrderAddress.text.toString() + " " + it.address
            textOrderSum.text =
                textOrderSum.text.toString() + " " + it.products.size
            textOrderStatus.text = " $orderStatus"
            textTotalMoney.text = " " + it.orderTotal?.let { orderTotal ->
                formatMoney.formatMoney(
                    orderTotal.toDouble().toLong()
                )
            }
            textShippingType.text = textShippingType.text.toString() + " " + it.shippingType
            loadingLayout.root.visibility = View.INVISIBLE
        }
    }
}