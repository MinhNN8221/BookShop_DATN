package com.example.BookShopApp.ui.adapter

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.BookShopApp.R
import com.example.BookShopApp.data.model.response.product.BookInHome
import com.example.BookShopApp.data.model.Product
import com.example.BookShopApp.databinding.ItemBookBinding
import com.example.BookShopApp.databinding.ItemNewArrivalBinding
import com.example.BookShopApp.utils.format.FormatMoney

class BookAdapter(private val isNewArrival: Boolean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var productList: MutableList<Product> = mutableListOf()
    private var productHomeList: MutableList<BookInHome> = mutableListOf()
    private var onItemClickListener: OnItemClickListener? = null
    private var addItemToCart: OnItemClickListener? = null
    private val formatMoney = FormatMoney()

    companion object {
        private const val VIEW_TYPE_NewArrival = 0
        private const val VIEW_TYPE_BOOK = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_BOOK) {
            val binding = ItemBookBinding.inflate(inflater, parent, false)
            BookViewHolder(binding)
        } else {
            val binding = ItemNewArrivalBinding.inflate(inflater, parent, false)
            NewArrivalViewHolder(binding)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData() {
        productList.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(products: List<Product>) {
        productList.clear()
        productList.addAll(products)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setBookInHome(products: List<BookInHome>) {
        productHomeList.clear()
        productHomeList.addAll(products)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    fun setAddItemToCart(listener: OnItemClickListener) {
        addItemToCart = listener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BookViewHolder -> {
                if (productHomeList.size > 0) {
                    holder.bindBookHome(productHomeList[position])
                } else {
                    holder.bind(productList[position])
                }
            }
            is NewArrivalViewHolder -> {
                val productInHome = productHomeList[position]
                holder.bindNewArrival(productInHome)
            }
        }
    }

    fun getBook(position: Int): Product = productList[position]

    fun getBookInHome(position: Int): BookInHome = productHomeList[position]

    override fun getItemCount(): Int {
        return if (productHomeList.size > 0) productHomeList.size else productList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isNewArrival) VIEW_TYPE_NewArrival else VIEW_TYPE_BOOK
    }

    inner class BookViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            Glide.with(binding.root)
                .load(product.thumbnail)
                .centerCrop()
                .into(binding.imageProduct)
            if (product.discounted_price != product.price) {
                val layoutParams =
                    binding.textPrice.layoutParams as ViewGroup.MarginLayoutParams
                val newMarginTopInDp = 0
                binding.textDiscountPrice.visibility = View.VISIBLE
                layoutParams.topMargin = newMarginTopInDp
                binding.textPrice.layoutParams = layoutParams
                binding.textDiscountPrice.text =
                    product.discounted_price?.toDouble()
                        ?.let { formatMoney.formatMoney(it.toLong()) }
                binding.textPrice.text = setPrice(
                    product.price?.toDouble()
                        ?.let { formatMoney.formatMoney(it.toLong()) }.toString()
                )
            } else {
                val layoutParams =
                    binding.textPrice.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.topMargin = dpToPx(binding.root, 10)
                binding.textPrice.layoutParams=layoutParams
                binding.textPrice.text = product.price?.toDouble()
                    ?.let { formatMoney.formatMoney(it.toLong()) }
                binding.textDiscountPrice.visibility=View.GONE
            }

            binding.textName.text = product.name
            binding.cardview.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(position)
                }
            }
            binding.textQuantitySold.text =
                binding.root.resources.getString(R.string.sold) + " " + product.quantitySold
//            binding.imageCart.setOnClickListener {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    addItemToCart?.onItemClick(position)
//                }
//            }
        }

        fun bindBookHome(product: BookInHome) {
            Glide.with(binding.root)
                .load(product.thumbnail)
                .centerCrop()
                .into(binding.imageProduct)
            if (product.discountedPrice != null && product.discountedPrice != product.price) {
                val layoutParams =
                    binding.textPrice.layoutParams as ViewGroup.MarginLayoutParams
                val newMarginTopInDp = 0
                binding.textDiscountPrice.visibility = View.VISIBLE
                layoutParams.topMargin = newMarginTopInDp
                binding.textPrice.layoutParams = layoutParams
                binding.textDiscountPrice.text =
                    product.discountedPrice?.toDouble()
                        ?.let { formatMoney.formatMoney(it.toLong()) }
                binding.textPrice.text = setPrice(
                    product.price?.toDouble()
                        ?.let { formatMoney.formatMoney(it.toLong()) }.toString()
                )
            } else {
                val layoutParams =
                    binding.textPrice.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.topMargin = dpToPx(binding.root, 10)
                binding.textPrice.layoutParams=layoutParams
                binding.textPrice.text = product.price?.toDouble()
                    ?.let { formatMoney.formatMoney(it.toLong()) }
                binding.textDiscountPrice.visibility=View.GONE
            }
            binding.cardview.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(position)
                }
            }
//            binding.imageCart.visibility = View.INVISIBLE
            binding.textQuantitySold.text =
                binding.root.resources.getString(R.string.sold) + " " + product.quantitySold
            binding.textName.text = product.name
        }
    }

    inner class NewArrivalViewHolder(private val binding: ItemNewArrivalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindNewArrival(product: BookInHome) {
            Glide.with(binding.root)
                .load(product.thumbnail)
                .centerCrop()
                .into(binding.imageProduct)
            if (product.discountedPrice != null && product.discountedPrice != product.price) {
                binding.textDiscountPrice.text =
                    product.discountedPrice?.toDouble()
                        ?.let { formatMoney.formatMoney(it.toLong()) }
                binding.textPrice.text = setPrice(
                    product.price?.toDouble()
                        ?.let { formatMoney.formatMoney(it.toLong()) }.toString()
                )
            } else {
                val layoutParams =
                    binding.textPrice.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.topMargin = dpToPx(binding.root, 10)
                binding.textPrice.layoutParams=layoutParams
                binding.textPrice.text = product.price?.toDouble()
                    ?.let { formatMoney.formatMoney(it.toLong()) }
                binding.textDiscountPrice.visibility=View.GONE
            }
            binding.textNameProduct.text = product.name
            binding.imageProduct.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(position)
                }
            }
//            binding.textQuantitySold.text =
//                binding.root.resources.getString(R.string.sold) + " " + product.quantitySold
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