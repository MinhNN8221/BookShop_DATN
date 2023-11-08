package com.example.BookShopApp.ui.main.cart

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.BookShopApp.R
import com.example.BookShopApp.databinding.FragmentCartBinding
import com.example.BookShopApp.ui.adapter.CartAdapter
import com.example.BookShopApp.ui.adapter.OnItemClickListener
import com.example.BookShopApp.ui.checkout.CheckOutFragment
import com.example.BookShopApp.ui.profile.ProfileFragment
import com.example.BookShopApp.utils.AlertMessageViewer
import com.example.BookShopApp.utils.format.FormatMoney

class CartFragment : Fragment() {

    companion object {
        fun newInstance() = CartFragment()
    }

    private lateinit var viewModel: CartViewModel
    private var binding: FragmentCartBinding? = null
    private lateinit var adapter: CartAdapter
    private var formatMoney = FormatMoney()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[CartViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = CartAdapter()
        initViewModel()
        viewModel.getAllCartItem()
        binding?.apply {
            swipeRefresh.setOnRefreshListener {
                Handler().postDelayed({
                    swipeRefresh.isRefreshing = false
                    viewModel.getAllCartItem()
                }, 1000)
            }
            swipeRefresh.setColorSchemeColors(resources.getColor(R.color.teal_200))
            imageProfile.setOnClickListener {
                parentFragmentManager.beginTransaction().replace(R.id.container, ProfileFragment())
                    .addToBackStack("CartFragment").commit()
            }
            textDeleteCart.setOnClickListener {
                viewModel.deleteAllItemCart()
                Handler().postDelayed({
                    onResume()
                }, 500)
            }
            recyclerCartItem.layoutManager = LinearLayoutManager(context)
            recyclerCartItem.adapter = adapter
        }
    }

    private fun initViewModel() {
        viewModel.cartItem.observe(viewLifecycleOwner) { cartItem ->
            adapter.setData(cartItem)
            increProductQuantity()
            decreProductQuantity()
            binding?.apply {
                textPrice.text = formatMoney.formatMoney(adapter.getTotalPrice().toLong())
                if (cartItem.isEmpty()) {
                    textCartInfor.visibility = View.VISIBLE
                    textDeleteCart.visibility = View.INVISIBLE
                    textCheckOut.setOnClickListener {
                        AlertMessageViewer.showAlertDialogMessage(
                            requireContext(),
                            "Bạn chưa chọn sản phẩm nào để mua!"
                        )
                    }
                } else {
                    textDeleteCart.visibility = View.VISIBLE
                    textCartInfor.visibility = View.INVISIBLE
                    textCheckOut.setOnClickListener {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.container, CheckOutFragment(), "CartFragment")
                            .addToBackStack("Cart").commit()
                    }
                }
            }
        }
        viewModel.message.observe(viewLifecycleOwner) {
            AlertMessageViewer.showAlertDialogMessage(requireContext(), it.message)
        }
    }

    private fun decreProductQuantity() {
        adapter.setOnDecreClickListener(object : OnItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClick(position: Int) {
                val cartItem = adapter.getItemCart(position)
                val itemId = cartItem.itemId
                val quantity = cartItem.quantity - 1
                val subTotalPrice =
                    cartItem.subTotal.toDouble() - (cartItem.discountedPrice?.toDouble() ?: 0.0)
                if (quantity > 0) {
                    itemId?.let { viewModel.changeProductQuantityInCart(it, quantity) }
                    adapter.setSubTotalPrice(subTotalPrice, position)
                    adapter.setQuantity(quantity, position)
                    adapter.setQuantityBook(1, position)
                    binding?.textPrice?.text =
                        formatMoney.formatMoney((adapter.getTotalPrice()).toLong())
                } else {
                    AlertDialog.Builder(requireContext())
                        .setTitle(null)
                        .setMessage("Bạn chắc chắn muốn bỏ sản phẩm này?")
                        .setCancelable(false)
                        .setPositiveButton("Đồng ý") { dialog, _ ->
                            itemId?.let { viewModel.removeItemInCart(it) }
                            adapter.setSubTotalPrice(subTotalPrice, position)
                            adapter.setQuantity(quantity, position)
                            adapter.removeData(position)
                            binding?.textPrice?.text =
                                formatMoney.formatMoney((adapter.getTotalPrice()).toLong())
                            dialog.cancel()
                        }
                        .setNegativeButton("Không") { dialog, _ ->
                            binding?.textPrice?.text =
                                formatMoney.formatMoney((adapter.getTotalPrice()).toLong())
                            dialog.cancel()
                        }
                        .show()
                }
            }
        })
    }

    private fun increProductQuantity() {
        adapter.setOnIncreClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val cartItem = adapter.getItemCart(position)
                if (adapter.getQuantityBook(position) - adapter.getQuantitySold(position) > 0) {
                    val itemId = cartItem.itemId
                    val quantity = cartItem.quantity + 1
                    val subTotalPrice =
                        cartItem.subTotal.toDouble() + (cartItem.discountedPrice?.toDouble() ?: 0.0)
                    adapter.setSubTotalPrice(subTotalPrice, position)
                    adapter.setQuantity(quantity, position)
                    adapter.setQuantityBook(-1, position)
                    itemId?.let { viewModel.changeProductQuantityInCart(it, quantity) }
                    binding?.textPrice?.text =
                        formatMoney.formatMoney((adapter.getTotalPrice()).toLong())
                } else {
                    AlertMessageViewer.showAlertDialogMessage(
                        requireContext(),
                        "Sản phẩm này tạm hết!"
                    )
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllCartItem()
    }
}