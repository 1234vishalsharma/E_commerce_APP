package com.example.e_com_kart_admin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.e_com_kart_admin.R
import com.example.e_com_kart_admin.databinding.FragmentProductBinding


class ProductFragment : Fragment() {

    // initialiing the binding variable
    private lateinit var binding:FragmentProductBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding=FragmentProductBinding.inflate(layoutInflater)

        binding.floatingActionButton.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_productFragment_to_addProductFragment2)
        })

        return binding.root
    }

}