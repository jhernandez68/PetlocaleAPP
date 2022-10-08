package com.example.petlocale_final

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*


enum class ProviderType{
    BASIC,
    GOOGLE
}
class MainActivity : AppCompatActivity() {

    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

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