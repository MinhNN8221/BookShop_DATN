package com.example.BookShopApp.data.repository.category

import com.example.BookShopApp.data.model.response.CategoryList
import retrofit2.Response

interface CategoryRepository {
    suspend fun getAllCategory(): Response<CategoryList>?
    suspend fun getHotCategory():Response<CategoryList>?
}