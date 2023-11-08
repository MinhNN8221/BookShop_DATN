package com.example.BookShopApp.ui.main.wishlist

import android.annotation.SuppressLint
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.BookShopApp.R
import com.example.BookShopApp.data.model.Wishlist
import com.example.BookShopApp.databinding.FragmentWishlistBinding
import com.example.BookShopApp.ui.adapter.OnItemClickListener
import com.example.BookShopApp.ui.adapter.WishListAdapter
import com.example.BookShopApp.ui.productdetail.ProductdetailViewModel
import com.example.BookShopApp.ui.profile.ProfileFragment
import com.example.BookShopApp.utils.AlertMessageViewer
import com.example.BookShopApp.utils.format.FormatMoney

class WishlistFragment : Fragment() {

    companion object {
        fun newInstance() = WishlistFragment()
    }

    private lateinit var viewModel: WishlistViewModel
    private lateinit var adapter: WishListAdapter
    private var binding: FragmentWishlistBinding? = null
    private lateinit var viewModelProduct: ProductdetailViewModel
    private var listItemWishList = mutableListOf<Wishlist>()
    private val formatMoney = FormatMoney()
    private var currentPage = 1
    private var lastPosition = 0
    private var totalPosition = 0
    private var currentPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentWishlistBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[WishlistViewModel::class.java]
        viewModelProduct = ViewModelProvider(this)[ProductdetailViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = WishListAdapter()
        initViewModel()
        viewModel.getWishList(10, 1, 100)
        binding?.apply {
            swipeRefresh.setOnRefreshListener {
                Handler().postDelayed({
                    swipeRefresh.isRefreshing = false
                    refreshData()
                }, 1000)
            }
            swipeRefresh.setColorSchemeColors(resources.getColor(R.color.teal_200))
            imageProfile.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, ProfileFragment())
                    .addToBackStack("WishListFragment")
                    .commit()
            }
            textAddToBag.setOnClickListener {
                viewModel.addAllItemToCart()
                Handler().postDelayed({
                    viewModel.getWishList(10, 1, 100);
                }, 500)
            }
            recyclerWishList.layoutManager = LinearLayoutManager(context)
            recyclerWishList.adapter = adapter
        }
//        handleLoadData()
        handleSwipeItem()
    }

    private fun handleSwipeItem() {
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val itemWishList = adapter.getBook(position)
                itemWishList.let {
                    viewModelProduct.removeItemInWishList(it.product_id)
                }
                listItemWishList.removeAt(position)
                adapter.removeData(position)
                binding?.textPrice?.text = formatMoney.formatMoney(adapter.getTotalPrice().toLong())
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding?.recyclerWishList)
    }

//    private fun handleLoadData() {
//        binding?.apply {
//            recyclerWishList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)
//                    lastPosition =
//                        (recyclerWishList.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
//                    totalPosition = adapter.itemCount
//                    if (lastPosition != currentPosition && lastPosition == totalPosition - 3) {
//                        currentPage++
//                        viewModel.getWishList(10, currentPage, 100)
//                        currentPosition = lastPosition
//                    }
//                }
//            })
//        }
//    }

    private fun refreshData() {
        viewModel.getWishList(10, 1, 100)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViewModel() {
        listItemWishList.clear()
        viewModel.wishList.observe(viewLifecycleOwner) {
            it.wishlist.let { wishList ->
                listItemWishList.addAll(wishList)
                adapter.setData(wishList)
                addItemToCart()
                binding?.textPrice?.text = formatMoney.formatMoney(adapter.getTotalPrice().toLong())
            }
        }
        viewModel.message.observe(viewLifecycleOwner) {
            AlertMessageViewer.showAlertDialogMessage(requireContext(), it.message)
        }
        viewModelProduct.messageRemove.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun addItemToCart() {
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val product = adapter.getBook(position)
                val quantityRemaining = product.quantity - product.quantitySold
                if (quantityRemaining > 0) {
                    viewModel.addItemToCart(product.product_id)
                    Handler().postDelayed({
                        viewModel.getWishList(10, 1, 100)
                    }, 500)
                    AlertMessageViewer.showAlertDialogMessage(
                        requireContext(),
                        "Đã thêm sản phẩm vào giỏ hàng"
                    )
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
        viewModel.getWishList(10, 1, 100)
    }
}