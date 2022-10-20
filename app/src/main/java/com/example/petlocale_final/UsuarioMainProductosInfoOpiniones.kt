package com.example.petlocale_final

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.adapter.OpinionesAdapter
import com.example.petlocale_final.databinding.ActivityUsuarioMainInfoBinding
import com.example.petlocale_final.databinding.ActivityUsuarioMainProductosInfoOpinionesBinding
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_usuario_main_productos_info.*
import kotlinx.android.synthetic.main.activity_usuario_main_productos_info_opiniones.*
import java.io.File
import kotlin.collections.ArrayList

class UsuarioMainProductosInfoOpiniones : AppCompatActivity() {

    //Variables para hacer el recyclerview
    private lateinit var tempArrayList : ArrayList<Opinion>
    private lateinit var recyclerView: RecyclerView
    private lateinit var opinionArrayList: ArrayList<Opinion>
    private lateinit var myAdapter: OpinionesAdapter
    private lateinit var db: FirebaseFirestore


    //Variable para guardar el correo de usuario
    private lateinit var correo : String
    private lateinit var nombreproducto : String
    private lateinit var nombreveterinaria : String
    private lateinit var nitfb : String
    private lateinit var tamaño : String


    lateinit var binding : ActivityUsuarioMainProductosInfoOpinionesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuarioMainProductosInfoOpinionesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Se trae los datos de la veterinaria
        val objetoIntent: Intent = intent

        var nombre_producto = objetoIntent.getStringExtra("nombre_producto")

        var nombre_veterinaria = objetoIntent.getStringExtra("nombre_veterinaria")

        var nit_product = objetoIntent.getStringExtra("nit")

        var email = objetoIntent.getStringExtra("email")

        //Se asignan para usar globalmente.

        correo = email.toString()

        nombreveterinaria = nombre_veterinaria.toString()

        nitfb = nit_product.toString()
        nombreproducto = nombre_producto.toString()

        //Lógica del recylclerview

        recyclerView = findViewById(R.id.recyclerViewOpiniones)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        opinionArrayList = arrayListOf()
        tempArrayList = arrayListOf()

        myAdapter = OpinionesAdapter(tempArrayList)

        recyclerView.adapter = myAdapter

        EventChangeListener()

        main_name_product_detailed_info2_opiniones.setText(nombre_producto.toString())

        main_name_vet_product_detailed_info2_opiniones.setText(nombre_veterinaria.toString())

        val storageRef = FirebaseStorage.getInstance().reference.child("images/${nit_product}/${nombre_producto}.jpg")
        val localfile = File.createTempFile("tempImage", "jpg")


        storageRef.getFile(localfile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.mainProductDetailedOpiniones.setImageBitmap(bitmap)

        }


    }

    private fun EventChangeListener() {


        db = FirebaseFirestore.getInstance()

        db.collection("veterinarias")
            .document(nitfb.toString())
            .collection("productos")
            .document(nombreproducto.toString())
            .collection("reseñas")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if(error != null){
                        Log.e("Firestore error", error.message.toString())
                        return
                    }
                    for (dc : DocumentChange in value?.documentChanges!!){
                        if(dc.type == DocumentChange.Type.ADDED){
                            opinionArrayList.add(dc.document.toObject(Opinion::class.java))
                        }
                    }
                    tempArrayList.addAll(opinionArrayList)

                    if(opinionArrayList.size > 0){
                        cantidad_opiniones_producto2.setText(tempArrayList.size.toString())

                        var contador : Int = 0

                        var sumatoria : Float = 0F

                        var total : Float = 0F

                        while (contador <opinionArrayList.size) {
                            sumatoria = opinionArrayList[contador].calificacion!!.toFloat() + sumatoria
                            contador = contador + 1
                        }

                        total = sumatoria / opinionArrayList.size

                        main_precio_product_detailed_info2_opiniones.setText(total.toString())
                    }

                    myAdapter.notifyDataSetChanged()
                }
            })
    }

}