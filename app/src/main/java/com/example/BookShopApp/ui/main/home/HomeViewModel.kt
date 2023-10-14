package com.example.BookShopApp.ui.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BookShopApp.data.model.*
import com.example.BookShopApp.data.model.response.product.Banner
import com.example.BookShopApp.data.model.response.product.BookInHome
import com.example.BookShopApp.data.repository.author.AuthorRepository
import com.example.BookShopApp.data.repository.author.AuthorRepositoryImp
import com.example.BookShopApp.data.repository.category.CategoryRepository
import com.example.BookShopApp.data.repository.category.CategoryRepositoryImp
import com.example.BookShopApp.data.repository.product.ProductRepository
import com.example.BookShopApp.data.repository.product.ProductRepositoryImp
import com.example.BookShopApp.datasource.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private var _categoryHotList = MutableLiveData<List<Category>>()
    val categoryHotList: LiveData<List<Category>> get() = _categoryHotList
    private var _authorFamousList = MutableLiveData<List<Author>>()
    val authorFamousList: LiveData<List<Author>> get() = _authorFamousList
    private var _bookNewList = MutableLiveData<List<BookInHome>>()
    val bookNewList: LiveData<List<BookInHome>> get() = _bookNewList
    private var _bookHotList = MutableLiveData<List<BookInHome>>()
    val bookHotList: LiveData<List<BookInHome>> get() = _bookHotList
    private var _bannerList = MutableLiveData<List<Banner>>()
    val bannerList: LiveData<List<Banner>> get() = _bannerList
    private var productRepository: ProductRepository = ProductRepositoryImp(RemoteDataSource())
    private var categotyRepository: CategoryRepository? = CategoryRepositoryImp(RemoteDataSource())
    private var authorRepository: AuthorRepository = AuthorRepositoryImp(RemoteDataSource())

    fun getProductsBanner() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = productRepository.getProductsBanner()
            if (response?.isSuccessful == true) {
                _bannerList.postValue(response.body()?.products)
            } else {
                Log.d("GetBanner", "NULL")
            }
        }
    }

    fun getHotCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = categotyRepository?.getHotCategory()
            if (response?.isSuccessful == true) {
                _categoryHotList.postValue(response.body()?.categories)
            } else {
                Log.d("CategoryHot", "NULL")
            }
        }
    }

    fun getFamousAuthor() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = authorRepository.getHotAuthors()
            if (response?.isSuccessful == true) {
                _authorFamousList.postValue(response.body()?.authors)
            } else {
                Log.d("AuthorHot", "NULL")
            }
        }
    }

    fun getNewBook() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = productRepository.getNewBook()
            if (response?.isSuccessful == true) {
                _bookNewList.postValue(response.body()?.products)
            } else {
                Log.d("getNewBook", "NULL")
            }
        }
    }

    fun getHotBook() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = productRepository.getHotBook()
            if (response?.isSuccessful == true) {
                _bookHotList.postValue(response.body()?.products)
            } else {
                Log.d("getHotBook", "NULL")
            }
        }
    }
}