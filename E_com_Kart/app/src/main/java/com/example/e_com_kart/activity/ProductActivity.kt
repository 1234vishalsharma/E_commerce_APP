package com.example.e_com_kart.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.e_com_kart.MainActivity
import com.example.e_com_kart.R
import com.example.e_com_kart.databinding.ActivityProductBinding
import com.example.e_com_kart.fragment.CartFragment
import com.example.e_com_kart.roomdb.AppDatabase
import com.example.e_com_kart.roomdb.ProductModel
import com.example.e_com_kart.roomdb.productDao
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProductBinding.inflate(layoutInflater)
        val pro_id=intent.getStringExtra("id")
        if(pro_id!=null){
            getProductDetails(pro_id)
        }
        else{
            Toast.makeText(this, "Unable to Fetch Product details", Toast.LENGTH_SHORT).show()
        }
        setContentView(binding.root)

    }

    private fun getProductDetails(Product_ID: String) {
        Firebase.firestore.collection("Products").document(Product_ID).get()
            .addOnSuccessListener {
                val list = it.get("product_img") as ArrayList<String>
                val name = it.getString("product_name")
                val product_sp = it.getString("product_SP")
                val product_desc = it.getString("product_desc")
                binding.textView5.text=name
                binding.textView2.text=product_sp
                binding.textView7.text=product_desc

                val slidelist= ArrayList<SlideModel>()
                for(doc in list){
                    slidelist.add(SlideModel(doc,ScaleTypes.CENTER_CROP))
                }

                CartAction(Product_ID,name,product_sp,it.getString("product_cover_img"))

                binding.imageSlider.setImageList(slidelist)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

    }

    private fun CartAction(productId: String, name: String?, productSp: String?, cover_img: String?) {
        val prodao=AppDatabase.getInstance(this).ProductDao()
        if(prodao.isExit(productId) != null){
            binding.textView9.text="Go To Cart"
        }else{
            binding.textView9.text="Add To Cart"
        }
        binding.textView9.setOnClickListener {
            if(prodao.isExit(productId) != null){
               openCart()
            }else{
               addtoCart(prodao,productId,name,productSp,cover_img)
            }
        }
    }

    private fun addtoCart(prodao: productDao, productId: String, name: String?, productSp: String?, coverImg: String?) {
        val data=ProductModel(productId,name,productSp,coverImg)
        lifecycleScope.launch(Dispatchers.IO){
            prodao.insertProduct(data)
            binding.textView9.text="Go To Cart"
        }
    }


    private fun openCart() {
        val preference=this.getSharedPreferences("info", MODE_PRIVATE)
        val editor=preference.edit()
        editor.putBoolean("isCart",true)
        editor.apply()

        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

}


