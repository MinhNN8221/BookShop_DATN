package com.example.BookShopApp.ui.productdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BookShopApp.data.model.response.Message
import com.example.BookShopApp.data.model.response.product.ProductInfoList
import com.example.BookShopApp.data.repository.cart.CartRepositoryImp
import com.example.BookShopApp.data.repository.product.ProductRepository
import com.example.BookShopApp.data.repository.product.ProductRepositoryImp
import com.example.BookShopApp.data.repository.wishlist.WishListRepositoryImp
import com.example.BookShopApp.datasource.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductdetailViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val _productListInfo = MutableLiveData<ProductInfoList?>()
    val productInfo: LiveData<ProductInfoList?> get() = _productListInfo
    private val _messageAdd = MutableLiveData<Message>()
    val messageAdd: LiveData<Message> get() = _messageAdd
    private val _messageRemove = MutableLiveData<Message>()
    val messageRemove: LiveData<Message> get() = _messageRemove

    private var productRepository: ProductRepository? = ProductRepositoryImp(RemoteDataSource())
    private var cartRepository: CartRepositoryImp? = CartRepositoryImp(RemoteDataSource())
    private var wishListRepository: WishListRepositoryImp? =
        WishListRepositoryImp(RemoteDataSource())

    fun getProductInfo(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = productRepository?.getProductInfo(id)
            if (response?.isSuccessful == true) {
                _productListInfo.postValue(response.body())
            } else {
                Log.d("PRODUCTINFONULL", "NULLLL")
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

    fun addItemToWishList(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = wishListRepository?.addItemToWishList(productId)
            if (response?.isSuccessful == true) {
                _messageAdd.postValue(response.body())
            } else {
                Log.d("ADDWISHLISTNULL", "FAIL")
            }
        }
    }

    fun removeItemInWishList(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = wishListRepository?.removeItemInWishList(productId)
            if (response?.isSuccessful == true) {
                _messageRemove.postValue(response.body())
            } else {
                Log.d("REMOVEITEMINWISHLIST", "FAIL")
            }
        }
    }
}