package com.example.petlocale_final.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.Productos
import com.example.petlocale_final.R

class ProductosAdapter (private val productosList : ArrayList<Productos>): RecyclerView.Adapter<ProductosAdapter.MyViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnClickItemListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductosAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return MyViewHolder(itemView,mListener)
    }


    override fun getItemCount(): Int {
        return productosList.size
    }

    public class MyViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val nombre : TextView = itemView.findViewById(R.id.main_name_product_detailed2XD)
        val tipo : TextView = itemView.findViewById(R.id.main_name_vet_product_detailed2)
        val precio: TextView = itemView.findViewById(R.id.yearsDetailed2Product)
        val descripcion: TextView = itemView.findViewById(R.id.emailDetailed2Product)
        val categoria: TextView = itemView.findViewById(R.id.categoriaDetailedProduct2)
        val nombre_veterinaria: TextView = itemView.findViewById(R.id.nameVeterinaria2ProductXD)
        val nit : TextView = itemView.findViewById(R.id.nit_product)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val producto : Productos = productosList[position]
        holder.nombre.text =producto.nombre
        holder.tipo.text = producto.tipo
        holder.precio.text = producto.precio
        holder.descripcion.text = producto.descripcion
        holder.categoria.text = producto.categoria
        holder.nombre_veterinaria.text = producto.nombre_veterinaria
        holder.nit.text = producto.nit
    }
}