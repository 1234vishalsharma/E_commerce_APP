package com.example.e_com_kart.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.e_com_kart.Model.UserModel
import com.example.e_com_kart.R
import com.example.e_com_kart.databinding.ActivityRegisterBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Launching the login activty on clicking the login button
        binding.button4.setOnClickListener {
            openLogin()
        }

        // checking all details on clicking the signup button
        binding.button3.setOnClickListener {
            if(binding.usernumber.text.toString().isEmpty()){
                binding.usernumber.setError("REQUIRED FIELD")
            }else if(binding.username.text.toString().isEmpty()){
                binding.username.setError("REQUIRED FIELD")
            }else{
                storedata()
            }
        }

    }

    private fun storedata() {
        val builder=AlertDialog.Builder(this)
            .setTitle("Loading...")
            .setMessage("Please wait")
            .setCancelable(false)
            .create()

        builder.show()

        val preference=getSharedPreferences("user", MODE_PRIVATE)
        val editor=preference.edit()

        editor.putString("Number",binding.usernumber.text.toString())
        editor.putString("Name",binding.username.text.toString())
        editor.apply()

        val pre=getSharedPreferences("currusernumber", MODE_PRIVATE)
        val edt=pre.edit()
        edt.putString("Number",binding.usernumber.text.toString())
        edt.apply()

        val data=UserModel(username=binding.username.text.toString(), usernumber = binding.usernumber.text.toString())

        Firebase.firestore.collection("Users").document(binding.usernumber.text.toString())
            .set(data).addOnSuccessListener {
                builder.dismiss()
                Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show()
                openLogin()
            }.addOnFailureListener {
                builder.dismiss()
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openLogin() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
}