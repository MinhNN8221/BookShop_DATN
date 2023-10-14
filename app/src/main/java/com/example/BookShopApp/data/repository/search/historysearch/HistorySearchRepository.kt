package com.example.BookShopApp.data.repository.search.historysearch


import com.example.BookShopApp.datasource.local.db.entity.ProductDb

interface HistorySearchRepository {

    fun getAllProducts(idCustomer: Int): List<String>
    fun insertProduct(product: ProductDb)
    fun deleteAllProducts()
    fun deleteColName(productName: String)
    fun getAllProductName(query: String): List<String>
}