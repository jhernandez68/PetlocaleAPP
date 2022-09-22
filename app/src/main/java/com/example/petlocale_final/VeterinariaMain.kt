package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_veterinaria_main.*

class VeterinariaMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinaria_main)

        //Se obtiene el nit
        val objetoIntent: Intent = intent

        var nit = objetoIntent.getStringExtra("nit")

        productosButton.setOnClickListener {
            startActivity(Intent(this, VeterinariaMainProductos::class.java).putExtra("Nombre",  nit))
        }

        serviciosButton.setOnClickListener {
            startActivity(Intent(this, VeterinariaMainServicios::class.java).putExtra("Nombre",  nit))
        }

        infoButton.setOnClickListener {
            startActivity(Intent(this, VeterinariaMainInfo::class.java).putExtra("Nombre",  nit))
        }
    }
}