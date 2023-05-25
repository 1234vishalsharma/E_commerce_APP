package com.example.e_com_kart.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.e_com_kart.Model.addproduct_model
import com.example.e_com_kart.activity.ProductActivity
import com.example.e_com_kart.databinding.ProductLayoutBinding

class ProductAdapter(val context: Context,val list:ArrayList<addproduct_model> ):
    Adapter<ProductAdapter.ProductViewHolder>() {
    inner class ProductViewHolder(val binding: ProductLayoutBinding):
            ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding=ProductLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        Glide.with(context).load(list[position].product_cover_img).into(holder.binding.imageView3)
        holder.binding.textView1.text=list[position].product_desc
        holder.binding.textView.text=list[position].product_name


        // set the selling price of the product on the button
        holder.binding.button.text=list[position].product_SP
        holder.binding.button2.text="Add to Cart"

        holder.binding.button.setOnClickListener {
            Toast.makeText(context, "Tap on Add to Cart to buy this product", Toast.LENGTH_SHORT).show()
        }
        
        holder.binding.button2.setOnClickListener(View.OnClickListener {
            val intent: Intent = Intent(context, ProductActivity::class.java)
            intent.putExtra("id",list[position].product_ID)
            context.startActivity(intent)

        })

        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent: Intent = Intent(context, ProductActivity::class.java)
            intent.putExtra("id",list[position].product_ID)
            context.startActivity(intent)

        })
    }
}