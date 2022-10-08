package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_veterinaria_main.*
import kotlinx.android.synthetic.main.activity_veterinaria_main_info.*

class VeterinariaMainInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinaria_main_info)

        //Se obtiene el nit
        val objetoIntent: Intent = intent

        var nit = objetoIntent.getStringExtra("Nombre")

        horariosButton.setOnClickListener {
            startActivity(Intent(this, VeterinariaMainHorarios::class.java).putExtra("Nombre",  nit))
        }

        datosButton.setOnClickListener {
            startActivity(Intent(this, VeterinariaMainDetailedInfo::class.java).putExtra("Nombre",  nit))
        }
    }
}