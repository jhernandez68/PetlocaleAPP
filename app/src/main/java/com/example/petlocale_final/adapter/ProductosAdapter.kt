package com.example.petlocale_final.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.Productos
import com.example.petlocale_final.R

class ProductosAdapter (private val productosList : ArrayList<Productos>): RecyclerView.Adapter<ProductosAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductosAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return MyViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return productosList.size
    }

    public class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val nombre : TextView = itemView.findViewById(R.id.nombreDetailed2Product)
        val tipo : TextView = itemView.findViewById(R.id.nitDetailed2Product)
        val precio: TextView = itemView.findViewById(R.id.yearsDetailed2Product)
        val descripcion: TextView = itemView.findViewById(R.id.emailDetailed2Product)
        val categoria: TextView = itemView.findViewById(R.id.categoriaDetailedProduct2)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val producto : Productos = productosList[position]
        holder.nombre.text =producto.nombre
        holder.tipo.text = producto.tipo
        holder.precio.text = producto.precio
        holder.descripcion.text = producto.descripcion
        holder.categoria.text = producto.categoria
    }
}