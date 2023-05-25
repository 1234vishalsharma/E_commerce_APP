package com.example.e_com_kart.roomdb

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "products")
class ProductModel(
                   @PrimaryKey
                   val product_ID:String,
                   @ColumnInfo(name = "ProductName")
                   val product_name:String? ="" ,
                   @ColumnInfo(name = "ProductSP")
                   val product_SP:String?="",
                   @ColumnInfo(name = "ProductImage")
                   val product_Cover_Img:String?=""
) {

}