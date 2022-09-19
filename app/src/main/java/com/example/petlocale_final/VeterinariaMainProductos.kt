package com.example.petlocale_final

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.adapter.ProductosAdapter
import com.google.firebase.firestore.FirebaseFirestore

class VeterinariaMainProductos : AppCompatActivity() {

    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    //Lista de productos
    val xd = db.collection("productos_test").get()


    val productosList = listOf<Productos>(
        Productos("hola", "xd", "https://cursokotlin.com/wp-content/uploads/2017/07/spiderman.jpg")
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinaria_main_productos)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ProductosAdapter(productosList)

    }
}