package com.example.petlocale_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.adapter.ProductosAdapter
import com.google.firebase.firestore.*

class UsuarioMainProductos : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productoArrayList: ArrayList<Productos>
    private lateinit var myAdapter: ProductosAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario_main_productos)

        recyclerView = findViewById(R.id.recyclerViewUserProduct)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        productoArrayList = arrayListOf()

        myAdapter = ProductosAdapter(productoArrayList)

        recyclerView.adapter = myAdapter

        EventChangeListener()
    }

    private fun EventChangeListener() {

        db = FirebaseFirestore.getInstance()

        db.collectionGroup("productos")
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
                            productoArrayList.add(dc.document.toObject(Productos::class.java))
                        }
                    }

                    myAdapter.notifyDataSetChanged()
                }
            })
    }
}