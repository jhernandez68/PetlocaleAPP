package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class VeterinariaMainHorarios : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinaria_main_horarios)

        //Se obtiene el nit
        val objetoIntent: Intent = intent

        var nit = objetoIntent.getStringExtra("nit")


    }
}