package com.example.BookShopApp.ui.productdetail.ratingbook

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.BookShopApp.data.model.Rating
import com.example.BookShopApp.databinding.FragmentRatingBinding
import com.example.BookShopApp.ui.adapter.RatingAdapter

class RatingFragment : Fragment() {

    companion object {
        fun newInstance() = RatingFragment()
    }

    private lateinit var viewModel: RatingViewModel
    private var binding: FragmentRatingBinding? = null
    private lateinit var adapter: RatingAdapter
    private var ratingList = mutableListOf<Rating>()
    private var currentPage = 1
    private var lastPosition = 0
    private var totalPosition = 0
    private var currentPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RatingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentRatingBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewHolder()
        adapter = RatingAdapter(true)
        val bookId = arguments?.getString("bookId")?.toInt()
        bookId?.let { viewModel.getAllRatingByBook(it, 10, 1) }
        binding?.apply {
            recyclerRatingList.layoutManager = LinearLayoutManager(context)
            recyclerRatingList.adapter = adapter

            imageLeft.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
        if (bookId != null) {
            handleLoadData(bookId)
        }
    }

    private fun initViewHolder() {
        viewModel.ratingList.observe(viewLifecycleOwner) {
            ratingList.addAll(it)
            if (ratingList.isEmpty()) {
                binding?.textRatingInfo?.visibility = View.VISIBLE
            } else {
                binding?.textRatingInfo?.visibility = View.GONE
            }
            adapter.setDataOrderBook(ratingList)

        }
    }

    private fun handleLoadData(bookId: Int) {
        binding?.apply {
            recyclerRatingList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    lastPosition =
                        (recyclerRatingList.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    totalPosition = adapter.itemCount
                    if (lastPosition != currentPosition && (lastPosition == totalPosition - 3 && totalPosition % 2 == 0)) {
                        currentPage++
                        bookId.let { bookId ->
                            viewModel.getAllRatingByBook(bookId, 10, currentPage)
                        }
                        currentPosition = lastPosition
                    }
                }
            })
        }
    }
}