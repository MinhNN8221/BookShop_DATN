package com.example.BookShopApp.ui.publisher

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.BookShopApp.R
import com.example.BookShopApp.data.model.Product
import com.example.BookShopApp.databinding.FragmentPublisherBinding
import com.example.BookShopApp.ui.adapter.BookListAdapter
import com.example.BookShopApp.ui.adapter.OnItemClickListener
import com.example.BookShopApp.ui.adapter.BookAdapter
import com.example.BookShopApp.ui.productdetail.ProductdetailFragment
import com.example.BookShopApp.utils.AlertMessageViewer
import com.example.BookShopApp.utils.ItemSpacingDecoration
import com.example.BookShopApp.utils.LoadingProgressBar

class PublisherFragment : Fragment() {

    private var binding: FragmentPublisherBinding? = null
    private lateinit var viewModel: PublisherViewModel
    private lateinit var adapter: BookAdapter
    private lateinit var adapterList: BookListAdapter
    private lateinit var loadingProgressBar: LoadingProgressBar
    private var bookList = mutableListOf<Product>()
    private var currentPage = 1
    private var lastPosition = 0
    private var totalPosition = 0
    private var currentPosition = 0
    private var pastPage = -1
    private val searchHandler = Handler(Looper.getMainLooper())
    private var isStateMenu = true

    companion object {
        fun newInstance() = PublisherFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPublisherBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[PublisherViewModel::class.java]
        adapter = BookAdapter(false)
        adapterList = BookListAdapter()
        loadingProgressBar = LoadingProgressBar(requireContext())
        loadingProgressBar.show()
        initViewModel()
        navToProductDetail()
//        addItemToCart()
        val supplierId = arguments?.getString("publisherId")?.toInt()
        supplierId?.let {
            viewModel.getProductsBySupplier(supplierId, 10, currentPage, 100)
            handleSearch(supplierId)
        }
        val horizontalSpacing =
            resources.getDimensionPixelSize(R.dimen.horizontal_spacing)
        val verticalSpacing =
            resources.getDimensionPixelSize(R.dimen.vertical_spacing)
        val itemDecoration = ItemSpacingDecoration(horizontalSpacing, verticalSpacing)
        handleMenu(itemDecoration)
        binding?.apply {
            imageLeft.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            layoutSupplier.setOnTouchListener { view, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    val event =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    event.hideSoftInputFromWindow(requireView().windowToken, 0)
                }
                false
            }
            swipeRefresh.setColorSchemeColors(resources.getColor(R.color.teal_200))
            swipeRefresh.setOnRefreshListener {
                Handler().postDelayed({
                    swipeRefresh.isRefreshing = false
                    supplierId?.let {
                        viewModel.getProductsBySupplier(it, 10, 1, 100)
                    }
                }, 1000)
            }

        }
        supplierId?.let { handleLoadData(it) }
    }

    private fun handleMenu(itemDecoration: ItemSpacingDecoration) {
        binding?.apply {
            if (isStateMenu) {
                recyclerProduct.adapter = adapter
                recyclerProduct.layoutManager = GridLayoutManager(context, 2)
                recyclerProduct.addItemDecoration(itemDecoration)
                isStateMenu = true
                imageMenu.setImageResource(R.drawable.ic_menu)
            } else {
                recyclerProduct.adapter = adapterList
                recyclerProduct.layoutManager = LinearLayoutManager(context)
                recyclerProduct.removeItemDecoration(itemDecoration)
                isStateMenu = false
                imageMenu.setImageResource(R.drawable.ic_microsoft)
            }
            imageMenu.setOnClickListener {
                if (isStateMenu) {
                    recyclerProduct.adapter = adapterList
                    recyclerProduct.layoutManager = LinearLayoutManager(context)
                    recyclerProduct.removeItemDecoration(itemDecoration)
                    isStateMenu = false
                    imageMenu.setImageResource(R.drawable.ic_microsoft)
                } else {
                    recyclerProduct.adapter = adapter
                    recyclerProduct.layoutManager = GridLayoutManager(context, 2)
                    recyclerProduct.addItemDecoration(itemDecoration)
                    isStateMenu = true
                    imageMenu.setImageResource(R.drawable.ic_menu)
                }
            }
        }
    }

    private fun initViewModel() {
        viewModel.productState.observe(viewLifecycleOwner) { state ->
            val isDefaultState = state.isDefaultState
            state.products?.let {
                if (it.isEmpty()) {
                    currentPage = 1
                } else {
                    if (pastPage != currentPage && isDefaultState) {
                        if (currentPage > 1) {
                            bookList.addAll(it)
                        } else {
                            bookList.clear()
                            bookList.addAll(it)
                        }
                    } else if (!isDefaultState) {
                        bookList.clear()
                        bookList.addAll(it)
                    }
                }
                adapter.setData(bookList)
                adapterList.setDataProduct(bookList)
                navToProductDetail()
//                addItemToCart()
                loadingProgressBar.cancel()
            }
        }
    }

    private fun navToProductDetail() {
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val productFragment = ProductdetailFragment()
                val product = adapter.getBook(position)
                val bundle = Bundle()
                bundle.putString("bookId", product.product_id.toString())
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, productFragment.apply { arguments = bundle })
                    .addToBackStack("AuthorFragment")
                    .commit()
                pastPage = currentPage
            }
        })
        adapterList.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val productFragment = ProductdetailFragment()
                val product = adapterList.getBook(position)
                val bundle = Bundle()
                bundle.putString("bookId", product.product_id.toString())
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, productFragment.apply { arguments = bundle })
                    .addToBackStack("AuthorFragment")
                    .commit()
                pastPage = currentPage
            }
        })
    }

//    private fun addItemToCart() {
//        adapter.setAddItemToCart(object : OnItemClickListener {
//            override fun onItemClick(position: Int) {
//                val product = adapter.getBook(position)
////                viewModel.addItemToCart(product.product_id)
////                Toast.makeText(context, "Add item to cart successful", Toast.LENGTH_SHORT).show()
//                val quantityRemaining = product.quantity - product.quantitySold
//                if (quantityRemaining > 0) {
//                    viewModel.addItemToCart(product.product_id)
////                    Handler().postDelayed({
////                        val supplierId = arguments?.getString("publisherId")?.toInt()
////                        supplierId?.let {
////                            viewModel.getProductsBySupplier(it, 10, currentPage, 100)
////                        }
////                    }, 500)
//                    AlertMessageViewer.showAlertDialogMessage(
//                        requireContext(),
//                        "Đã thêm sản phẩm vào giỏ hàng"
//                    )
//                } else {
//                    AlertMessageViewer.showAlertDialogMessage(
//                        requireContext(),
//                        "Sản phẩm này tạm hết!"
//                    )
//                }
//            }
//        })
//        adapterList.setAddItemToCart(object : OnItemClickListener {
//            override fun onItemClick(position: Int) {
//                val product = adapterList.getBook(position)
//                viewModel.addItemToCart(product.product_id)
//                Toast.makeText(context, "Add item to cart successful", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

    private fun handleSearch(supplierId: Int) {
        binding?.apply {
            searchProduct.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    // task HERE
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isEmpty()) {
//                        currentPage = 1
                        supplierId.let { supplierId ->
                            viewModel.getProductsBySupplier(supplierId, 10, 1, 100)
                        }
                        loadingProgressBar.show()
                    } else {
                        val delayMillis = 300L
                        searchHandler.removeCallbacksAndMessages(null)
                        searchHandler.postDelayed({
                            supplierId.let {
                                viewModel.getSearchSupplierProduct(it, 1, newText)
                            }
                        }, delayMillis)
                    }
                    return false;
                }
            })
        }
    }

    private fun handleLoadData(supplierId: Int) {
        binding?.apply {
            recyclerProduct.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    lastPosition =
                        if (isStateMenu) {
                            (recyclerProduct.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                        } else {
                            (recyclerProduct.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        }
                    totalPosition = adapter.itemCount
                    if (lastPosition != currentPosition && ((lastPosition == totalPosition - 3 && totalPosition % 2 == 0) || (lastPosition == totalPosition - 2 && totalPosition % 2 != 0))) {
                        currentPage++
                        supplierId.let { supplierId ->
                            viewModel.getProductsBySupplier(supplierId, 10, currentPage, 100)
                        }
                        currentPosition = lastPosition
                    }
                }
            })
        }
    }

}