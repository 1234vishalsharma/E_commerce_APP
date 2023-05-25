package com.example.e_com_kart.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.e_com_kart.MainActivity
import com.example.e_com_kart.R
import com.example.e_com_kart.databinding.ActivityLoginBinding
import com.example.e_com_kart.databinding.ActivityOtpactivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button3.setOnClickListener {
            if(binding.inputotp.text.toString().isEmpty()){
                binding.inputotp.setError("REQUIRED FIELD")
            }
            else{
                verifyUser(binding.inputotp.text.toString())
            }
        }
    }

    private fun verifyUser(OTP: String) {
        val credential = PhoneAuthProvider.getCredential(intent.getStringExtra("verificationId")!!, OTP)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val preference=getSharedPreferences("user", MODE_PRIVATE)
                    val editor=preference.edit()

                    editor.putString("Number",intent.getStringExtra("Number")!!)
                    editor.apply()

                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                    val user = task.result?.user
                } else {
                    Toast.makeText(this, "Wrong OTP", Toast.LENGTH_SHORT).show()
                }
            }
    }
}