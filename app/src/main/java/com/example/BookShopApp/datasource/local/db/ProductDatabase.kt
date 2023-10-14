package com.example.BookShopApp.datasource.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.BookShopApp.datasource.local.db.dao.ProductDao
import com.example.BookShopApp.datasource.local.db.entity.ProductDb

@Database(entities = [ProductDb::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile// truy cập và gán giá trịluôn được thực hiện trên main memory
        private var INSTANCE: ProductDatabase? = null
        fun getInstance(context: Context): ProductDatabase {
            if(INSTANCE==null){
                INSTANCE=Room.databaseBuilder(context, ProductDatabase::class.java, "localproduct").build()
            }
            return INSTANCE!!
        }
    }
}