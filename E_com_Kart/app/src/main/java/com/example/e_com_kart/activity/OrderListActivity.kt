package com.example.e_com_kart.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.e_com_kart.Adapter.Orderitemadapter
import com.example.e_com_kart.Model.addproduct_model
import com.example.e_com_kart.R
import com.example.e_com_kart.databinding.ActivityOrderListBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class OrderListActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOrderListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getOrderedProduct()

    }

    private fun getOrderedProduct() {
        var list= ArrayList<addproduct_model>()
            val firestore= FirebaseFirestore.getInstance().collection("AllOrders")

        firestore.get()
            .addOnSuccessListener { querySnapShot ->
            for(doc in querySnapShot){
                val name = doc.getString("name") ?: ""
                val coverimg = doc.getString("Cover_Img") ?: ""
                val product_sp = doc.getString("S_Price") ?: ""
                val product=addproduct_model(product_name = name, product_cover_img = coverimg, product_SP = product_sp)

                list.add(product)
            }
                binding.orderlistrecycler.adapter=Orderitemadapter(this, list)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wronng with orderlist", Toast.LENGTH_SHORT).show()
            }
    }
}