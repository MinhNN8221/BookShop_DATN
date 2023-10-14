package com.example.BookShopApp.ui.main.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.BookShopApp.R
import com.example.BookShopApp.data.model.response.product.Banner
import com.example.BookShopApp.databinding.FragmentHomeBinding
import com.example.BookShopApp.databinding.FragmentMainMenuBinding
import com.example.BookShopApp.ui.adapter.*
import com.example.BookShopApp.ui.author.AuthorFragment
import com.example.BookShopApp.ui.category.CategoryBookFragment
import com.example.BookShopApp.ui.category.categoryindex.CategoryIndexFragment
import com.example.BookShopApp.ui.productdetail.ProductdetailFragment
import com.example.BookShopApp.ui.profile.ProfileFragment
import com.example.BookShopApp.utils.ItemSpacingDecoration
import com.example.BookShopApp.utils.LoadingProgressBar


class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private lateinit var bindingMenu: FragmentMainMenuBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapterHotCategory: CategoryIndexAdapter
    private lateinit var adapterNewBook: BookAdapter
    private lateinit var adapterHotBook: BookAdapter
    private lateinit var adapterFamousAuthor: AuthorFamousAdapter
    private lateinit var adapterBanner: BannerAdapter
    private lateinit var loadingProgressBar: LoadingProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        bindingMenu = FragmentMainMenuBinding.inflate(layoutInflater, container, false)
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        adapterHotCategory = CategoryIndexAdapter(true)
        adapterNewBook = BookAdapter(true)
        adapterHotBook = BookAdapter(false)
        adapterFamousAuthor = AuthorFamousAdapter()
        adapterBanner = BannerAdapter()
        loadingProgressBar = LoadingProgressBar(requireContext())
        loadingProgressBar.show()
        initViewModel()
        viewModel.getHotCategory()
        viewModel.getNewBook()
        viewModel.getHotBook()
        viewModel.getFamousAuthor()
        viewModel.getProductsBanner()
        navToProductDetail()
        navToAuthorDetail()
        navToCategory()
        val horizontalSpacing = resources.getDimensionPixelSize(R.dimen.horizontal_spacing)
        val verticalSpacing = resources.getDimensionPixelSize(R.dimen.vertical_spacing)
        binding?.apply {
            imageProfile.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, ProfileFragment())
                    .addToBackStack("HomeFragment")
                    .commit()
            }
            imageNavCategory.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, CategoryIndexFragment())
                    .addToBackStack("HomeFragment")
                    .commit()
            }
            recyclerviewHotCategory.adapter = adapterHotCategory
            recyclerviewHotCategory.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            recyclerviewNewarrival.adapter = adapterNewBook
            recyclerviewNewarrival.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            recyclerviewHotbook.adapter = adapterHotBook
            recyclerviewHotbook.layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerviewHotbook.addItemDecoration(
                ItemSpacingDecoration(
                    horizontalSpacing,
                    verticalSpacing
                )
            )
            recyclerviewFamousAuthor.adapter = adapterFamousAuthor
            recyclerviewFamousAuthor.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initViewModel() {
        viewModel.categoryHotList.observe(viewLifecycleOwner) {
            it?.let {
                adapterHotCategory.setDataCategory(it)
            }
            loadingProgressBar.cancel()
        }
        viewModel.authorFamousList.observe(viewLifecycleOwner) {
            it?.let {
                adapterFamousAuthor.setData(it)
            }
        }
        viewModel.bookNewList.observe(viewLifecycleOwner) {
            it?.let {
                adapterNewBook.setBookInHome(it)
            }
        }
        viewModel.bookHotList.observe(viewLifecycleOwner) {
            it?.let {
                adapterHotBook.setBookInHome(it)
            }
        }
        viewModel.bannerList.observe(viewLifecycleOwner) {
            it?.let {
                adapterBanner.setData(it)
                autoImageSlider(it)
            }
        }
    }

    private fun autoImageSlider(banners: List<Banner>) {
        binding?.apply {
            val handler = Handler()
            val runnable = Runnable {
                if (viewpagerBanner.currentItem == banners.size - 1) {
                    viewpagerBanner.currentItem = 0
                } else {
                    viewpagerBanner.currentItem = viewpagerBanner.currentItem + 1
                }
            }
            viewpagerBanner.adapter = adapterBanner
            circleindicator.setViewPager(viewpagerBanner)
            viewpagerBanner.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    handler.removeCallbacks(runnable)
                    handler.postDelayed(runnable, 3000)
                }
            })
        }
    }

    private fun navToCategory() {
        adapterHotCategory.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val category = adapterHotCategory.getCategory(position)
                val bundle = Bundle()
                bundle.putString("categoryId", category.categoryId.toString())
                bundle.putString("categoryName", category.name)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, CategoryBookFragment().apply { arguments = bundle })
                    .addToBackStack("HomeFragment")
                    .commit()
            }
        })
    }

    fun navToAuthorDetail() {
        adapterFamousAuthor.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val author = adapterFamousAuthor.getAuthor(position)
                val bundle = Bundle()
                bundle.putString("authorId", author.authorId.toString())
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, AuthorFragment().apply { arguments = bundle })
                    .addToBackStack("HomeFragment")
                    .commit()
            }
        })
    }

    fun navToProductDetail() {
        adapterNewBook.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val prouduct = adapterNewBook.getBookInHome(position)
                val bundle = Bundle()
                bundle.putString("bookId", prouduct.productId.toString())
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, ProductdetailFragment().apply { arguments = bundle })
                    .addToBackStack("HomeFragment")
                    .commit()
            }
        })
        adapterHotBook.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val prouduct = adapterHotBook.getBookInHome(position)
                val bundle = Bundle()
                bundle.putString("bookId", prouduct.productId.toString())
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, ProductdetailFragment().apply { arguments = bundle })
                    .addToBackStack("HomeFragment")
                    .commit()
            }
        })
    }
}