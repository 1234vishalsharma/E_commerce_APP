package com.example.e_com_kart.Model

import android.net.Uri

data class UserModel(
    val username:String? = "",
    val usernumber:String? = "",
    val state:String? = "",
    val city:String? = "",
    val street:String? = "",
    val pincode:String? = "",
    val profile_pic:Uri?=null)
