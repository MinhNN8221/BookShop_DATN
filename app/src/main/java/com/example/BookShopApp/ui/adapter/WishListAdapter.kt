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
import com.example.BookShopApp.data.model.Wishlist
import com.example.BookShopApp.databinding.ItemWishlistBinding
import com.example.BookShopApp.utils.format.FormatMoney

class WishListAdapter() : RecyclerView.Adapter<WishListAdapter.ViewHolder>() {
    private var wishLists: MutableList<Wishlist> = mutableListOf()
    private val formatMoney = FormatMoney()
    private var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWishlistBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WishListAdapter.ViewHolder, position: Int) {
        val wishList = wishLists[position]
        holder.bind(wishList)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(products: List<Wishlist>) {
        wishLists.clear()
        wishLists.addAll(products)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeData(position: Int) {
        wishLists.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return wishLists.size
    }

    fun getBook(position: Int): Wishlist {
        return wishLists[position]
    }

    fun getTotalPrice(): Double {
        var totalPrice = 0.0
        for (wishList in wishLists) {
            wishList.price?.toDouble()?.let { price ->
                totalPrice += price
            }
        }
        return totalPrice
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    inner class ViewHolder(private val binding: ItemWishlistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wishList: Wishlist) {
            Glide.with(binding.root)
                .load(wishList.thumbnail)
                .centerCrop()
                .into(binding.imageProduct)
            if (wishList.discount != null) {
                val layoutParamsName =
                    binding.textName.layoutParams as ViewGroup.MarginLayoutParams
                val layoutParamsNXB =
                    binding.textNXB.layoutParams as ViewGroup.MarginLayoutParams
                val layoutParamQuantity =
                    binding.textQuantityRemaining.layoutParams as ViewGroup.MarginLayoutParams
                val newMarginTopInDp = 12
                val newMarginBottomDp = 12
                binding.textDiscountPrice.visibility = View.VISIBLE
                layoutParamsName.topMargin = dpToPx(binding.root, newMarginTopInDp)
//                layoutParamsNXB.topMargin = dpToPx(binding.root, newMarginTopInDp)
//                layoutParamsNXB.bottomMargin = dpToPx(binding.root, newMarginBottomDp)
                layoutParamQuantity.bottomMargin = dpToPx(binding.root, newMarginBottomDp)
                binding.textName.layoutParams = layoutParamsName
                binding.textNXB.layoutParams = layoutParamsNXB
                binding.textQuantityRemaining.layoutParams = layoutParamQuantity
                binding.textDiscountPrice.text =
                    wishList.discount.toDouble()
                        .let { formatMoney.formatMoney(it.toLong()) }
                binding.textPrice.text = setPrice(
                    wishList.price?.toDouble()
                        ?.let { formatMoney.formatMoney(it.toLong()) }.toString()
                )
            } else {
                binding.textPrice.text = wishList.price?.toDouble()
                    ?.let { formatMoney.formatMoney(it.toLong()) }
            }
            binding.textName.text = wishList.name
            binding.textNXB.text = wishList.supplierName
            binding.textQuantityRemaining.text =
                (wishList.quantity - wishList.quantitySold).toString()
            binding.imageAdd.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(position)
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