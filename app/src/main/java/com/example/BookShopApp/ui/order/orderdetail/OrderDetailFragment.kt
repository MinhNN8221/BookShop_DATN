package com.example.BookShopApp.ui.order.orderdetail

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import com.example.BookShopApp.data.model.OrderDetail
import com.example.BookShopApp.data.model.response.order.OrderDetailProduct
import com.example.BookShopApp.databinding.FragmentOrderDetailBinding
import com.example.BookShopApp.databinding.LayoutAlertBinding
import com.example.BookShopApp.ui.adapter.OrderDetailAdapter
import com.example.BookShopApp.ui.order.ratingorder.RatingOrderFragment
import com.example.BookShopApp.ui.profile.profilesignin.ProfileSignInFragment
import com.example.BookShopApp.utils.AlertMessageViewer
import com.example.BookShopApp.utils.MySharedPreferences
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
        val bindingAlert = LayoutAlertBinding.inflate(LayoutInflater.from(context))
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogTheme)
            .setView(bindingAlert.root)
        val dialog = builder.create()
        dialog.setCancelable(false)
        val orderId = arguments?.getString("orderId")?.toInt()
        val orderStatus = arguments?.getString("orderStatus")
        val isRating = arguments?.getString("isRating")?.toInt()
        if(orderStatus.equals("Đang chuẩn bị")){
            binding?.textCancelOrder?.visibility=View.VISIBLE
        }
//        if(orderStatus.equals("Đã giao hàng") && isRating!=1){
//            binding?.textRatingOrder?.visibility=View.VISIBLE
//        }
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
            textCancelOrder.setOnClickListener {
                bindingAlert.textDescription.text = "Bạn chắc chắn muốn hủy đơn hàng này!"
                bindingAlert.textConfirm.setOnClickListener {
                    orderId?.let { id -> viewModel.updateOrderStatus(id, 4) }
                    textOrderStatus.text="Đã hủy"
                    binding?.textCancelOrder?.visibility=View.GONE
                    dialog.dismiss()
                }
                bindingAlert.textCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
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
            var totalProduct = 0
            for (product: OrderDetailProduct in it.products) {
                product.quantity?.let { totalProduct += it }

            }
            textOrderSum.text =
                textOrderSum.text.toString() + " " + totalProduct
            textOrderStatus.text = " $orderStatus"
            textTotalMoney.text = " " + it.orderTotal?.let { orderTotal ->
                formatMoney.formatMoney(
                    orderTotal.toDouble().toLong()
                )
            }

            textPaymentMethod.text =
                textPaymentMethod.text.toString() + " " + it.paymentMethod?.substring(16)
                    ?.capitalize()
            textShippingType.text = textShippingType.text.toString() + " " + it.shippingType
            loadingLayout.root.visibility = View.INVISIBLE
        }
    }
}