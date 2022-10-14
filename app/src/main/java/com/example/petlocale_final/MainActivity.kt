package com.example.petlocale_final

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_usuario_main_info.*


enum class ProviderType{
    BASIC,
    GOOGLE
}
class MainActivity : AppCompatActivity() {

    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    private lateinit var testino : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Setup
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("'provider")

        //Guardado de datos

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()

        db.collection("usuarios").document(email.toString()).get().addOnSuccessListener {
            email_user_main.setText(it.get("email") as String?)
            testino = email_user_main.text.toString()


            if (testino == ""){

                Toast.makeText(this, "entró", Toast.LENGTH_LONG).show()

                db.collection("usuarios").document(email.toString()).set(
                    hashMapOf(
                        "email" to email.toString(),
                        "tipo_mascota" to "Gato y Perro"
                    ))
            }
        }


        //Botón para utilizar API de Google Maps
        google_mapsButton.setOnClickListener{
            startActivity(Intent(this, MapsActivity::class.java))
        }

        //Boton para ir a INFO de usuario
        infoUsers.setOnClickListener {
            startActivity(Intent(this, UsuarioMainInfo::class.java).putExtra("email", email))
        }

        searchProductUserButton.setOnClickListener {
            startActivity(Intent(this, UsuarioMainProductos::class.java).putExtra("email", email))
        }

        searchServiceUserButton.setOnClickListener {
            startActivity(Intent(this, UsuarioMainServicios::class.java).putExtra("email", email))
        }

        buttonVeterinarias.setOnClickListener {
            startActivity(Intent(this, UsuarioMainVeterinarias::class.java).putExtra("email", email))
        }
    }

}