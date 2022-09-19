package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_logeo_veterinaria.*
import kotlinx.android.synthetic.main.activity_registro.*

class LogeoVeterinaria : AppCompatActivity() {

    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    var test = String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logeo_veterinaria)

        textView3.setOnClickListener{
            startActivity(Intent(this, RegistroVeterinaria::class.java))
        }

        entrar.setOnClickListener{

            if(nitlogin.text.isEmpty() || passwordLoginVet.text.isEmpty()){
                Toast.makeText(this, "Rellena todos los campos!", Toast.LENGTH_LONG).show()
            }

            if(nitlogin.text.isNotEmpty() && passwordLoginVet.text.isNotEmpty()){
                //Si el usuario digitó completo

                //Get de la db para comprobar.
                db.collection("veterinarias")
                    .document(nitlogin.text.toString())
                    .get()
                    .addOnSuccessListener {

                        passwordfb.setText(it.get("password") as String?)

                        if(passwordfb.text.toString() == passwordLoginVet.text.toString()){
                            startActivity(Intent(this, VeterinariaMain::class.java).putExtra("nit", nitlogin.text.toString()))
                        }

                        if(passwordfb.text.toString() != passwordLoginVet.text.toString()){
                            Toast.makeText(this, "Contraseña o correo incorrectos!!", Toast.LENGTH_LONG).show()
                        }
                    }
            }


        }
    }
}