package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_service.*
import kotlinx.android.synthetic.main.activity_logeo_veterinaria.*
import kotlinx.android.synthetic.main.activity_registro_veterinaria.*

class AddService : AppCompatActivity() {

    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    //Tipos de mascota
    val tipos = arrayOf("Gato", "Perro")

    //Variable para guardar el tipo
    private lateinit var tipo_mascota : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service)

        //Se trae el nombre de la veterinaria
        val objetoIntent: Intent = intent

        var Nombre = objetoIntent.getStringExtra("nit")

        //Spinner
        val spinner = findViewById<Spinner>(R.id.spinner)
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tipos)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //Variable para guardar el tipo
                tipo_mascota = tipos[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                tipo_mascota = tipos[0]
            }

        }
        addServicebutton2.setOnClickListener {

            if(nombreService.text.isNotEmpty() &&
                descripcionService.text.isNotEmpty() &&
                costoService2.text.isNotEmpty()){
                db.collection("veterinarias").document(Nombre.toString()).collection("productos").document(nombreService.text.toString()).set(
                    hashMapOf(
                        "nombre" to nombreService.text.toString(),
                        "precio" to costoService2.text.toString(),
                        "descripcion" to descripcionService.text.toString(),
                        "tipo" to tipo_mascota
                    ))
                startActivity(Intent(this, VeterinariaMainServicios::class.java).putExtra("Nombre", Nombre ))
            }

            if(nombreService.text.isEmpty() &&
                descripcionService.text.isEmpty() &&
                costoService2.text.isEmpty()){
                Toast.makeText(this, "¡Rellena todos los campos!", Toast.LENGTH_LONG).show()
            }

        }

    }
}



