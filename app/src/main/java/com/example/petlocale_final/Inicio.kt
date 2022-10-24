package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_inicio.*

class Inicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        //Se quita el logeo del usuario
        FirebaseAuth.getInstance().signOut()

        //Botón para iniciar sesión como dueño de mascota
        usuariosButton.setOnClickListener{
            startActivity(Intent(this, Logeo::class.java))
        }
        //Botón para iniciar sesión como dueño de veterinaria
        veterinariasButton.setOnClickListener {
            startActivity(Intent(this, LogeoVeterinaria::class.java))
        }
    }
}