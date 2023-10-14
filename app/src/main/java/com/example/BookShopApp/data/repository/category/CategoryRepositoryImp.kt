package com.example.BookShopApp.data.repository.category

import com.example.BookShopApp.data.model.response.CategoryList
import com.example.BookShopApp.datasource.IDataSource
import retrofit2.Response

class CategoryRepositoryImp(private val dataSource: IDataSource) : CategoryRepository {
    override suspend fun getAllCategory(): Response<CategoryList>? {
        return dataSource.getAllCategory()
    }

    override suspend fun getHotCategory(): Response<CategoryList>? {
        return dataSource.getHotCategory()
    }
}