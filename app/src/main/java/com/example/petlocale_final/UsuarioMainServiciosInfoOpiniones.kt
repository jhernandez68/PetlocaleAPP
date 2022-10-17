package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.adapter.OpinionesAdapter
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_usuario_main_servicios_info_opiniones.*

class UsuarioMainServiciosInfoOpiniones : AppCompatActivity() {
    //Variables para hacer el recyclerview
    private lateinit var tempArrayList : ArrayList<Opinion>
    private lateinit var recyclerView: RecyclerView
    private lateinit var opinionArrayList: ArrayList<Opinion>
    private lateinit var myAdapter: OpinionesAdapter
    private lateinit var db: FirebaseFirestore


    //Variable para guardar el correo de usuario
    private lateinit var correo : String
    private lateinit var nombreservicio : String
    private lateinit var nombreveterinaria : String
    private lateinit var nitfb : String
    private lateinit var tamaño : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario_main_servicios_info_opiniones)

        //Se trae los datos de la veterinaria
        val objetoIntent: Intent = intent

        var nombre_servicio = objetoIntent.getStringExtra("nombre_servicio")

        var nombre_veterinaria = objetoIntent.getStringExtra("nombre_veterinaria")

        var nit_servicio = objetoIntent.getStringExtra("nit")

        var email = objetoIntent.getStringExtra("email")


        //Se asignan para usar globalmente.

        correo = email.toString()

        nombreveterinaria = nombre_veterinaria.toString()

        nitfb = nit_servicio.toString()

        nombreservicio = nombre_servicio.toString()


        //Lógica del recylclerview

        recyclerView = findViewById(R.id.recyclerViewOpinionesServicio)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        opinionArrayList = arrayListOf()
        tempArrayList = arrayListOf()

        myAdapter = OpinionesAdapter(tempArrayList)

        recyclerView.adapter = myAdapter

        EventChangeListener()

        main_name_servicio_detailed_info2_opiniones.setText(nombre_servicio.toString())

        main_name_vet_servicio_detailed_info2_opiniones.setText(nombre_veterinaria.toString())


    }

    private fun EventChangeListener() {


        db = FirebaseFirestore.getInstance()

        db.collection("veterinarias")
            .document(nitfb.toString())
            .collection("servicios")
            .document(nombreservicio.toString())
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
                        cantidad_opiniones_servicio2.setText(tempArrayList.size.toString())

                        var contador : Int = 0

                        var sumatoria : Float = 0F

                        var total : Float = 0F

                        while (contador <opinionArrayList.size) {
                            sumatoria = opinionArrayList[contador].calificacion!!.toFloat() + sumatoria
                            contador = contador + 1
                        }

                        total = sumatoria / opinionArrayList.size

                        main_precio_servicio_detailed_info2_opiniones.setText(total.toString())
                    }

                    myAdapter.notifyDataSetChanged()
                }
            })
    }
}