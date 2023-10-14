package com.example.BookShopApp.ui.author

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.BookShopApp.R
import com.example.BookShopApp.data.model.Product
import com.example.BookShopApp.ui.adapter.BookAdapter
import com.example.BookShopApp.databinding.FragmentAuthorBinding
import com.example.BookShopApp.ui.adapter.OnItemClickListener
import com.example.BookShopApp.ui.productdetail.ProductdetailFragment
import com.example.BookShopApp.utils.ItemSpacingDecoration

class AuthorFragment : Fragment() {

    companion object {
        fun newInstance() = AuthorFragment()
    }

    private lateinit var viewModel: AuthorViewModel
    private var binding: FragmentAuthorBinding? = null
    private lateinit var adapter: BookAdapter
    private var bookList = mutableListOf<Product>()
    private var currentPage = 1
    private var lastPosition = 0
    private var totalPosition = 0
    private var currentPosition = 0
    private var pastPage = -1
    private val searchHandler = Handler(Looper.getMainLooper())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAuthorBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AuthorViewModel::class.java]
        adapter = BookAdapter(false)
        val authorId = arguments?.getString("authorId")?.toInt()
        authorId?.let {
            viewModel.getProductsByAuthor(authorId, 10, currentPage, 100)
            viewModel.getAuthor(authorId)
        }
        binding?.loadingLayout?.root?.visibility = View.VISIBLE
        initViewModel()
        navToProductDetail()
        addItemToCart()
        val horizontalSpacing =
            resources.getDimensionPixelSize(R.dimen.horizontal_spacing)
        val verticalSpacing =
            resources.getDimensionPixelSize(R.dimen.vertical_spacing)
        authorId?.let { handleSearch(it) }
        binding?.apply {
            recyclerAuthor.addItemDecoration(
                ItemSpacingDecoration(
                    horizontalSpacing,
                    verticalSpacing
                )
            )
            recyclerAuthor.layoutManager = GridLayoutManager(context, 2)
            recyclerAuthor.adapter = adapter
            imageLeft.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            layoutAuthor.setOnTouchListener { view, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    val event =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    event.hideSoftInputFromWindow(requireView().windowToken, 0)
                }
                false
            }
            swipeRefresh.setOnRefreshListener {
                Handler().postDelayed({
                    swipeRefresh.isRefreshing = false
                    authorId?.let {
                        viewModel.getAuthor(it)
                        viewModel.getProductsByAuthor(it, 10, 1, 100)
                    }
                }, 1000)
            }
            swipeRefresh.setColorSchemeColors(resources.getColor(R.color.teal_200))
        }
        authorId?.let { handleLoadData(it) }
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
                binding?.loadingLayout?.root?.visibility = View.INVISIBLE
            }
        }
        viewModel.author.observe(viewLifecycleOwner) {
            if (it != null) {
                if (!it.author.authorDescription.contains(it.author.authorName))
                    it.author.authorDescription =
                        it.author.authorName + " " + it.author.authorDescription
                binding?.textAuthor?.text =
                    setAuthorName(it.author.authorDescription, it.author.authorName)
            }
        }
    }

    private fun handleSearch(authorId: Int) {
        binding?.apply {
            searchProduct.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    // task HERE
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isEmpty()) {
                        textAuthor.visibility = View.VISIBLE
                        textHot.visibility = View.VISIBLE
//                        if(currentPage==1 && currentPage==pastPage){pastPage = -1}
                        authorId.let { authorId ->
                            viewModel.getProductsByAuthor(authorId, 10, 1, 100)
                        }
                        loadingLayout.root.visibility = View.VISIBLE
                    } else {
                        val layoutParams =
                            searchProduct.layoutParams as ViewGroup.MarginLayoutParams
                        val newMarginTopInDp = 12
                        val newMarginTopInPx = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, newMarginTopInDp.toFloat(),
                            resources.displayMetrics
                        ).toInt()
                        layoutParams.topMargin = newMarginTopInPx
                        searchProduct.layoutParams = layoutParams
                        textAuthor.visibility = View.GONE
                        textHot.visibility = View.GONE

                        val delayMillis = 300L
                        searchHandler.removeCallbacksAndMessages(null)
                        searchHandler.postDelayed({
                            authorId.let {
                                viewModel.getSearchAuthorProduct(it, 1, newText)
                            }
                        }, delayMillis)
                    }
                    return false;
                }
            })
        }
    }

    private fun handleLoadData(authorId: Int) {
        binding?.apply {
            recyclerAuthor.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    lastPosition =
                        (recyclerAuthor.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                    totalPosition = adapter.itemCount
                    if (lastPosition != currentPosition && ((lastPosition == totalPosition - 3 && totalPosition % 2 == 0) || (lastPosition == totalPosition - 2 && totalPosition % 2 != 0))) {
                        currentPage++
                        authorId.let {
                            viewModel.getProductsByAuthor(authorId, 10, currentPage, 100)
                        }
                        currentPosition = lastPosition
                    }
                }
            })
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
    }

    private fun addItemToCart() {
        adapter.setAddItemToCart(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val product = adapter.getBook(position)
                viewModel.addItemToCart(product.product_id)
                Toast.makeText(context, "Add item to cart successful", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun setAuthorName(authorDes: String, authorName: String): SpannableString {
        val content = SpannableString(authorDes)
        content.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            authorName.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        content.setSpan(
            RelativeSizeSpan(1.25f),
            0,
            authorName.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return content
    }
}