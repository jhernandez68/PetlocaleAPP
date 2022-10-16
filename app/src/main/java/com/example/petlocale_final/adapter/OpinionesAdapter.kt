package com.example.petlocale_final.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.Opinion
import com.example.petlocale_final.R

class OpinionesAdapter (private val opinionesList : ArrayList<Opinion>): RecyclerView.Adapter<OpinionesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OpinionesAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_opiniones, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return opinionesList.size
    }

    public class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val usuario : TextView = itemView.findViewById(R.id.nombre_opinion2)
        val calificacion : TextView = itemView.findViewById(R.id.calificacion_opinion2)
        val reseña: TextView = itemView.findViewById(R.id.reseña_opinion2)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position : Int) {
        val opinion : Opinion = opinionesList[position]
        holder.usuario.text = opinion.usuario
        holder.calificacion.text = opinion.calificacion
        holder.reseña.text = opinion.reseña
    }

}