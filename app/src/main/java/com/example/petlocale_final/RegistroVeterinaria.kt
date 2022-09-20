package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
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
            if( nitDetailed.text.isNotEmpty() &&
                nameVeterinariaDetailed.text.isNotEmpty() &&
                passwordVeterinaria.text.isNotEmpty() &&
                yearsExperienceDetailed.text.isNotEmpty() &&
                    passwordVeterinaria2.text.isNotEmpty()){

                        //Se verifica que las contraseñas sean iguales
                        if(passwordVeterinaria.text.toString() == passwordVeterinaria2.text.toString()){
                            db.collection("veterinarias").document(nitDetailed.text.toString()).set(
                                hashMapOf( "nombre" to nameVeterinariaDetailed.text.toString(),
                                    "password" to passwordVeterinaria.text.toString(),
                                    "years" to yearsExperienceDetailed.text.toString(),
                                    "email" to correoVeterinaria.text.toString(),
                                    "nit" to nitDetailed.text.toString()
                                ))
                            startActivity(Intent(this, LogeoVeterinaria::class.java))
                        }

                         if(passwordVeterinaria.text.toString() != passwordVeterinaria2.text.toString()){
                             Toast.makeText(this, "¡Las contraseñas no son iguales!", Toast.LENGTH_LONG).show()
                         }
            }

            //Si el usuario no digitó todos los datos
            if( nitDetailed.text.isEmpty() ||
                nameVeterinariaDetailed.text.isEmpty() ||
                passwordVeterinaria.text.isEmpty() ||
                yearsExperienceDetailed.text.isEmpty()){
                Toast.makeText(this, "Rellena todos los campos!", Toast.LENGTH_LONG).show()
            }

        }
    }

}