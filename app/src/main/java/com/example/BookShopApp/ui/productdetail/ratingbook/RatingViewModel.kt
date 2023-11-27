package com.example.BookShopApp.ui.productdetail.ratingbook

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BookShopApp.data.model.Rating
import com.example.BookShopApp.data.repository.product.ProductRepository
import com.example.BookShopApp.data.repository.product.ProductRepositoryImp
import com.example.BookShopApp.datasource.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RatingViewModel : ViewModel() {
    private var _ratingList = MutableLiveData<List<Rating>>()
    val ratingList: LiveData<List<Rating>> get() = _ratingList
    private val productRepo: ProductRepository = ProductRepositoryImp(RemoteDataSource())

    fun getAllRatingByBook(bookId: Int, limit: Int, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = productRepo.getAllRatingByBook(bookId, limit, page)
            if (response.isSuccessful) {
                _ratingList.postValue(response.body()?.ratingDtos)
            } else {
                Log.d("GetAllRating", "NULL")
            }
        }
    }
}