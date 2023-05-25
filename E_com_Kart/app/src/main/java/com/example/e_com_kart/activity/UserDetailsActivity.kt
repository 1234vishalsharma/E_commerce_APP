package com.example.e_com_kart.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.e_com_kart.databinding.ActivityUserDetailsBinding
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.net.URI

class UserDetailsActivity : AppCompatActivity() {

    // initializing binding
    private lateinit var binding:ActivityUserDetailsBinding

    // initializing the iamge uri variable
    private var imageuri:Uri?=null
    // loading dialog
    private lateinit var dialog: Dialog


    // launchng the galary activity
    private var launchgalaryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode== Activity.RESULT_OK){
            imageuri=it.data!!.data
            binding.imageView7.setImageURI(imageuri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserDetailsBinding.inflate(layoutInflater)

//              opens gallery on on clicking button
        binding.uploadimg.setOnClickListener {
            if(imageuri!=null){
                uploadimage(imageuri!!)
            }
            else{
                Toast.makeText(this, "please select an image first", Toast.LENGTH_SHORT).show()
            }
        }

        //      opens gallery on on clicking image
        binding.imageView7.setOnClickListener {
            val intent:Intent= Intent("android.intent.action.GET_CONTENT")
            intent.type="image/*"
            launchgalaryActivity.launch(intent)
        }
//              Get all the details of the user like username, user-number and address of user.
        getdetails()
        setContentView(binding.root)

    }

    private fun uploadimage(img: Uri) {
        dialog= AlertDialog.Builder(this)
            .setTitle("Loading...")
            .setMessage("Please wait")
            .setCancelable(false)
            .create()

        dialog.show()
        val pre=getSharedPreferences("currusernumber", MODE_PRIVATE)
        val data= hashMapOf<String,Any>()
            data["profile_pic"] = img.toString()



        Firebase.firestore.collection("Users").document(pre.getString("Number","")!!)
            .set(data, SetOptions.merge())
            .addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(this, "Profile Pic Updated Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
    }

    @SuppressLint("SetTextI18n")
    private fun getdetails() {

        val pre=getSharedPreferences("currusernumber", MODE_PRIVATE)

        Firebase.firestore.collection("Users").document(pre.getString("Number","")!!).get()
            .addOnSuccessListener {
                val name=it.getString("username")
                val contactnum=it.getString("usernumber")

                val city=it.getString("city")
                val state=it.getString("state")
                val street=it.getString("street")
                val pinnum=it.getString("pincode")

                binding.greetuser.text = "Hello, $name"
                binding.usernumber.text = "Contact: +91-$contactnum"
                binding.address.text="Address: $street, $city, $state $pinnum"
                Glide.with(this).load(it.get("profile_pic")).into(binding.imageView7)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
    }
}


