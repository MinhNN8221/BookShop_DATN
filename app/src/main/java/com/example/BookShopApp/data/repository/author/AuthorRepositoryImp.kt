package com.example.BookShopApp.data.repository.author

import com.example.BookShopApp.data.model.response.author.AuthorFamousList
import com.example.BookShopApp.data.model.response.author.AuthorInfor
import com.example.BookShopApp.datasource.IDataSource
import retrofit2.Response

class AuthorRepositoryImp(private val iDataSource: IDataSource) : AuthorRepository {
    override suspend fun getHotAuthors(): Response<AuthorFamousList>? {
        return iDataSource.getHotAuthor()
    }

    override suspend fun getAuthor(authorId: Int): Response<AuthorInfor>? {
        return iDataSource.getAuthor(authorId)
    }
}