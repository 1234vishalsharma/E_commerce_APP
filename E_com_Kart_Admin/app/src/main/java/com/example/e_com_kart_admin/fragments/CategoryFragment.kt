package com.example.e_com_kart_admin.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.e_com_kart_admin.Model.CategoryModel
import com.example.e_com_kart_admin.R
import com.example.e_com_kart_admin.adapter.CategoryAdapter
import com.example.e_com_kart_admin.databinding.FragmentCategoryBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

class CategoryFragment : Fragment() {
    // initializing binding variable
    private lateinit var binding: FragmentCategoryBinding
    private var imageuri: Uri?=null
    // now initializing a dialog for progress bar
    private lateinit var dialog: Dialog

    // initializing the launch galary activity to open galary from register for aactivity result method
    private var launchgalaryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode== Activity.RESULT_OK){
            imageuri=it.data!!.data
            binding.imgprev.setImageURI(imageuri)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // dialog code
        dialog=Dialog(requireContext())
        dialog.setContentView(R.layout.progress_bar)
        dialog.setCancelable(false)


        // Inflate the layout for this fragment
        binding= FragmentCategoryBinding.inflate(layoutInflater)

        getdata()

        binding.apply{
            imgprev.setOnClickListener(View.OnClickListener {
                val intent: Intent = Intent("android.intent.action.GET_CONTENT")
                intent.type="image/*"
                launchgalaryActivity.launch(intent)
            })
            button6.setOnClickListener(View.OnClickListener {
                validatedata(binding.categoryname.text.toString())
            })
        }

        return binding.root
    }

    private fun getdata() {
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

    private fun validatedata(category_name: String) {
        if(category_name.isEmpty()){
            Toast.makeText(requireContext(), "Enter Category name first", Toast.LENGTH_SHORT).show()
        }
        else if(imageuri == null){
            Toast.makeText(requireContext(), "Please Select image first", Toast.LENGTH_SHORT).show()
        }
        else{
            uploadImage(category_name)
        }
    }

    private fun uploadImage(category_name: String) {
        dialog.show()

        val filename = UUID.randomUUID().toString() + ".jpg"
        val refstorage= FirebaseStorage.getInstance().reference.child("category/$filename")
        refstorage.putFile(imageuri!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {image->
                    storeData(category_name , image.toString())
                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong with storage", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(categoryName: String, image: String) {
        val db= Firebase.firestore
        val data = hashMapOf<String,Any>(
            "categ" to categoryName,
            "img" to image
        )
        db.collection("categories").add(data)
            .addOnSuccessListener {
                dialog.dismiss()
                binding.imgprev.setImageDrawable(resources.getDrawable(R.drawable.preview))
                binding.categoryname.text = null
                getdata()
                Toast.makeText(requireContext(), "Category Added Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went Wrong", Toast.LENGTH_SHORT).show()
            }
    }
}
