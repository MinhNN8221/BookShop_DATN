package com.example.BookShopApp.data.repository.product

import com.example.BookShopApp.data.model.request.RatingRequest
import com.example.BookShopApp.data.model.response.Message
import com.example.BookShopApp.data.model.response.RatingResponse
import com.example.BookShopApp.data.model.response.product.*
import com.example.BookShopApp.datasource.IDataSource
import retrofit2.Response

class ProductRepositoryImp(private val dataSource: IDataSource) : ProductRepository {
    override suspend fun createRatingOrder(ratingRequest: List<RatingRequest>): Response<Message> {
        return dataSource.createRatingOrder(ratingRequest)
    }
    override suspend fun getAllRatingByBook(
        bookId: Int,
        limit: Int,
        page: Int
    ): Response<RatingResponse> {
        return dataSource.getAllRatingByBook(bookId, limit, page)
    }

    override suspend fun getProductsBanner(): Response<BannerList>? {
        return dataSource.getProductsBanner()
    }

    override suspend fun getProductInfo(id: Int): Response<ProductInfoList>? {
        return dataSource.getProductInfo(id)
    }

    override suspend fun getProductsByAuthor(
        author_id: Int,
        limit: Int,
        page: Int,
        description_length: Int,
    ): Response<ProductsByAuthor>? {
        return dataSource.getProductsByAuthor(author_id, limit, page, description_length)
    }

    override suspend fun getProductsByCategory(
        author_id: Int,
        limit: Int,
        page: Int,
        description_length: Int,
    ): Response<ProductList>? {
        return dataSource.getProductsByCategory(author_id, limit, page, description_length)
    }

    override suspend fun getProductsBySupplier(
        id: Int,
        limit: Int,
        page: Int,
        description_length: Int,
    ): Response<ProductList>? {
        return dataSource.getProductsBySupplier(id, limit, page, description_length)
    }

    override suspend fun getNewBook(): Response<BookInHomeList>? {
        return dataSource.getNewBook()
    }

    override suspend fun getHotBook(): Response<BookInHomeList>? {
        return dataSource.getHotBook()
    }
}