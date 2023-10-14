package com.example.BookShopApp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.BookShopApp.data.model.Category
import com.example.BookShopApp.databinding.ItemCategoryBinding
import com.example.BookShopApp.databinding.ItemCategoryHotBinding

class CategoryIndexAdapter(private val isStateCategoryHot: Boolean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var onItemClickListener: OnItemClickListener
    private var categoryList: MutableList<Category> = mutableListOf()

    companion object {
        private const val VIEW_TYPE_HOT = 0
        private const val VIEW_TYPE_ALL = 1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_ALL) {
            val binding = ItemCategoryBinding.inflate(inflater, parent, false)
            ItemAllViewHolder(binding)
        } else {
            val binding = ItemCategoryHotBinding.inflate(inflater, parent, false)
            ItemHotViewHolder(binding)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataCategory(categories: List<Category>) {
        categoryList.clear()
        categoryList.addAll(categories)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemAllViewHolder -> holder.bind(categoryList[position])
            is ItemHotViewHolder -> holder.bind(categoryList[position])
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    fun getCategory(position: Int): Category {
        return categoryList[position]
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isStateCategoryHot) VIEW_TYPE_HOT else VIEW_TYPE_ALL
    }

    inner class ItemAllViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.nameCategory.text = category.name
            binding.cardviewCategory.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position)
                }
            }
        }
    }
    inner class ItemHotViewHolder(private val binding: ItemCategoryHotBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.textCategoryHot.text = category.name
            binding.cardviewCategory.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position)
                }
            }
        }
    }
}