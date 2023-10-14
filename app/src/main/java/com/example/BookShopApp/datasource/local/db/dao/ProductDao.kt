package com.example.BookShopApp.datasource.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.BookShopApp.datasource.local.db.entity.ProductDb

@Dao
interface ProductDao {
    @Query("SELECT productName FROM product WHERE idCustomer = :idCustomer")
    fun getAll(idCustomer: Int): List<String>

    @Query("SELECT productName FROM product WHERE productName = :query")
    fun getAllProductName(query: String): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: ProductDb)

    @Query("DELETE FROM product")
    fun deleteAllData()

    @Query("DELETE FROM product WHERE productName = :productName")
    fun deleteColumnName(productName: String)
}