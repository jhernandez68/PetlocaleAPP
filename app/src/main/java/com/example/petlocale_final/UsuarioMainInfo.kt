package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_usuario_main_info.*

class UsuarioMainInfo : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    //Tipos de mascota
    val tipos = arrayOf("Gato", "Perro", "Gato y Perro")

    //Variable para guardar el tipo
    private lateinit var tipo_mascota : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario_main_info)

        //Se obtiene el nit
        val objetoIntent: Intent = intent

        var email = objetoIntent.getStringExtra("email")


        val spinner = findViewById<Spinner>(R.id.spinnerMascota)
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tipos)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //Variable para guardar el tipo
                tipo_mascota = tipos[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                tipo_mascota = tipos[2]
            }
        }

        guardarInfoUserButton.setOnClickListener {
            db.collection("usuarios").document(email.toString()).set(
                hashMapOf(
                    "email" to email.toString(),
                    "tipo_mascota" to tipo_mascota
                ))
            startActivity(Intent(this, MainActivity::class.java).putExtra("email", email ))

        }


    }
}