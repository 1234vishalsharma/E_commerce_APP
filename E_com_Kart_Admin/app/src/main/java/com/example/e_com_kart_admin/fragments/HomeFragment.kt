package com.example.e_com_kart_admin.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.e_com_kart_admin.R
import com.example.e_com_kart_admin.activity.AllOrdersActivity
import com.example.e_com_kart_admin.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(layoutInflater)

        binding.button!!.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment2_to_categoryFragment2)
        }
        binding.button1!!.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment2_to_productFragment)
        }
        binding.button2!!.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment2_to_sliderFragment)
        }
        binding.button3!!.setOnClickListener{
            startActivity(Intent(requireContext(),AllOrdersActivity::class.java))
        }


        return binding.root
    }

}