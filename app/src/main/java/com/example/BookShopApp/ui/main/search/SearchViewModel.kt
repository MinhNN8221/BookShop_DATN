package com.example.BookShopApp.ui.main.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BookShopApp.data.model.Product
import com.example.BookShopApp.data.model.response.product.ProductState
import com.example.BookShopApp.data.repository.cart.CartRepository
import com.example.BookShopApp.data.repository.cart.CartRepositoryImp
import com.example.BookShopApp.data.repository.search.SearchRepository
import com.example.BookShopApp.data.repository.search.SearchRepositoryImp
import com.example.BookShopApp.data.repository.search.historysearch.HistorySearchRepository
import com.example.BookShopApp.data.repository.search.historysearch.HistorySearchRepositoryImp
import com.example.BookShopApp.datasource.local.db.entity.ProductDb
import com.example.BookShopApp.datasource.remote.RemoteDataSource
import kotlinx.coroutines.*

class SearchViewModel(application: Application) : ViewModel() {
    private val _productState = MutableLiveData<ProductState>()
    val productState: LiveData<ProductState> get() = _productState
    private val _historyList = MutableLiveData<List<String>>()
    val historyList: LiveData<List<String>> get() = _historyList

    private val _productNameList = MutableLiveData<List<Product>>()
    val productNameList: LiveData<List<Product>> get() = _productNameList

    private var searchRepository: SearchRepository? = SearchRepositoryImp(RemoteDataSource())
    private var cartRepository: CartRepository? = CartRepositoryImp(RemoteDataSource())
    private val historySearchRepository: HistorySearchRepository =
        HistorySearchRepositoryImp(application)
    var job: Job? = null

    fun getSearchProducts(
        limit: Int,
        page: Int,
        description_length: Int,
        query_string: String,
        filter_type: Int,
        price_sort_order: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = searchRepository?.getSearchProducts(
                limit,
                page,
                description_length,
                query_string,
                filter_type,
                price_sort_order
            )
            if (response?.isSuccessful == true) {
                if (query_string.isEmpty()) {
                    _productState.postValue(ProductState(response.body()?.products, true))
                } else {
                    _productState.postValue(ProductState(response.body()?.products, false))
                }
            } else {
                Log.d("SearchProduct", "NULLLL")
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

    fun getHistorySearchLocal(idCustomer: Int) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            val response = historySearchRepository.getAllProducts(idCustomer)
            _historyList.postValue(response)
        }
    }

    fun insertHistorySearchLocal(product: ProductDb) {
        viewModelScope.launch(Dispatchers.IO) {
            historySearchRepository.insertProduct(product)
        }
    }

    fun deleteHistorySearchLocal() {
        viewModelScope.launch(Dispatchers.IO) {
            historySearchRepository.deleteAllProducts()
        }
    }

    fun removeItemHistorySearchLocal(productName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            historySearchRepository.deleteColName(productName)
        }
    }

    fun getSearchHistory(queryString: String) {
        job?.cancel()
        job=viewModelScope.launch(Dispatchers.IO) {
            val response = searchRepository?.getSearchHistory(queryString)
            if (response?.isSuccessful == true) {
                _productNameList.postValue(response.body()?.products)
            } else {
                Log.d("SearchHistory", "NULL")
            }
        }
    }
}