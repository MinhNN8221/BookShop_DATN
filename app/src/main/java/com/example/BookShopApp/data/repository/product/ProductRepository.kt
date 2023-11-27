package com.example.BookShopApp.data.repository.product

import com.example.BookShopApp.data.model.request.RatingRequest
import com.example.BookShopApp.data.model.response.Message
import com.example.BookShopApp.data.model.response.RatingResponse
import com.example.BookShopApp.data.model.response.product.*
import retrofit2.Response

interface ProductRepository {
    suspend fun createRatingOrder(ratingRequest:List<RatingRequest>):Response<Message>
    suspend fun getAllRatingByBook(bookId:Int, limit:Int, page:Int):Response<RatingResponse>
    suspend fun getProductsBanner(): Response<BannerList>?
    suspend fun getProductInfo(id: Int): Response<ProductInfoList>?
    suspend fun getProductsByAuthor(
        author_id: Int,
        limit: Int,
        page: Int,
        description_length: Int,
    ): Response<ProductsByAuthor>?

    suspend fun getProductsByCategory(
        author_id: Int,
        limit: Int,
        page: Int,
        description_length: Int,
    ): Response<ProductList>?

    suspend fun getProductsBySupplier(
        id: Int,
        limit: Int,
        page: Int,
        description_length: Int,
    ): Response<ProductList>?

    suspend fun getNewBook(): Response<BookInHomeList>?
    suspend fun getHotBook(): Response<BookInHomeList>?
}