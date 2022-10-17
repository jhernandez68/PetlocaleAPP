package com.example.petlocale_final.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.R
import com.example.petlocale_final.Servicio

class ServiciosAdapter (private val serviciosList : ArrayList<Servicio>): RecyclerView.Adapter<ServiciosAdapter.MyViewHolder>() {


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
    ): ServiciosAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_servicio, parent, false)
        return ServiciosAdapter.MyViewHolder(itemView, mListener)
    }


    override fun getItemCount(): Int {
        return serviciosList.size
    }

    public class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val nombre : TextView = itemView.findViewById(R.id.main_name_servicio_detailed2XD)
        val tipo : TextView = itemView.findViewById(R.id.main_name_vet_servicio_detailed2)
        val precio: TextView = itemView.findViewById(R.id.yearsDetailed2Servicio)
        val descripcion: TextView = itemView.findViewById(R.id.emailDetailed2Servicio)
        val nombre_veterinaria : TextView = itemView.findViewById(R.id.nameVeterinaria2XDServicio)
        val categoria : TextView = itemView.findViewById(R.id.categoriaServicio2)
        val nit : TextView = itemView.findViewById(R.id.nit_service)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val servicio : Servicio = serviciosList[position]
        holder.nombre.text =servicio.nombre
        holder.tipo.text = servicio.tipo
        holder.precio.text = servicio.precio
        holder.descripcion.text = servicio.descripcion
        holder.nombre_veterinaria.text = servicio.nombre_veterinaria
        holder.categoria.text = servicio.categoria
        holder.nit.text = servicio.nit
    }

}