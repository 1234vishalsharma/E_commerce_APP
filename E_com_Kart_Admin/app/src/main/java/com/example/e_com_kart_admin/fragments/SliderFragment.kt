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
import com.example.e_com_kart_admin.R
import com.example.e_com_kart_admin.databinding.FragmentSliderBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SliderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SliderFragment : Fragment() {

    // initializing binding variable
    private lateinit var binding: FragmentSliderBinding
    // INITIALIZING A variable for storing the image uri in a string variable\
    private var imageuri:Uri?=null
    // now initializing a dialog for progress bar
    private lateinit var dialog:Dialog

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
        // Inflate the layout for this fragment
        binding=FragmentSliderBinding.inflate(layoutInflater)

        dialog=Dialog(requireContext())
        dialog.setContentView(R.layout.progress_bar)
        dialog.setCancelable(false)
        
        binding.imgprev.setOnClickListener(View.OnClickListener {
            val intent:Intent=Intent("android.intent.action.GET_CONTENT")
            intent.type="image/*"
            launchgalaryActivity.launch(intent)
        })
        binding.button5.setOnClickListener(View.OnClickListener {
            if(imageuri != null){
                UploadImage(imageuri!!)
            }else{
                Toast.makeText(requireContext(), "Please select an image first", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }
    private fun UploadImage(imageurl: Uri) {
        dialog.show()

        val filename = UUID.randomUUID().toString() + ".jpg"
        val refstorage=FirebaseStorage.getInstance().reference.child("Slider/$filename")
        refstorage.putFile(imageurl)
            .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {image->
                storeData(image.toString())
            }
        }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong with storage", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(image: String) {
        val db= Firebase.firestore
        val data = hashMapOf<String,Any>(
            "img" to image
        )
        db.collection("Slider").document("Item").set(data)
            .addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Slider Uploaded Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went Wrong", Toast.LENGTH_SHORT).show()
            }
    }
}


