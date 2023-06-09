package com.example.e_com_kart.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities= [ProductModel::class], version = 1)

abstract class AppDatabase:RoomDatabase(){

    companion object{
        private var database:AppDatabase?=null
        private var database_name="EcomKart"

        @Synchronized
        fun getInstance(context: Context):AppDatabase{
            if(database==null){
                database=Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java,
                database_name).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return database!!
        }
    }

    abstract fun ProductDao():productDao

}