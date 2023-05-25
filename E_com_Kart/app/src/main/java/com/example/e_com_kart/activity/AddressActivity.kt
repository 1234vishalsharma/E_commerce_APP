package com.example.e_com_kart.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.e_com_kart.databinding.ActivityAddressBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddressBinding
    private lateinit var preference: SharedPreferences
    private lateinit var Total_Cost:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference =getSharedPreferences("user", MODE_PRIVATE)

        Total_Cost= intent.getStringExtra("Total_Cost").toString()

        loadinfo()

        binding.proceedbtn.setOnClickListener {
                Validatedata(binding.UserName.text.toString(),
                binding.UserNumber.text.toString(),
                binding.state.text.toString(),
                binding.city.text.toString(),
                binding.street.text.toString(),
                binding.pincode.text.toString())
        }
    }

    private fun Validatedata(name: String, number: String, state: String, city: String, street: String, pincode: String) {
        if(name.isEmpty() || number.isEmpty() || state.isEmpty() || city.isEmpty() || street.isEmpty() || pincode.isEmpty()){
            Toast.makeText(this, "Please Fill All the Details", Toast.LENGTH_SHORT).show()
        }else{
            storedata(state,city,street,pincode)
        }
    }

    private fun storedata(
        state: String,
        city: String,
        street: String,
        pincode: String
    ) {
        val data= hashMapOf<String,Any>()

        data["state"]=state
        data["city"]=city
        data["street"]=street
        data["pincode"]=pincode

        Firebase.firestore.collection("Users")
            .document(preference.getString("Number","")!!)
            .update(data)
            .addOnSuccessListener {

                val b=Bundle()
                b.putStringArrayList("Product_Ids",intent.getStringArrayListExtra("Product_Ids"))
                b.putString("Total_Cost",Total_Cost)
                val intent:Intent= Intent(this,CheckoutActivity::class.java)

                intent.putExtras(b)
                startActivity(intent)
            }
            .addOnFailureListener {

                Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadinfo() {

        Firebase.firestore.collection("Users")
            .document(preference.getString("Number","")!!)
            .get()
            .addOnSuccessListener {

                binding.UserName.setText(it.getString("username"))
                binding.UserNumber.setText(it.getString("usernumber"))
                binding.state.setText(it.getString("state"))
                binding.city.setText(it.getString("city"))
                binding.street.setText(it.getString("street"))
                binding.pincode.setText(it.getString("pincode"))
            }
            .addOnFailureListener {

            }
    }

}