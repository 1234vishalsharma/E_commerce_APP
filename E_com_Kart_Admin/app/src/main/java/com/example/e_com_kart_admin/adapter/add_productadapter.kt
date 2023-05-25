package com.example.e_com_kart_admin.adapter

import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.StructuredName
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.e_com_kart_admin.databinding.ImageItemBinding

class add_productadapter(val list:ArrayList<Uri>) :
    RecyclerView.Adapter<add_productadapter.AddProductImageViewHolder>() {
    inner class AddProductImageViewHolder(val binding:ImageItemBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddProductImageViewHolder {
        val binding=ImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AddProductImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AddProductImageViewHolder, position: Int) {
        holder.binding.itemImg.setImageURI(list[position])
    }
}