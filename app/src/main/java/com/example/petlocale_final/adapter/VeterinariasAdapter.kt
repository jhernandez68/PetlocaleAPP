package com.example.petlocale_final.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.R
import com.example.petlocale_final.Veterinaria

class VeterinariasAdapter (private val veterinariasList : ArrayList<Veterinaria>): RecyclerView.Adapter<VeterinariasAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VeterinariasAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_veterinaria, parent, false)
        return MyViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return veterinariasList.size
    }

    public class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val nombre : TextView = itemView.findViewById(R.id.nombreVeterinariaList2)
        val email : TextView = itemView.findViewById(R.id.emailVeterinaria2)
        val years : TextView = itemView.findViewById(R.id.yearsVeterinaria2)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val veterinaria : Veterinaria = veterinariasList[position]
        holder.nombre.text = veterinaria.nombre
        holder.email.text = veterinaria.email
        holder.years.text = veterinaria.years
    }
}