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
import com.example.BookShopApp.data.model.Product
import com.example.BookShopApp.databinding.ItemBookListBinding
import com.example.BookShopApp.utils.format.FormatMoney

class BookListAdapter : RecyclerView.Adapter<BookListAdapter.ViewHolder>() {
    private var productList: MutableList<Product> = mutableListOf()
    private var cartItemList: MutableList<CartItem> = mutableListOf()
    private var onItemClickListener: OnItemClickListener? = null
    private var addItemToCart: OnItemClickListener? = null
    private val formatMoney = FormatMoney()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBookListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookListAdapter.ViewHolder, position: Int) {
        if (productList.size > 0) {
            val product = productList[position]
            holder.bindProduct(product)
        } else {
            val cartItem = cartItemList[position]
            holder.bindCartItem(cartItem)
        }
    }

    override fun getItemCount(): Int {
        if (productList.size > 0)
            return productList.size
        return cartItemList.size
    }

    fun getTotalPrice(): Double {
        var totalPrice = 0.0
        for (cartItem in cartItemList) {
            cartItem.price?.toDouble()?.let { price ->
                totalPrice += price * cartItem.quantity
            }
        }
        return totalPrice
    }

    fun getTotalPromotionPrice(): Double {
        var totalDicountPrice = 0.0
        for (cartItem in cartItemList) {
            cartItem.subTotal.toDouble()?.let { subTotal ->
                totalDicountPrice += subTotal
            }
        }
        return totalDicountPrice - getTotalPrice()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataCart(cartItem: List<CartItem>) {
        cartItemList.clear()
        cartItemList.addAll(cartItem)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataProduct(product: List<Product>) {
        productList.clear()
        productList.addAll(product)
        notifyDataSetChanged()
    }

    fun getBook(position: Int): Product = productList[position]
    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }
    fun setAddItemToCart(listener: OnItemClickListener) {
        addItemToCart = listener
    }
    inner class ViewHolder(private val binding: ItemBookListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindProduct(product: Product) {
            Glide.with(binding.root)
                .load(product.thumbnail)
                .centerCrop()
                .into(binding.imageProduct)
            binding.textNameBook.text = product.name
            binding.textDescription.text = product.description
            if (product.discounted_price != null && product.discounted_price != product.price){
                binding.textDiscountPrice.visibility = View.VISIBLE
                binding.textDiscountPrice.text =
                    product.discounted_price?.toDouble()
                        ?.let { formatMoney.formatMoney(it.toLong()) }
                binding.textPriceBook.text = setPrice(
                    product.price?.toDouble()
                        ?.let { formatMoney.formatMoney(it.toLong()) }.toString()
                )
            } else {
                binding.textPriceBook.text = product.price?.toDouble()
                    ?.let { formatMoney.formatMoney(it.toLong()) }
            }
            binding.cardview.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(position)
                }
            }
//            binding.imageCart.setOnClickListener {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    addItemToCart?.onItemClick(position)
//                }
//            }
            binding.textQuantitySold.text =
                binding.root.resources.getString(R.string.sold) + " " + product.quantitySold
            binding.textQuantity.visibility = View.INVISIBLE
        }

        @SuppressLint("SetTextI18n")
        fun bindCartItem(cartItem: CartItem) {
            Glide.with(binding.root)
                .load(cartItem.image)
                .centerCrop()
                .into(binding.imageProduct)
            binding.textNameBook.text = cartItem.name
            binding.textQuantity.text = "x" + cartItem.quantity.toString()
            binding.textPriceBook.text =
                cartItem.price?.toDouble()
                    ?.let { formatMoney.formatMoney(it.toLong()) }
            binding.textQuantitySold.visibility = View.INVISIBLE
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
}