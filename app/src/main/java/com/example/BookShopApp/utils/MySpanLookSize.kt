package com.example.BookShopApp.utils

import androidx.recyclerview.widget.GridLayoutManager
import com.example.BookShopApp.ui.adapter.CategoryIndexAdapter

class MySpanLookSize(var adapter: CategoryIndexAdapter, val spanCount1: Int, val spanCount2: Int) :
    GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
        val size = adapter.getCategory(position).name?.length
        if (size != null && size > 25) {
            return spanCount2
        }
        return spanCount1
    }
}