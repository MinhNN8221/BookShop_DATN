package com.example.BookShopApp.datasource.remote

import com.example.BookShopApp.data.api.RetrofitClient
import com.example.BookShopApp.data.model.*
import com.example.BookShopApp.data.model.response.*
import com.example.BookShopApp.data.model.response.auth.AuthResponse
import com.example.BookShopApp.data.model.response.author.AuthorFamousList
import com.example.BookShopApp.data.model.response.author.AuthorInfor
import com.example.BookShopApp.data.model.response.order.OrderList
import com.example.BookShopApp.data.model.response.product.*
import com.example.BookShopApp.datasource.IDataSource
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Field

class RemoteDataSource() : IDataSource {

    override suspend fun login(email: String, password: String): Response<AuthResponse>? {
        return RetrofitClient.apiService.login(email, password)
    }

    override suspend fun forgotPassword(email: String): Response<Message> {
        return RetrofitClient.apiService.fotgotPass(email)
    }

    override suspend fun register(
        email: String,
        name: String,
        password: String,
    ): Response<AuthResponse> {
        return RetrofitClient.apiService.register(email, name, password)
    }

    override suspend fun getProductsBanner(): Response<BannerList>? {
        return RetrofitClient.apiService.getProductBanner()
    }

    override suspend fun getSearchProducts(
        limit: Int,
        page: Int,
        description_length: Int,
        query_string: String,
        filter_type: Int,
        price_sort_order: String,
    ): Response<ProductList>? {
        return RetrofitClient.apiService.getSearchProducts(
            limit,
            page,
            description_length,
            query_string,
            filter_type,
            price_sort_order,
        )
    }

    override suspend fun getSearchHistory(query_string: String): Response<ProductList> {
        return RetrofitClient.apiService.getSearchHistory(query_string)
    }

    override suspend fun getSearchAuthorProducts(
        authorId: Int,
        limit: Int,
        page: Int,
        description_length: Int,
        query_string: String,
    ): Response<ProductList>? {
        return RetrofitClient.apiService.getSearchAuthorProducts(
            authorId,
            limit,
            page,
            description_length,
            query_string,
        )
    }

    override suspend fun getSearchCategoryProducts(
        limit: Int,
        page: Int,
        description_length: Int,
        query_string: String,
        categoryId: Int,
    ): Response<ProductList>? {
        return RetrofitClient.apiService.getSearchCategoryProducts(
            limit,
            page,
            description_length,
            query_string,
            categoryId,
        )
    }

    override suspend fun getSearchSupplierProducts(
        supplierId: Int,
        limit: Int,
        page: Int,
        description_length: Int,
        query_string: String,
    ): Response<ProductList>? {
        return RetrofitClient.apiService.getSearchSupplierProducts(
            supplierId,
            limit,
            page,
            description_length,
            query_string
        )
    }

    override suspend fun getProductInfo(id: Int): Response<ProductInfoList>? {
        return RetrofitClient.apiService.getProductInfo(id)
    }

    override suspend fun getProductsByAuthor(
        id: Int,
        limit: Int,
        page: Int,
        description_length: Int,
    ): Response<ProductsByAuthor>? {
        return RetrofitClient.apiService.getProductsByAuthor(id, limit, page, description_length)
    }

    override suspend fun getProductsByCategory(
        id: Int,
        limit: Int,
        page: Int,
        description_length: Int,
    ): Response<ProductList> {
        return RetrofitClient.apiService.getProductsByCategory(id, limit, page, description_length)
    }

    override suspend fun getProductsBySupplier(
        id: Int,
        limit: Int,
        page: Int,
        description_length: Int,
    ): Response<ProductList>? {
        return RetrofitClient.apiService.getProductsBySupplier(id, limit, page, description_length)
    }

    override suspend fun getSearchNewProduct(): Response<ProductNewList>? {
        return RetrofitClient.apiService.getSearchNewProduct()
    }

    override suspend fun getCustomer(): Response<Customer>? {
        return RetrofitClient.apiService.getCustomer()
    }

    override suspend fun updateCustomer(
        name: String,
        address: String,
        dob: String,
        gender: String,
        mob_phone: String,
    ): Response<Customer>? {
        return RetrofitClient.apiService.updateCustomer(name, address, dob, gender, mob_phone)
    }

    override suspend fun updateOrderInfor(
        name: String,
        address: String,
        mob_phone: String,
    ): Response<Customer>? {
        return RetrofitClient.apiService.updateOrderInfor(name, address, mob_phone)
    }

    override suspend fun changePassword(
        email: String,
        old_password: String,
        new_password: String,
    ): Response<Customer>? {
        return RetrofitClient.apiService.changePassword(email, old_password, new_password)
    }

    override suspend fun changeAvatar(image: MultipartBody.Part): Response<Customer>? {
        return RetrofitClient.apiService.changeAvatar(image)
    }

    override suspend fun getOrderHistory(): Response<OrderList>? {
        return RetrofitClient.apiService.getOrderHistory()
    }

    override suspend fun getOrderDetail(orderId: Int): Response<OrderDetail>? {
        return RetrofitClient.apiService.getOrderDetail(orderId)
    }

    override suspend fun getAuthor(authorId: Int): Response<AuthorInfor>? {
        return RetrofitClient.apiService.getAuthor(authorId)
    }

    override suspend fun getHotAuthor(): Response<AuthorFamousList>? {
        return RetrofitClient.apiService.getHotAuthor()
    }

    override suspend fun addCartItem(productId: Int): Response<List<CartItem>>? {
        return RetrofitClient.apiService.addProduct2Cart(productId)
    }

    override suspend fun addAllItemToCart(): Response<Message> {
        return RetrofitClient.apiService.addAllItem2Cart()
    }

    override suspend fun deleteAllItemCart(): Response<Message> {
        return RetrofitClient.apiService.deleteAllItemCart()
    }

    override suspend fun changeProductQuantityInCart(
        itemId: Int,
        quantity: Int,
    ): Response<Message>? {
        return RetrofitClient.apiService.changeProductQuantityInCart(itemId, quantity)
    }

    override suspend fun removeItemInCart(itemId: Int): Response<Message>? {
        return RetrofitClient.apiService.removeItemInCart(itemId)
    }

    override suspend fun addItemToWishList(productId: Int): Response<Message>? {
        return RetrofitClient.apiService.addItemToWishList(productId)
    }

    override suspend fun removeItemInWishList(productId: Int): Response<Message> {
        return RetrofitClient.apiService.removeItemInWishList(productId)
    }

    override suspend fun getWishList(
        limit: Int,
        page: Int,
        description_length: Int,
    ): Response<WishlistResponse>? {
        return RetrofitClient.apiService.getWishList(limit, page, description_length)
    }

    override suspend fun getAllCart(): Response<Cart>? {
        return RetrofitClient.apiService.getAllCart()
    }

    override suspend fun getAllCategory(): Response<CategoryList>? {
        return RetrofitClient.apiService.getAllCategory()
    }

    override suspend fun getHotCategory(): Response<CategoryList>? {
        return RetrofitClient.apiService.getHotCategory()
    }

    override suspend fun getNewBook(): Response<BookInHomeList>? {
        return RetrofitClient.apiService.getNewBook()
    }

    override suspend fun getHotBook(): Response<BookInHomeList>? {
        return RetrofitClient.apiService.getHotBook()
    }

    override suspend fun createOrder(
        cartId: String,
        shippingId: Int,
        receiverId: Int,
        paymentId: Int,
    ): Response<Message> {
        return RetrofitClient.apiService.createOrder(
            cartId, shippingId, receiverId, paymentId
        )
    }
    override suspend fun updateOrderStatus(orderId: Int, orderStatusId: Int): Response<Message> {
        return RetrofitClient.apiService.updateOrderStatus(orderId, orderStatusId)
    }
    override suspend fun getReceiverInfo(receiverId: Int): Response<Receiver> {
        return RetrofitClient.apiService.getReceiverInfo(receiverId)
    }

    override suspend fun addReceiverInfo(
        receiverName: String,
        receiverPhone: String,
        receiverAddress: String,
        isDefault: Int,
    ): Response<Message> {
        return RetrofitClient.apiService.addReceiverInfo(
            receiverName,
            receiverPhone,
            receiverAddress,
            isDefault,
        )
    }

    override suspend fun updateReceiverInfo(
        receiverName: String,
        receiverPhone: String,
        receiverAddress: String,
        receiverId: Int,
        isDefault: Int,
        isSelected:Int,
    ): Response<Message> {
        return RetrofitClient.apiService.updateReceiverInfo(
            receiverName,
            receiverPhone,
            receiverAddress,
            receiverId,
            isDefault,
            isSelected,
        )
    }

    override suspend fun getReceiverDefault(): Response<Receiver> {
        return RetrofitClient.apiService.getReceiverDefault()
    }

    override suspend fun getReceiverSelected(): Response<Receiver> {
        return RetrofitClient.apiService.getReceiverSelected()
    }

    override suspend fun getReceivers(): Response<ReceiverResponse> {
        return RetrofitClient.apiService.getReceivers()
    }

    override suspend fun updateReceiverDefaultIsSelected(): Response<Message> {
        return RetrofitClient.apiService.updateReceiverDefaultIsSelected()
    }
    override suspend fun removeReceiver(receiverId: Int): Response<Message> {
        return RetrofitClient.apiService.removeReceiver(receiverId)
    }
}