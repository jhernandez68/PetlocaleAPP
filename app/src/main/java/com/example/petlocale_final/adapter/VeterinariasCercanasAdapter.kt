package com.example.petlocale_final.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.R
import com.example.petlocale_final.VeterinariaCercana

class VeterinariasCercanasAdapter (private val veterinariasCercanaList : ArrayList<VeterinariaCercana>): RecyclerView.Adapter<VeterinariasCercanasAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VeterinariasCercanasAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_veterinaria_cercana, parent, false)
        return MyViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return veterinariasCercanaList.size
    }

    public class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val nombre : TextView = itemView.findViewById(R.id.nombreVeterinariaCercanaList2)
        val email : TextView = itemView.findViewById(R.id.emailVeterinariaCercana2)
        val years : TextView = itemView.findViewById(R.id.yearsVeterinariaCercana2)
        val distance : TextView = itemView.findViewById(R.id.distanciaVeterinariaCercana2)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val veterinariaCercana : VeterinariaCercana = veterinariasCercanaList[position]
        holder.nombre.text = veterinariaCercana.nombre
        holder.email.text = veterinariaCercana.email
        holder.years.text = veterinariaCercana.years
        holder.distance.text = veterinariaCercana.distancia
    }
}