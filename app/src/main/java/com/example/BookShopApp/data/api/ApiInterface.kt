package com.example.BookShopApp.data.api

import com.example.BookShopApp.data.model.*
import com.example.BookShopApp.data.model.response.*
import com.example.BookShopApp.data.model.response.auth.AuthResponse
import com.example.BookShopApp.data.model.response.author.AuthorFamousList
import com.example.BookShopApp.data.model.response.author.AuthorInfor
import com.example.BookShopApp.data.model.response.order.OrderList
import com.example.BookShopApp.data.model.response.product.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {
    @FormUrlEncoded
    @POST("customers/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<AuthResponse>

    @FormUrlEncoded
    @POST("customers/forgotPass")
    suspend fun fotgotPass(
        @Field("email") email: String,
    ): Response<Message>

    @FormUrlEncoded
    @POST("customers")
    suspend fun register(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("password") password: String,
    ): Response<AuthResponse>

    @GET("products/recommend")
    suspend fun getProductBanner(): Response<BannerList>

    @GET("products")
    suspend fun getProducts(): Response<ProductList>

    @GET("category")
    suspend fun getAllCategory(): Response<CategoryList>

    @GET("category/hot")
    suspend fun getHotCategory(): Response<CategoryList>

    @GET("products/new")
    suspend fun getNewBook(): Response<BookInHomeList>

    @GET("products/hot")
    suspend fun getHotBook(): Response<BookInHomeList>

    @GET("products/search")
    suspend fun getSearchProducts(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("description_length") descriptionLength: Int,
        @Query("query_string") queryString: String,
        @Query("filter_type") filterType: Int,
        @Query("price_sort_order") priceSortOrder: String,
    ): Response<ProductList>

    @GET("products/search")
    suspend fun getSearchHistory(
        @Query("query_string") queryString: String,
    ): Response<ProductList>

    @GET("products/author/search")
    suspend fun getSearchAuthorProducts(
        @Query("author_id") authorId: Int,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("description_length") descriptionLength: Int,
        @Query("query_string") queryString: String,
    ): Response<ProductList>

    @GET("products/category/search")
    suspend fun getSearchCategoryProducts(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("description_length") descriptionLength: Int,
        @Query("query_string") queryString: String,
        @Query("category_id") categoryId: Int,
    ): Response<ProductList>

    @GET("products/supplier/search")
    suspend fun getSearchSupplierProducts(
        @Query("supplier_id") supplierId: Int,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("description_length") descriptionLength: Int,
        @Query("query_string") queryString: String,
    ): Response<ProductList>

    @GET("products/{product_id}")
    suspend fun getProductInfo(@Path("product_id") product_id: Int): Response<ProductInfoList>

    @GET("products/author")
    suspend fun getProductsByAuthor(
        @Query("author_id") author_id: Int,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("description_length") description_length: Int,
    ): Response<ProductsByAuthor>

    @GET("products/incategory/{categoryId}")
    suspend fun getProductsByCategory(
        @Path("categoryId") categoryId: Int,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("description_length") description_length: Int,
    ): Response<ProductList>

    @GET("products/supplier")
    suspend fun getProductsBySupplier(
        @Query("supplier_id") supplierId: Int,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("description_length") description_length: Int,
    ): Response<ProductList>

    @GET("author/hot")
    suspend fun getHotAuthor(): Response<AuthorFamousList>

    @GET("author/{authorId}")
    suspend fun getAuthor(@Path("authorId") authorId: Int): Response<AuthorInfor>

    @GET("products/new")
    suspend fun getSearchNewProduct(): Response<ProductNewList>

    @GET("customers")
    suspend fun getCustomer(): Response<Customer>

    @FormUrlEncoded
    @PUT("customers")
    suspend fun updateCustomer(
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("date_of_birth") dateofbirth: String,
        @Field("gender") gender: String,
        @Field("mob_phone") mobphone: String,
    ): Response<Customer>

    @FormUrlEncoded
    @PUT("customers")
    suspend fun updateOrderInfor(
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("mob_phone") mobphone: String,
    ): Response<Customer>

    @FormUrlEncoded
    @POST("customers/changePass")
    suspend fun changePassword(
        @Field("email") email: String,
        @Field("old_password") old_password: String,
        @Field("new_password") new_password: String,
    ): Response<Customer>

    @Multipart
    @POST("customers/update/avatar")
    suspend fun changeAvatar(
        @Part image: MultipartBody.Part,
    ): Response<Customer>

    @GET("orders")
    suspend fun getOrderHistory(): Response<OrderList>

    @GET("orders/{orderId}")
    suspend fun getOrderDetail(@Path("orderId") orderId: Int): Response<OrderDetail>

    @FormUrlEncoded
    @POST("shoppingCart/add")
    suspend fun addProduct2Cart(@Field("product_id") productId: Int): Response<List<CartItem>>

    @FormUrlEncoded
    @POST("wishlist/add")
    suspend fun addItemToWishList(@Field("product_id") productId: Int): Response<Message>

    @POST("shoppingCart/add/wishlist")
    suspend fun addAllItem2Cart(): Response<Message>

    @DELETE("shoppingCart/empty")
    suspend fun deleteAllItemCart(): Response<Message>

    @FormUrlEncoded
    @POST("shoppingCart/update")
    suspend fun changeProductQuantityInCart(
        @Field("item_id") itemId: Int,
        @Field("quantity") quantity: Int,
    ): Response<Message>?

    @DELETE("shoppingCart/removeProduct/{item_id}")
    suspend fun removeItemInCart(
        @Path("item_id") itemId: Int,
    ): Response<Message>?

    @DELETE("wishlist/remove/{product_id}")
    suspend fun removeItemInWishList(@Path("product_id") productId: Int): Response<Message>

    @GET("wishlist")
    suspend fun getWishList(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("description_length") description_length: Int,
    ): Response<WishlistResponse>?

    @GET("shoppingCart")
    suspend fun getAllCart(): Response<Cart>?

    @FormUrlEncoded
    @POST("orders")
    suspend fun createOrder(
        @Field("cart_id") cartId: String,
        @Field("shipping_id") shippingId: Int,
        @Field("receiver_id") receiverId: Int,
    ): Response<Message>

    @GET("/receiver/{receiver_id}")
    suspend fun getReceiverInfo(@Path("receiver_id") receiverId: Int): Response<Receiver>

    @FormUrlEncoded
    @POST("/receiver")
    suspend fun addReceiverInfo(
        @Field("receiver_name") receiverName: String,
        @Field("receiver_phone") receiverPhone: String,
        @Field("receiver_address") receiverAddress: String,
    ): Response<Message>

    @GET("/receiver/default")
    suspend fun getReceiverDefault(): Response<Receiver>

    @GET("/receiver")
    suspend fun getReceivers(): Response<ReceiverResponse>
}