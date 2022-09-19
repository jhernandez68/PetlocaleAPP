package com.example.petlocale_final.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.Productos
import com.example.petlocale_final.R

class ProductosViewHolder(view: View):RecyclerView.ViewHolder(view){

    val nombre = view.findViewById<TextView>(R.id.tvProductoName)
    val tipo = view.findViewById<TextView>(R.id.tvPublisher)
    val photo = view.findViewById<ImageView>(R.id.ivProducto)

    fun render(productoModel : Productos){
        nombre.text = productoModel.nombre
        tipo.text = productoModel.tipo
    }
}