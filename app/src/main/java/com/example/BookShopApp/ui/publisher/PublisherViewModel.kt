package com.example.BookShopApp.ui.publisher

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BookShopApp.data.model.response.product.ProductState
import com.example.BookShopApp.data.repository.cart.CartRepository
import com.example.BookShopApp.data.repository.cart.CartRepositoryImp
import com.example.BookShopApp.data.repository.product.ProductRepository
import com.example.BookShopApp.data.repository.product.ProductRepositoryImp
import com.example.BookShopApp.data.repository.search.SearchRepository
import com.example.BookShopApp.data.repository.search.SearchRepositoryImp
import com.example.BookShopApp.datasource.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PublisherViewModel : ViewModel() {
    private var _productState = MutableLiveData<ProductState>()
    val productState: LiveData<ProductState> get() = _productState
    private var productRepository: ProductRepository? = ProductRepositoryImp(RemoteDataSource())
    private var searchRepository: SearchRepository? = SearchRepositoryImp(RemoteDataSource())
    private var cartRepository: CartRepository? = CartRepositoryImp(RemoteDataSource())
    var job: Job? = null

    fun getProductsBySupplier(categoryId: Int, limit: Int, page: Int, desLength: Int) {
        job?.cancel()
        job=viewModelScope.launch(Dispatchers.IO) {
            val response =
                productRepository?.getProductsBySupplier(categoryId, limit, page, desLength)
            if (response?.isSuccessful == true) {
                _productState.postValue(ProductState(response.body()?.products, true))
            } else {
                Log.d("getProdcutInSupplier", "NULLLL")
            }
        }
    }

    fun getSearchSupplierProduct(supplierId: Int, currentPage: Int, queryString: String) {
        job?.cancel()
        job=viewModelScope.launch(Dispatchers.IO) {
            val response =
                searchRepository?.getSearchSupplierProducts(
                    supplierId,
                    10,
                    currentPage,
                    100,
                    queryString
                )
            if (response?.isSuccessful == true) {
                _productState.postValue(ProductState(response.body()?.products, false))
            } else {
                Log.d("searchSupplier", "NULLLL")
            }
        }
    }

    fun addItemToCart(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = cartRepository?.addCartItem(productId)
            if (response?.isSuccessful == true) {
            } else {
                Log.d("AddToCart", "NULL")
            }
        }
    }
}