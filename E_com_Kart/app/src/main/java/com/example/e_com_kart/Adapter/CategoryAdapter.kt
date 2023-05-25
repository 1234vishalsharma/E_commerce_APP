package com.example.e_com_kart.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.e_com_kart.Model.CategoryModel
import com.example.e_com_kart.R
import com.example.e_com_kart.activity.CategoryActivity
import com.example.e_com_kart.databinding.CategoryLayoutBinding


class CategoryAdapter(val context : Context, val list:ArrayList<CategoryModel>)
    : androidx.recyclerview.widget.RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){
    inner class CategoryViewHolder(val binding: CategoryLayoutBinding):
        ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding=CategoryLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.textview.text=list[position].categ
        Glide.with(context).load(list[position].img).into(holder.binding.imageView)

        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent:Intent= Intent(context,CategoryActivity::class.java)
            intent.putExtra("categ",list[position].categ)
            context.startActivity(intent)

        })
    }
    override fun getItemCount(): Int {
        return list.size
    }


}