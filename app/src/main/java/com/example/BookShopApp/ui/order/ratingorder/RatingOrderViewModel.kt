package com.example.BookShopApp.ui.order.ratingorder

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BookShopApp.data.model.request.RatingRequest
import com.example.BookShopApp.data.model.response.Message
import com.example.BookShopApp.data.repository.product.ProductRepository
import com.example.BookShopApp.data.repository.product.ProductRepositoryImp
import com.example.BookShopApp.datasource.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RatingOrderViewModel : ViewModel() {
    private var _message = MutableLiveData<Message>()
    val message:LiveData<Message> get() = _message
    private val producRepo:ProductRepository=ProductRepositoryImp(RemoteDataSource())

    fun createRatingOrder(ratingRequests:List<RatingRequest>){
        viewModelScope.launch(Dispatchers.IO){
            val response=producRepo.createRatingOrder(ratingRequests)
            if(response.isSuccessful){
                _message.postValue(response.body())
            }else{
                Log.d("createRatingOrder", "NULL")
            }
        }
    }
}