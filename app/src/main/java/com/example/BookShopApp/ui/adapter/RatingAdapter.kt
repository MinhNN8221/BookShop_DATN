package com.example.BookShopApp.ui.adapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.BookShopApp.R
import com.example.BookShopApp.data.model.Rating
import com.example.BookShopApp.data.model.response.order.OrderDetailProduct
import com.example.BookShopApp.databinding.ItemRatingBinding
import com.example.BookShopApp.databinding.ItemRatingOrderBinding

@Suppress("UNUSED_EXPRESSION")
class RatingAdapter(private val isRatingBook: Boolean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var ratingList: MutableList<Rating> = mutableListOf()
    private var mapComment: MutableMap<Int, String> = mutableMapOf()
    private var mapRating: MutableMap<Int, Int> = mutableMapOf()
    private var orderDetailProductList: MutableList<OrderDetailProduct> = mutableListOf()

    companion object {
        private const val VIEW_RATING_BOOK = 0
        private const val VIEW_RATING_ORDER = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_RATING_BOOK) {
            val binding =
                ItemRatingBinding.inflate(inflater, parent, false)
            RatingBookViewHolder(binding)
        } else {
            val binding = ItemRatingOrderBinding.inflate(inflater, parent, false)
            RatingOrderViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RatingBookViewHolder -> {
                holder.bindRatingBook(ratingList[position])
            }
            is RatingOrderViewHolder -> {
                holder.bindRatingOrder(orderDetailProductList[position])
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataOrderBook(rating: List<Rating>) {
        ratingList.clear()
        ratingList.addAll(rating)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataRatingOrder(orderDetail: List<OrderDetailProduct>) {
        orderDetailProductList.clear()
        orderDetailProductList.addAll(orderDetail)
        notifyDataSetChanged()
    }

    fun getRatingOrder(position: Int) = orderDetailProductList[position]

    fun getComment(position: Int): String? {
        return mapComment[position]
    }

    fun getRating(position: Int) = mapRating[position]

    override fun getItemCount(): Int {
        return if (ratingList.size > 0) ratingList.size else orderDetailProductList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isRatingBook) VIEW_RATING_BOOK else VIEW_RATING_ORDER
    }

    inner class RatingBookViewHolder(private val binding: ItemRatingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindRatingBook(rating: Rating) {
            binding.apply {
                if (rating.avatarUser == null) {
                    imageAvatar.setImageResource(R.drawable.account_profile)
                } else {
                    Glide.with(root)
                        .load(rating.avatarUser)
                        .centerCrop().into(imageAvatar)
                }
                textName.text = rating.nameUser
                textTime.text = rating.createTime
                textComment.text = rating.comment
                ratingbar.rating = rating.ratingLevel.toFloat()
            }
        }
    }

    inner class RatingOrderViewHolder(private val binding: ItemRatingOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindRatingOrder(orderDetailProduct: OrderDetailProduct) {
            binding.apply {
                textIdBook.text = orderDetailProduct.productId.toString()
                Glide.with(root)
                    .load(orderDetailProduct.image)
                    .centerCrop().into(imageProduct)
                textNameBook.text = orderDetailProduct.productName
                textDescription.text = orderDetailProduct.productDescription
                editComment.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int,
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {

                    }

                    override fun afterTextChanged(s: Editable?) {
                        mapComment[adapterPosition] = s.toString().trim()
                    }
                })
                mapRating[adapterPosition] = ratingbar.rating.toInt()
                ratingbar.setOnRatingBarChangeListener { _, rating, _ ->
                    mapRating[adapterPosition] = rating.toInt()

                    satisfactionLevel.text = when (rating.toInt()) {
                        1 -> "Tệ"
                        2 -> "Không hài lòng"
                        3 -> "Bình thường"
                        4 -> "Hài lòng"
                        else -> {"Tuyệt vời"}
                    }
                }
            }
        }
    }
}