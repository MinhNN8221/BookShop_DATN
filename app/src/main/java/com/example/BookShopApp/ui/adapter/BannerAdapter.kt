package com.example.BookShopApp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.BookShopApp.data.model.response.product.Banner
import com.example.BookShopApp.databinding.ItemBannerBinding

class BannerAdapter() : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {
    private var bannerList: MutableList<Banner> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerAdapter.ViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerAdapter.ViewHolder, position: Int) {
        holder.bind(bannerList[position])
    }

    override fun getItemCount(): Int {
        return bannerList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(banner: List<Banner>){
        bannerList.clear()
        bannerList.addAll(banner)
        notifyDataSetChanged()
    }
    inner class ViewHolder(private val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(banner: Banner) {
            Glide.with(binding.root)
                .load(banner.bannerUrl)
                .centerCrop().into(binding.imageBanner)
        }
    }
}