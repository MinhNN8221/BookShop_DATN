package com.example.BookShopApp.ui.category

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

class CategoryBookViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private var _productState = MutableLiveData<ProductState>()
    val producList: LiveData<ProductState> get() = _productState
    private var productRepository: ProductRepository? = ProductRepositoryImp(RemoteDataSource())
    private var cartRepository: CartRepository? = CartRepositoryImp(RemoteDataSource())
    private var searchRepository: SearchRepository? = SearchRepositoryImp(RemoteDataSource())
    var job: Job? = null
    fun getProductsInCategory(categoryId: Int, limit: Int, page: Int, desLength: Int) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            val response =
                productRepository?.getProductsByCategory(categoryId, limit, page, desLength)
            if (response?.isSuccessful == true) {
                _productState.postValue(ProductState(response.body()?.products, true))
            } else {
                Log.d("CategroBookNULL", "NULL")
            }
        }
    }

    fun addItemToCart(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = cartRepository?.addCartItem(productId)
            if (response?.isSuccessful == true) {
            } else {
                Log.d("ADDITEMTOCARTNULL", "NULL")
            }
        }
    }

    fun getSearchCategoryProducts(categoryId: Int, currentPage: Int, queryString: String) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            val response = searchRepository?.getSearchCategoryProducts(
                10,
                currentPage,
                100,
                queryString,
                categoryId,
            )
            if (response?.isSuccessful == true) {
                _productState.postValue(ProductState(response.body()?.products, false))
            } else {
                Log.d("SearchCategory", "NULL")
            }
        }
    }
}