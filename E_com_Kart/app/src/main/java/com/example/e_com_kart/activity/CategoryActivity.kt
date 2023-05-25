package com.example.e_com_kart.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.e_com_kart.Adapter.CategoryItemAdapter
import com.example.e_com_kart.Adapter.ProductAdapter
import com.example.e_com_kart.Model.addproduct_model
import com.example.e_com_kart.R
import com.example.e_com_kart.databinding.ActivityCategoryBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CategoryActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding=ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getProducts(intent.getStringExtra("categ"))
    }

    private fun getProducts(cat: String?) {
        val list=ArrayList<addproduct_model>()
        Firebase.firestore.collection("Products").whereEqualTo("product_category",cat)
            .get().addOnSuccessListener {
                list.clear()
                for(doc in it.documents){
                    val data=doc.toObject(addproduct_model::class.java)
                    list.add(data!!)
                }
                binding.categrecycler.adapter= CategoryItemAdapter(this,list)
            }
    }
}