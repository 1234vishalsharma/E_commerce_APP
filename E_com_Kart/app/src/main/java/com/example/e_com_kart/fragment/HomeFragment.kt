package com.example.e_com_kart.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.e_com_kart.Adapter.CategoryAdapter
import com.example.e_com_kart.Adapter.ProductAdapter
import com.example.e_com_kart.Model.CategoryModel
import com.example.e_com_kart.Model.addproduct_model
import com.example.e_com_kart.R
import com.example.e_com_kart.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(layoutInflater)

        val preference=requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        if(preference.getBoolean("isCart",false)){
            findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
        }

        getCategories()
        getSliderimg()
        getproducts()

        return binding.root
    }

    private fun getSliderimg() {
        Firebase.firestore.collection("Slider").document("Item").get()
            .addOnSuccessListener {
                Glide.with(requireContext()).load(it.get("img")).into(binding.sliderimage)
            }
            .addOnFailureListener{
                binding.sliderimage.setImageDrawable(resources.getDrawable(R.drawable.mainframe))
            }
    }

    private fun getproducts() {
        val list=ArrayList<addproduct_model>()
        Firebase.firestore.collection("Products").get()
            .addOnSuccessListener {
                list.clear()
                for(doc in it.documents){
                    val data=doc.toObject(addproduct_model::class.java)
                    list.add(data!!)
                }
                binding.productrecycler.adapter=ProductAdapter(requireContext(),list)
            }
    }

    private fun getCategories() {
        val list=ArrayList<CategoryModel>()
        Firebase.firestore.collection("categories").get()
            .addOnSuccessListener {
                list.clear()
                for(doc in it.documents){
                    val data=doc.toObject(CategoryModel::class.java)
                    list.add(data!!)
                }
                binding.categoryrecycler.adapter=CategoryAdapter(requireContext(),list)
            }

    }

}