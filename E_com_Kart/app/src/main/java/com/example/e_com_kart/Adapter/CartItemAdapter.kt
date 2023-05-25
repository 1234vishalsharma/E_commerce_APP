package com.example.e_com_kart.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.e_com_kart.activity.ProductActivity
import com.example.e_com_kart.databinding.CartItemLayoutBinding
import com.example.e_com_kart.fragment.CartFragment
import com.example.e_com_kart.roomdb.AppDatabase
import com.example.e_com_kart.roomdb.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartItemAdapter (val context: Context ,val list: List<ProductModel>) :
    Adapter<CartItemAdapter.CartItemViewHolder>() {

    inner class CartItemViewHolder(val binding:CartItemLayoutBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding=CartItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return CartItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        Glide.with(context).load(list[position].product_Cover_Img).into(holder.binding.imageView2)
        holder.binding.textView10.text=list[position].product_name
        holder.binding.textView9.text=list[position].product_SP

        holder.itemView.setOnClickListener {
            val intent:Intent=Intent(context,ProductActivity::class.java)
            intent.putExtra("id",list[position].product_ID)
            context.startActivity(intent)
        }

        val dao=AppDatabase.getInstance(context).ProductDao()
        holder.binding.imageView4.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                dao.deleteProduct(
                    ProductModel(
                        list[position].product_ID,
                        list[position].product_name,
                        list[position].product_SP,
                        list[position].product_Cover_Img
                    )
                )
            }
            }
    }
}