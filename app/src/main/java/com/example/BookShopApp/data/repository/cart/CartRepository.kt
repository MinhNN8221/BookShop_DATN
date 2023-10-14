package com.example.BookShopApp.data.repository.cart

import com.example.BookShopApp.data.model.Cart
import com.example.BookShopApp.data.model.CartItem
import com.example.BookShopApp.data.model.response.Message
import retrofit2.Response

interface CartRepository {
    suspend fun addCartItem(productId: Int): Response<List<CartItem>>?
    suspend fun getAllCart(): Response<Cart>?
    suspend fun addAllItemToCart(): Response<Message>
    suspend fun deleteAllItemCart(): Response<Message>
    suspend fun changeProductQuantityInCart(itemId: Int, quantity: Int): Response<Message>?
    suspend fun removeItemInCart(itemId: Int): Response<Message>?
}