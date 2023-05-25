package com.example.e_com_kart.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.e_com_kart.Model.addproduct_model
import com.example.e_com_kart.activity.ProductActivity
import com.example.e_com_kart.databinding.ItemCategoryLayoutBinding

class CategoryItemAdapter(val context: Context, val list:ArrayList<addproduct_model> )
    :Adapter<CategoryItemAdapter.CategoryItemViewHolder>() {
    inner class CategoryItemViewHolder(val binding:ItemCategoryLayoutBinding):
        ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
       val binding=ItemCategoryLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        Glide.with(context).load(list[position].product_cover_img).into(holder.binding.imageView5)
        holder.binding.textView3.text=list[position].product_name
        holder.binding.textView4.text=list[position].product_SP

        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent: Intent =Intent(context,ProductActivity::class.java)
            intent.putExtra("id",list[position].product_ID)
            context.startActivity(intent)

        })
    }
}