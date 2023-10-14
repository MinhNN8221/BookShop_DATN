package com.example.BookShopApp.data.repository.search.historysearch

import android.app.Application
import com.example.BookShopApp.datasource.local.db.ProductDatabase
import com.example.BookShopApp.datasource.local.db.dao.ProductDao
import com.example.BookShopApp.datasource.local.db.entity.ProductDb

class HistorySearchRepositoryImp(application: Application) : HistorySearchRepository {
    private val productDao: ProductDao

    init {
        val productDatabase: ProductDatabase = ProductDatabase.getInstance(application)
        productDao = productDatabase.productDao()
    }

    override fun getAllProducts(idCustomer: Int): List<String> {
        return productDao.getAll(idCustomer)
    }

    override fun insertProduct(product: ProductDb) {
        val products = productDao.getAllProductName(product.productName)
        if (products.isEmpty()) {
            return productDao.insert(product)
        } else {
            productDao.deleteColumnName(product.productName)
            return productDao.insert(product)
        }
    }

    override fun deleteAllProducts() {
        return productDao.deleteAllData()
    }

    override fun deleteColName(productName: String) {
        return productDao.deleteColumnName(productName)
    }

    override fun getAllProductName(query: String): List<String> {
        return productDao.getAllProductName(query)
    }
}