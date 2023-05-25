package com.example.e_com_kart_admin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.helper.widget.Carousel.Adapter
import androidx.constraintlayout.helper.widget.Carousel.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.e_com_kart_admin.Model.CategoryModel
import com.example.e_com_kart_admin.R
import com.example.e_com_kart_admin.databinding.FragmentCategoryBinding
import com.example.e_com_kart_admin.databinding.ItemCategoryBinding

class CategoryAdapter(var context : Context, val list:ArrayList<CategoryModel>):
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){
    inner class CategoryViewHolder(val binding:ItemCategoryBinding)
        :ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding=ItemCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.textView2.text=list[position].categ
        Glide.with(context).load(list[position].img).into(holder.binding.imageView)
    }
    override fun getItemCount(): Int {
        return list.size
    }


}