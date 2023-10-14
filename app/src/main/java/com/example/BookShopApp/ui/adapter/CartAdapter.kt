package com.example.BookShopApp.ui.adapter

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.BookShopApp.R
import com.example.BookShopApp.data.model.CartItem
import com.example.BookShopApp.databinding.ItemCartBinding
import com.example.BookShopApp.utils.format.FormatMoney

class CartAdapter() : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private var formatMoney = FormatMoney()
    private var cartItemList: MutableList<CartItem> = mutableListOf()
    private var onIncreClickListener: OnItemClickListener? = null
    private var onDecreClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCartBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        val cartItem = cartItemList[position]
        holder.bind(cartItem)
    }

    override fun getItemCount(): Int {
        return cartItemList.size
    }

    fun getItemCart(position: Int): CartItem {
        return cartItemList[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setProductQuantity(quantity: Int, position: Int) {
        cartItemList[position].quantity = quantity
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSubTotalPrice(subTotalPrice: Double, position: Int) {
        cartItemList[position].subTotal = subTotalPrice.toString()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeData(position: Int) {
        cartItemList.removeAt(position)
        notifyDataSetChanged()
    }

    fun getTotalPrice(): Double {
        var totalPrice = 0.0
        for (cartItem in cartItemList) {
            cartItem.subTotal?.toDouble()?.let { price ->
                totalPrice += price
            }
        }
        return totalPrice
    }

    fun setOnIncreClickListener(listener: OnItemClickListener) {
        onIncreClickListener = listener
    }

    fun setOnDecreClickListener(listener: OnItemClickListener) {
        onDecreClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(cartItem: List<CartItem>) {
        cartItemList.clear()
        cartItemList.addAll(cartItem)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            Glide.with(binding.root)
                .load(cartItem.image)
                .centerCrop()
                .into(binding.imageProduct)
            binding.textName.text = cartItem.name
            binding.textQuantity.text = cartItem.quantity.toString()
            if (cartItem.discountedPrice != null && cartItem.discountedPrice != cartItem.price) {
                val layoutParams =
                    binding.imageFavorite.layoutParams as ViewGroup.MarginLayoutParams
                val newMarginBottomInDp = 13
                binding.textDiscountPrice.visibility = View.VISIBLE
                layoutParams.bottomMargin = dpToPx(binding.root, newMarginBottomInDp)
                binding.imageFavorite.layoutParams = layoutParams
                binding.textDiscountPrice.text =
                    cartItem.discountedPrice?.toDouble()
                        ?.let { formatMoney.formatMoney(it.toLong()) }
                binding.textPrice.text = setPrice(
                    cartItem.price?.toDouble()
                        ?.let { formatMoney.formatMoney(it.toLong()) }.toString()
                )
            } else {
                binding.textPrice.text = cartItem.price?.toDouble()
                    ?.let { formatMoney.formatMoney(it.toLong()) }
            }
            if (cartItem.wishlist == 1) {
                binding.imageFavorite.setBackgroundResource(R.drawable.bg_ellipse_favor)
                binding.imageFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                binding.imageFavorite.setBackgroundResource(R.drawable.bg_ellipse)
                binding.imageFavorite.setImageResource(R.drawable.ic_favor_white)
            }
            binding.textIncreProduct.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onIncreClickListener?.onItemClick(position)
                }
            }
            binding.textDecreProduct.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDecreClickListener?.onItemClick(position)
                }
            }
        }
    }

    private fun setPrice(price: String): SpannableString {
        val content = SpannableString(price)
        content.setSpan(
            StrikethroughSpan(),
            0,
            price.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        content.setSpan(
            RelativeSizeSpan(12 / 14f),
            0,
            price.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return content
    }

    private fun dpToPx(view: View, dp: Int): Int {
        val scale = view.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}