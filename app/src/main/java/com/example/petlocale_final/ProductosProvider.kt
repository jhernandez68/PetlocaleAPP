package com.example.petlocale_final

import com.google.firebase.firestore.FirebaseFirestore
//Instancia de la DB
private val db = FirebaseFirestore.getInstance()

class ProductosProvider {

    companion object{
        val productosList = listOf<Productos>(
            Productos("hola", "xd", "https://cursokotlin.com/wp-content/uploads/2017/07/spiderman.jpg")
        )
    }
}