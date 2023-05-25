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
import com.example.e_com_kart.databinding.OrderitemactivityBinding

class Orderitemadapter(val context: Context, val list:ArrayList<addproduct_model>):
    Adapter<Orderitemadapter.OrderitemViewHolder>() {
    inner class OrderitemViewHolder(val binding:OrderitemactivityBinding):
        ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderitemViewHolder {
        val binding=OrderitemactivityBinding.inflate(LayoutInflater.from(context),parent,false)
        return OrderitemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: OrderitemViewHolder, position: Int) {
        Glide.with(context).load(list[position].product_cover_img).into(holder.binding.imageView2)
        holder.binding.textView10.text=list[position].product_name
        holder.binding.textView9.text=list[position].product_SP

        holder.binding.imageView4.setOnClickListener {
            Toast.makeText(context, "unable to clear the product", Toast.LENGTH_SHORT).show()
        }
        
        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent: Intent = Intent(context, ProductActivity::class.java)
            intent.putExtra("id",list[position].product_ID)
            context.startActivity(intent)
        })

    }


}