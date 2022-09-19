package com.example.petlocale_final.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.Productos
import com.example.petlocale_final.R

class ProductosAdapter (private val productosList:List<Productos>) : RecyclerView.Adapter<ProductosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductosViewHolder(layoutInflater.inflate(R.layout.item_producto, parent, false))
    }

    override fun onBindViewHolder(holder: ProductosViewHolder, position: Int) {
        val item = productosList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = productosList.size

}