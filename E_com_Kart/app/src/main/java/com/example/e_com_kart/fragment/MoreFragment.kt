package com.example.e_com_kart.fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.e_com_kart.R
import com.example.e_com_kart.activity.LoginActivity
import com.example.e_com_kart.activity.OrderListActivity
import com.example.e_com_kart.activity.UserDetailsActivity
import com.example.e_com_kart.databinding.FragmentMoreBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MoreFragment : Fragment() {
    private lateinit var binding:FragmentMoreBinding
    private lateinit var pre: SharedPreferences
    private lateinit var auth:FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentMoreBinding.inflate(layoutInflater)

        auth=FirebaseAuth.getInstance()

//        loadinfo()
        binding.logoutbtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireContext(),LoginActivity::class.java))
        }

        binding.orderdetails.setOnClickListener {
            startActivity(Intent(requireContext(),OrderListActivity::class.java))
        }
        binding.userdetails.setOnClickListener {
            startActivity(Intent(requireContext(),UserDetailsActivity::class.java))
        }

        return binding.root
    }
}