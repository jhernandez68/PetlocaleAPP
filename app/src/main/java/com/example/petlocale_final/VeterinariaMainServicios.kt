package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.adapter.ServiciosAdapter
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_veterinaria_main_servicios.*

class VeterinariaMainServicios : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var servicioArrayList: ArrayList<Servicio>
    private lateinit var myAdapter: ServiciosAdapter
    private lateinit var db: FirebaseFirestore

    private lateinit var Nombre2 : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinaria_main_servicios)


        //Se obtiene el nit
        val objetoIntent: Intent = intent

        var Nombre = objetoIntent.getStringExtra("Nombre")

        Nombre2 = Nombre.toString()

        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        servicioArrayList = arrayListOf()

        myAdapter = ServiciosAdapter(servicioArrayList)

        recyclerView.adapter = myAdapter

        EventChangeListener()

        addServiceButton.setOnClickListener {
            startActivity(Intent(this, AddService::class.java).putExtra("nit",  Nombre))
        }

        deleteServiceButton.setOnClickListener{
            startActivity(Intent(this, DeleteService::class.java).putExtra("nit",  Nombre))
        }
    }

    private fun EventChangeListener() {

        db = FirebaseFirestore.getInstance()

        db.collection("veterinarias").document(Nombre2).collection("productos")
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
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
                        servicioArrayList.add(dc.document.toObject(Servicio::class.java))
                    }
                }

                myAdapter.notifyDataSetChanged()
            }
        })
    }
}