package com.example.petlocale_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.adapter.ServiciosAdapter
import com.google.firebase.firestore.*

class UsuarioMainServicios : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var servicioArrayList: ArrayList<Servicio>
    private lateinit var myAdapter: ServiciosAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario_main_servicios)

        recyclerView = findViewById(R.id.recyclerViewUserService)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        servicioArrayList = arrayListOf()

        myAdapter = ServiciosAdapter(servicioArrayList)

        recyclerView.adapter = myAdapter

        EventChangeListener()

    }

    private fun EventChangeListener() {

        db = FirebaseFirestore.getInstance()

        db.collectionGroup("servicios")
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
                            servicioArrayList.add(dc.document.toObject(Servicio::class.java))
                        }
                    }

                    myAdapter.notifyDataSetChanged()
                }
            })
    }

}