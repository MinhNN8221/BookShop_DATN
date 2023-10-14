package com.example.BookShopApp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.BookShopApp.data.model.Author
import com.example.BookShopApp.databinding.ItemFamousAuthorsBinding

class AuthorFamousAdapter() : RecyclerView.Adapter<AuthorFamousAdapter.ViewHolder>() {
    private var authorFamousList: MutableList<Author> = mutableListOf()
    private var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AuthorFamousAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFamousAuthorsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AuthorFamousAdapter.ViewHolder, position: Int) {
        holder.bind(authorFamousList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(author: List<Author>) {
        authorFamousList.clear()
        authorFamousList.addAll(author)
        notifyDataSetChanged()
    }

    fun getAuthor(position: Int): Author = authorFamousList[position]

    override fun getItemCount(): Int {
        return authorFamousList.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    inner class ViewHolder(private val binding: ItemFamousAuthorsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(author: Author) {
            Glide.with(binding.root).load(author.authorAvatar)
                .transform(CircleCrop())
                .into(binding.imageAuthor)
            binding.textNameAuthor.text = author.authorName
            binding.cardview.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(position)
                }
            }
        }
    }
}