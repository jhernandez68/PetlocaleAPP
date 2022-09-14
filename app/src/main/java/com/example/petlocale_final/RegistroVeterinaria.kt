package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_registro.*
import kotlinx.android.synthetic.main.activity_registro.registrarse
import kotlinx.android.synthetic.main.activity_registro_veterinaria.*

class RegistroVeterinaria : AppCompatActivity() {

    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_veterinaria)

        //Botón de registro, se crea una colección en la DB
        registrarse.setOnClickListener {

            //Si el usuario digitó todos los datos
            if( nit.text.isNotEmpty() &&
                nameVeterinaria.text.isNotEmpty() &&
                passwordVeterinaria.text.isNotEmpty() &&
                yearsExperience.text.isNotEmpty()){

                    db.collection("veterinarias").document(nit.text.toString()).set(
                    hashMapOf( "nombre" to nameVeterinaria.text.toString(),
                        "password" to passwordVeterinaria.text.toString(),
                        "years" to yearsExperience.text.toString()
                    )
                )
                startActivity(Intent(this, LogeoVeterinaria::class.java))
            }

            //Si el usuario no digitó todos los datos
            if( nit.text.isEmpty() ||
                nameVeterinaria.text.isEmpty() ||
                passwordVeterinaria.text.isEmpty() ||
                yearsExperience.text.isEmpty()){
                Toast.makeText(this, "Rellena todos los campos!", Toast.LENGTH_LONG).show()
            }

        }
    }

}