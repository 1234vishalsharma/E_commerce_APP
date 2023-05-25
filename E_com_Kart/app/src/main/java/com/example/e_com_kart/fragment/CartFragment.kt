package com.example.e_com_kart.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.e_com_kart.Adapter.CartItemAdapter
import com.example.e_com_kart.R
import com.example.e_com_kart.activity.AddressActivity
import com.example.e_com_kart.databinding.FragmentCartBinding
import com.example.e_com_kart.roomdb.AppDatabase
import com.example.e_com_kart.roomdb.ProductModel
import com.example.e_com_kart.roomdb.productDao

class CartFragment : Fragment() {

    private lateinit var binding:FragmentCartBinding
    private lateinit var list:ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentCartBinding.inflate(layoutInflater)

        val preference=requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor=preference.edit()
        editor.putBoolean("isCart",false)
        editor.apply()

        val dao=AppDatabase.getInstance(requireContext()).ProductDao()
        list=ArrayList()
        dao.getAllProduct().observe(requireActivity()){
            binding.cartItemRecycler.adapter=CartItemAdapter(requireContext(), it)
            list.clear()
            for(data in it){
                list.add(data.product_ID)
            }

            TotalCost(it)
        }



        return binding.root
    }

    private fun TotalCost(data: List<ProductModel>?) {
        var totalcost=0
        for(item in data!!){
            totalcost+= item.product_SP!!.toInt()
        }
        binding.textview11.text="Total Products in Cart is: ${data.size}"
        binding.textview12.text="Total Cost of Items : $totalcost"

        binding.checkout.setOnClickListener {
            val intent:Intent= Intent(requireContext(),AddressActivity::class.java)
            val b=Bundle()
            b.putStringArrayList("Product_Ids",list)
            b.putString("Total_Cost",totalcost.toString())
            intent.putExtras(b)
            startActivity(intent)
        }
    }

}


