package com.example.e_com_kart.Model

data class addproduct_model(
    val product_name: String?="",
    val product_desc: String?="",
    val product_cover_img: String?="",
    val product_category: String?="",
    val product_ID: String?="",
    val product_MRP: String?="",
    val product_SP: String?="",
    val product_img: ArrayList<String> =  ArrayList()
)
