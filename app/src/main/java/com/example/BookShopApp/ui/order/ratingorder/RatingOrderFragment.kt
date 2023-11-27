package com.example.BookShopApp.ui.order.ratingorder

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.BookShopApp.R
import com.example.BookShopApp.data.model.request.RatingRequest
import com.example.BookShopApp.data.model.response.order.OrderHistory
import com.example.BookShopApp.databinding.FragmentRatingOrderBinding
import com.example.BookShopApp.ui.adapter.OrderHistoryAdapter
import com.example.BookShopApp.ui.adapter.RatingAdapter
import com.example.BookShopApp.ui.order.orderdetail.OrderDetailViewModel
import com.example.BookShopApp.utils.AlertMessageViewer
import com.example.BookShopApp.utils.MySharedPreferences

class RatingOrderFragment : Fragment() {

    companion object {
        fun newInstance() = RatingOrderFragment()
    }

    private var binding: FragmentRatingOrderBinding? = null
    private lateinit var viewModel: RatingOrderViewModel
    private lateinit var viewModelOrderDetail: OrderDetailViewModel
    private lateinit var adapter: RatingAdapter
    private lateinit var adapterOrderHistory: OrderHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RatingOrderViewModel::class.java]
        viewModelOrderDetail = ViewModelProvider(this)[OrderDetailViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentRatingOrderBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        adapter = RatingAdapter(false)
        adapterOrderHistory = OrderHistoryAdapter()
        val orderId = arguments?.getString("orderId")?.toInt()
        val position=arguments?.getString("position")?.toInt()
        val listOrder=arguments?.getSerializable("listOrder") as List<OrderHistory>
        adapterOrderHistory.setData(listOrder)
        if (orderId != null) {
            viewModelOrderDetail.getOrderDetails(orderId)
        }
        binding?.apply {
            imageLeft.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            recyclerRatingList.adapter = adapter
            recyclerRatingList.layoutManager = LinearLayoutManager(context)
            textSendRating.setOnClickListener {
                val ratingList: MutableList<RatingRequest> = mutableListOf()
                for (i in 0 until adapter.itemCount) {
                    val comment = adapter.getComment(i)
                    val product = adapter.getRatingOrder(i)
                    val idUser = MySharedPreferences.getInt("idCustomer", 0)
                    val rating = RatingRequest(
                        comment = comment,
                        ratingLevel = adapter.getRating(i),
                        bookId = product.productId,
                        userId = idUser,
                        orderId = orderId,
                    )
                    ratingList.add(rating)
                }
                viewModel.createRatingOrder(ratingList)
                if (position != null) {
                    adapterOrderHistory.setOrderIsRating(position)
                }
            }
        }
    }

    private fun initViewModel() {
        viewModelOrderDetail.orderDetailList.observe(viewLifecycleOwner) { orderDetail ->
            adapter.setDataRatingOrder(orderDetail.products)
        }
        viewModel.message.observe(viewLifecycleOwner) {
            AlertDialog.Builder(context)
                .setTitle(null)
                .setMessage(it.message)
                .setCancelable(false)
                .setNegativeButton("Close"){ dialog, _ ->
                    parentFragmentManager.popBackStack()
                    dialog.cancel()
                }
                .show()
        }
    }
}