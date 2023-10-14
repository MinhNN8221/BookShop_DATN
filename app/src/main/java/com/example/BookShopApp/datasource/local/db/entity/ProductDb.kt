package com.example.BookShopApp.datasource.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "product")
data class ProductDb(
    @PrimaryKey(autoGenerate = true)
    var id: Int=0,
    @ColumnInfo(name = "idCustomer")
    var idCustomer: Int,
    @ColumnInfo(name = "productName")
    var productName: String,
) : Serializable
