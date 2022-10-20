package com.example.petlocale_final

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.petlocale_final.databinding.ActivityAddProductBinding
import com.example.petlocale_final.databinding.ActivityAddProductImageBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_product_image.*
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class AddProductImage : AppCompatActivity() {

    lateinit var binding: ActivityAddProductImageBinding
    lateinit var ImageUri : Uri

    lateinit var nombre_productoXD : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Se trae el nombre de la veterinaria
        val objetoIntent: Intent = intent

        var Nombre = objetoIntent.getStringExtra("Nombre_Producto")

        nombre_productoXD = Nombre.toString()


        binding = ActivityAddProductImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectImageButton.setOnClickListener {
            checkPermission()
        }

        binding.uploadImageButton.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Subiendo imagen...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        var formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()

        val storageReference = FirebaseStorage.getInstance().getReference("images/$nombre_productoXD.jpg")

        storageReference.putFile(ImageUri).addOnSuccessListener {
            binding.imageView2.setImageURI(null)
            if (progressDialog.isShowing) progressDialog.dismiss()
        }.addOnFailureListener{
            if (progressDialog.isShowing) progressDialog.dismiss()

        }
    }

    private fun selectImage(){
        val intent = Intent()
        intent.type = "images/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == RESULT_OK){
            ImageUri = data?.data!!
            binding.imageView2.setImageURI(ImageUri)
        }
    }

    private fun checkPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestReadPermission()
        }else{
            selectImage()
        }
    }

    private fun requestReadPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){

        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 777)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 777){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }else{

            }
        }
    }


}