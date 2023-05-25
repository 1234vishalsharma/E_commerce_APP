package com.example.e_com_kart.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_com_kart.MainActivity
import com.example.e_com_kart.databinding.ActivityCheckoutBinding
import com.example.e_com_kart.fragment.HomeFragment
import com.example.e_com_kart.roomdb.AppDatabase
import com.example.e_com_kart.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


class CheckoutActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var binding:ActivityCheckoutBinding
    private var amount:Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val checkout=Checkout()
        var price=intent.getStringExtra("Total_Cost")


        checkout.setKeyID("rzp_test_rbvkLcPe2r96R0");

        try {
            val options = JSONObject()
            options.put("name", "E_Com_Kart")
            options.put("description", "Fast and Easy make orders")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#f4d415")
            options.put("currency", "INR")
            options.put("amount", (100 * (price!!).toInt()))//pass amount in currency subunits
            options.put("prefill.email", "vishalsharma15122003@gmail.com")
            options.put("prefill.contact", "9899955304")
            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Something went Wrong with payment", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show()
        uploaddata()
    }
    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,HomeFragment::class.java))
    }

    private fun uploaddata() {
        val id=intent.getStringArrayListExtra("Product_Ids")
        for(curr_id in id!!){
            fetchproduct(curr_id)
        }
    }

    private fun fetchproduct(currId: String?) {

        val dao=AppDatabase.getInstance(this).ProductDao()

        Firebase.firestore.collection("Products").document(currId!!)
            .get()
            .addOnSuccessListener {
                lifecycleScope.launch(Dispatchers.IO){
                    dao.deleteProduct(ProductModel(currId))
                }
                savedata(it.getString("product_name"),it.getString("product_SP"),it.getString("product_category"),
                it.getString("product_desc"),it.getString("product_cover_img"),it.getString("product_ID"))
            }
            .addOnFailureListener {
                Toast.makeText(this, "Unable to Fetch product details", Toast.LENGTH_SHORT).show()
            }
    }

    private fun savedata(name: String?, product_SP: String?, Category: String?, Description: String?, Cover_img: String?,Product_ID:String?) {
        val data= hashMapOf<String,Any>()
        val preferences=this.getSharedPreferences("user", MODE_PRIVATE)
        data["name"]=name!!
        data["S_Price"]=product_SP!!
        data["Category"]=Category!!
        data["Description"]=Description!!
        data["Cover_Img"]=Cover_img!!
        data["Product_ID"]=Product_ID!!

        data["Status"]="Ordered"
        data["user_id"]=preferences.getString("Number","")!!

        val firestore=Firebase.firestore.collection("AllOrders")
        val key=firestore.document().id
        data["Order_ID"]=key
        firestore.add(data)
            .addOnSuccessListener {
            Toast.makeText(this, "Order Placed successfully", Toast.LENGTH_SHORT).show()
            val intent:Intent=Intent(this,OrderListActivity::class.java)
            intent.putExtra("ID",key)
            startActivity(intent)
            finish()
        }
            .addOnFailureListener {
                Toast.makeText(this, "Something went Wrong while placing orders", Toast.LENGTH_SHORT).show()
            }
    }


}