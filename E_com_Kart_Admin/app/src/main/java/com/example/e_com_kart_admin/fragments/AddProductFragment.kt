package com.example.e_com_kart_admin.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.e_com_kart_admin.Model.CategoryModel
import com.example.e_com_kart_admin.Model.addproduct_model
import com.example.e_com_kart_admin.R
import com.example.e_com_kart_admin.adapter.add_productadapter
import com.example.e_com_kart_admin.databinding.FragmentAddProductBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

class AddProductFragment : Fragment() {

    // initializing binding for add product activity
    private lateinit var binding:FragmentAddProductBinding
    private lateinit var list:ArrayList<Uri>
    private lateinit var listImages:ArrayList<String>
    private lateinit var adapter:add_productadapter
    private var coverimage:Uri?=null
    private lateinit var dialog:Dialog
    private var coverimageurl:String?=""
    private lateinit var categorylist:ArrayList<String>


    // open gallery launcher activity for cover image
    private var launchgalaryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode== Activity.RESULT_OK){
            coverimage=it.data!!.data
            binding.productCoverImg.setImageURI(coverimage)
            binding.productCoverImg.visibility = VISIBLE
        }
    }

    // open gallery launcher activity for product image
    private var launchproductActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode== Activity.RESULT_OK){
            val imageurl=it.data!!.data
            list.add(imageurl!!)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAddProductBinding.inflate(layoutInflater)

        list= ArrayList()
        listImages=ArrayList()

        dialog= Dialog(requireContext())
        dialog.setContentView(R.layout.progress_bar)
        dialog.setCancelable(false)

        binding.selectCoverBtn.setOnClickListener(View.OnClickListener {
            val intent:Intent=Intent("android.intent.action.GET_CONTENT")
            intent.type="image/*"
            launchgalaryActivity.launch(intent)
        })
        binding.productCoverBtn.setOnClickListener(View.OnClickListener {
            val intent:Intent=Intent("android.intent.action.GET_CONTENT")
            intent.type="image/*"
            launchproductActivity.launch(intent)
        })

        setProductCategory()

        adapter= add_productadapter(list)
        binding.productImgRecyclerView.adapter=adapter


        binding.submitProductBtn.setOnClickListener(View.OnClickListener {
            validatedata()
        })

        return binding.root
    }

    private fun validatedata() {
        if(binding.productNameEdt.text.toString().isEmpty()){
            Toast.makeText(requireContext(), "Enter name first", Toast.LENGTH_SHORT).show()
            binding.productNameEdt.setError("Required")
        }else if(binding.productDescEdt.text.toString().isEmpty()){
            Toast.makeText(requireContext(), "Enter Description", Toast.LENGTH_SHORT).show()
            binding.productDescEdt.setError("Required")
        }else if(binding.productMRPEdt.text.toString().isEmpty()){
            Toast.makeText(requireContext(), "Enter Product MRP", Toast.LENGTH_SHORT).show()
            binding.productMRPEdt.setError("Required")
        }else if(binding.productSPEdt.text.toString().isEmpty()){
            Toast.makeText(requireContext(), "Enter Product Selling Price", Toast.LENGTH_SHORT).show()
            binding.productSPEdt.setError("Required")
        }else if(coverimage==null){
            Toast.makeText(requireContext(), "Please select cover image", Toast.LENGTH_SHORT).show()
        }else if(list.size<1){
            Toast.makeText(requireContext(), "Please select Product image", Toast.LENGTH_SHORT).show()
        }else{
            uploadimage()
        }
    }

    private fun uploadimage() {
        dialog.show()

        val filename = UUID.randomUUID().toString() + ".jpg"
        val refstorage= FirebaseStorage.getInstance().reference.child("Products/$filename")
        refstorage.putFile(coverimage!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {image->
                    coverimageurl=image.toString()
                    uploadproductimage()
                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong with storage", Toast.LENGTH_SHORT).show()
            }
    }
    private var i=0
    private fun uploadproductimage() {
        dialog.show()

        val filename = UUID.randomUUID().toString() + ".jpg"
        val refstorage= FirebaseStorage.getInstance().reference.child("Products/$filename")
        refstorage.putFile(list[i])
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {image->
                    listImages.add(image.toString())
                    if(list.size==listImages.size){
                        storedata()
                    }else{
                        i+=1
                        uploadproductimage()
                    }
                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong with storage", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storedata() {
        val db=Firebase.firestore.collection("Products")
        val key=db.document().id
        val data=addproduct_model(
            binding.productNameEdt.text.toString(),
            binding.productDescEdt.text.toString(),
            coverimageurl.toString(),
            categorylist[binding.productCategoryDropdown.selectedItemPosition],
            key,
            binding.productMRPEdt.text.toString(),
            binding.productSPEdt.text.toString(),
            listImages
        )
        db.document(key).set(data).addOnSuccessListener {
            dialog.dismiss()
            Toast.makeText(requireContext(), "Product added Successfully", Toast.LENGTH_SHORT).show()
            binding.productNameEdt.text=null
            binding.productDescEdt.text=null
            binding.productMRPEdt.text=null
            binding.productSPEdt.text=null
        }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
    }


    private fun setProductCategory(){
        categorylist=ArrayList()
        Firebase.firestore.collection("categories").get().addOnSuccessListener {
            categorylist.clear()
            for(doc in it.documents){
                val data=doc.toObject(CategoryModel::class.java)
                categorylist.add(data!!.categ!!)
            }
            categorylist.add(0,"Select Category")

            val arrayAdapter=ArrayAdapter(requireContext(),R.layout.dropdown_item,categorylist)
            binding.productCategoryDropdown.adapter=arrayAdapter
        }
    }
}