package com.example.petlocale_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_logeo_miss_password.*

class Logeo_MissPassword : AppCompatActivity() {

    private lateinit var emailMissed : EditText

    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logeo_miss_password)

        auth = FirebaseAuth.getInstance()

        emailMissed = findViewById(R.id.correo_recuperar_contraseña)
        buttonRecuperarContraseña.setOnClickListener {

            val sPassword = emailMissed.text.toString()

            if(sPassword.isEmpty()){
                Toast.makeText(this, "Digita el correo!", Toast.LENGTH_LONG).show()
            }

            if(sPassword.isNotEmpty()){
                auth.sendPasswordResetEmail(correo_recuperar_contraseña.text.toString())
                    .addOnSuccessListener {
                        Toast.makeText(this, "Revisa el correo!", Toast.LENGTH_LONG).show()

                    }
                    .addOnFailureListener{
                        Toast.makeText(this, "${it.toString()}", Toast.LENGTH_LONG).show()

                    }
            }

        }
    }
}