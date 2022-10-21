package com.example.petlocale_final

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.adapter.VeterinariasCercanasAdapter
import com.google.firebase.firestore.*

class UsuarioMainVeterinariasCercanas : AppCompatActivity() {

    private lateinit var tempArrayList : ArrayList<VeterinariaCercana>
    private lateinit var recyclerView: RecyclerView
    private lateinit var veterinariaArrayList: ArrayList<VeterinariaCercana>
    private lateinit var myAdapter: VeterinariasCercanasAdapter

    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    private lateinit var ubicacionesArrayList: ArrayList<UbicacionVeterinaria>
    private lateinit var ubicacionesArrayList2: ArrayList<UbicacionVeterinaria>

    private lateinit var distanciasArrayList : ArrayList<Double>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario_main_veterinarias_cercanas)


        recyclerView = findViewById(R.id.recyclerViewVeterinariasCercanas)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        //Listas para el recycler
        veterinariaArrayList = arrayListOf()
        tempArrayList = arrayListOf()


        //Listas para hallar la distancia
        distanciasArrayList = arrayListOf()

        ubicacionesArrayList = arrayListOf()
        ubicacionesArrayList2 = arrayListOf()

        myAdapter = VeterinariasCercanasAdapter(tempArrayList)

        recyclerView.adapter = myAdapter

        EventChangeListener()
    }

    private fun EventChangeListener() {
        val objetoIntent: Intent = intent

        var latitud_usuario = objetoIntent.getStringExtra("latitud_usuario")

        var longitud_usuario = objetoIntent.getStringExtra("longitud_usuario")

        var email = objetoIntent.getStringExtra("email")

        db.collection("usuarios")
            .document(email.toString())
            .collection("ubicaciones")
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
                            veterinariaArrayList.add(dc.document.toObject(VeterinariaCercana::class.java))
                        }
                    }
                    tempArrayList.addAll(veterinariaArrayList)
                    myAdapter.notifyDataSetChanged()
                }
            })
    }
}

