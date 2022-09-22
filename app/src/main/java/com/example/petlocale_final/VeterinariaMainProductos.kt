package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.adapter.ProductosAdapter
import com.example.petlocale_final.databinding.ActivityVeterinariaMainProductosBinding
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_veterinaria_main_productos.*
import kotlinx.android.synthetic.main.activity_veterinaria_main_servicios.*

class VeterinariaMainProductos : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productoArrayList: ArrayList<Productos>
    private lateinit var myAdapter: ProductosAdapter
    private lateinit var db: FirebaseFirestore

    private lateinit var Nombre2 : String

    private lateinit var binding : ActivityVeterinariaMainProductosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinaria_main_productos)

        //Se obtiene el nit
        val objetoIntent: Intent = intent

        var Nombre = objetoIntent.getStringExtra("Nombre")

        Nombre2 = Nombre.toString()

        recyclerView = findViewById(R.id.recyclerViewProduct)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        productoArrayList = arrayListOf()

        myAdapter = ProductosAdapter(productoArrayList)

        recyclerView.adapter = myAdapter



        EventChangeListener()



        addProductButton.setOnClickListener {
            startActivity(Intent(this, AddProduct::class.java).putExtra("nit",  Nombre))
        }

        deleteProductButton.setOnClickListener{
            startActivity(Intent(this, DeleteProduct::class.java).putExtra("nit",  Nombre))
        }
        
    }

    private fun EventChangeListener() {

        db = FirebaseFirestore.getInstance()

        db.collection("veterinarias").document(Nombre2).collection("productos")
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