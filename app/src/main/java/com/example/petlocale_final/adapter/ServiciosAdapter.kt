package com.example.petlocale_final.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.R
import com.example.petlocale_final.Servicio

class ServiciosAdapter (private val serviciosList : ArrayList<Servicio>): RecyclerView.Adapter<ServiciosAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServiciosAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_servicio, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ServiciosAdapter.MyViewHolder, position: Int) {
        val servicio : Servicio = serviciosList[position]
        holder.nombre.text =servicio.nombre
        holder.tipo.text = servicio.tipo
        holder.precio.text = servicio.precio
        holder.descripcion.text = servicio.descripcion
    }

    override fun getItemCount(): Int {
        return serviciosList.size
    }

    public class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val nombre : TextView = itemView.findViewById(R.id.nombreDetailed2)
        val tipo : TextView = itemView.findViewById(R.id.nitDetailed2)
        val precio: TextView = itemView.findViewById(R.id.yearsDetailed2)
        val descripcion: TextView = itemView.findViewById(R.id.emailDetailed2)
    }
}