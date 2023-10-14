package com.example.BookShopApp.data.repository.search

import com.example.BookShopApp.data.model.response.product.ProductList
import com.example.BookShopApp.data.model.response.product.ProductNewList
import retrofit2.Response

interface SearchRepository {
    suspend fun getSearchProducts(
        limit: Int,
        page: Int,
        descriptionLength: Int,
        queryString: String,
        filterType: Int,
        priceSortOrder: String,
    ): Response<ProductList>?

    suspend fun getSearchHistory(
        queryString: String,
    ): Response<ProductList>

    suspend fun getSearchAuthorProducts(
        authorId: Int,
        limit: Int,
        page: Int,
        descriptionLength: Int,
        queryString: String,
    ): Response<ProductList>?

    suspend fun getSearchCategoryProducts(
        limit: Int,
        page: Int,
        descriptionLength: Int,
        queryString: String,
        categroryId: Int,
    ): Response<ProductList>?

    suspend fun getSearchSupplierProducts(
        supplierId: Int,
        limit: Int,
        page: Int,
        descriptionLength: Int,
        queryString: String,
    ): Response<ProductList>?

    suspend fun getSearchNewProduct(): Response<ProductNewList>?
}