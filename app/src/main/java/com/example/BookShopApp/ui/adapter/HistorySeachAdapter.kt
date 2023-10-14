package com.example.BookShopApp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.BookShopApp.data.model.response.HistorySearch
import com.example.BookShopApp.data.model.Product
import com.example.BookShopApp.databinding.ItemHistorySearchBinding
import kotlin.math.min

class HistorySeachAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var historyList: MutableList<HistorySearch> = mutableListOf()
    private var onItemClickListener: OnItemClickListener? = null
    private var clickRemoveItem: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistorySearchBinding.inflate(inflater, parent, false)
        return if (viewType == 0) {
            HistoryLocalViewHolder(binding)
        } else {
            HistorySuggestViewHolder(binding)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeData(position: Int) {
        historyList.removeAt(position)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(historysearch: List<HistorySearch>) {
        historyList.clear()
        historyList.addAll(historysearch)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    fun clickRemoveItem(listener: OnItemClickListener) {
        clickRemoveItem = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData() {
        historyList.clear()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (historyList[position].historyLocal != null) 0 else 1
    }

    override fun getItemCount(): Int {
        return min(7, historyList.size)
    }

    fun getBook(possition: Int): Product? {
        return historyList[possition].historySuggest
    }

    fun getProductNameLocal(position: Int): String? {
        return historyList[position].historyLocal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val historyLocal = historyList[position].historyLocal
        val historySearch = historyList[position].historySuggest
        when (holder) {
            is HistoryLocalViewHolder -> historyLocal?.let { holder.bind(it) }
            is HistorySuggestViewHolder -> historySearch?.let { holder.bind(it) }
        }
    }

    inner class HistoryLocalViewHolder(private val binding: ItemHistorySearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productName: String) {
            binding.textItemHistroy.text = productName
            val position = adapterPosition
            binding.textItemHistroy.setOnClickListener {
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(position)
                }
            }
            binding.imageRemove.setOnClickListener {
                if (position != RecyclerView.NO_POSITION) {
                    clickRemoveItem?.onItemClick(position)
                }
            }
        }
    }

    inner class HistorySuggestViewHolder(private val binding: ItemHistorySearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.textItemHistroy.text = product.name
            binding.textItemHistroy.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(position)
                }
            }
        }
    }
}