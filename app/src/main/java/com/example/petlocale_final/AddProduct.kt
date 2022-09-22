package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_product.*

class AddProduct : AppCompatActivity() {

    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    //Tipos de mascota
    val tipos = arrayOf("Gato", "Perro")

    //Variable para guardar el tipo
    private lateinit var tipo_mascota : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        //Se trae el nombre de la veterinaria
        val objetoIntent: Intent = intent

        var Nombre = objetoIntent.getStringExtra("nit")

        //Spinner
        val spinner = findViewById<Spinner>(R.id.spinnerProduct)
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

        addProductButton2.setOnClickListener {

            if(nombreDeleteProduct.text.isNotEmpty() &&
                descripcionProduct.text.isNotEmpty() &&
                costoProduct.text.isNotEmpty() &&
                costoProduct2.text.isNotEmpty()){
                db.collection("veterinarias").document(Nombre.toString()).collection("productos").document(nombreDeleteProduct.text.toString()).set(
                    hashMapOf(
                        "nombre" to nombreDeleteProduct.text.toString(),
                        "precio" to costoProduct.text.toString(),
                        "descripcion" to descripcionProduct.text.toString(),
                        "tipo" to tipo_mascota,
                        "cantidad" to costoProduct2.text.toString()
                    ))
                startActivity(Intent(this, VeterinariaMainProductos::class.java).putExtra("Nombre", Nombre ))
            }

            if(nombreDeleteProduct.text.isEmpty() &&
                descripcionProduct.text.isEmpty() &&
                costoProduct.text.isEmpty() &&
                    costoProduct2.text.isEmpty()){
                Toast.makeText(this, "¡Rellena todos los campos!", Toast.LENGTH_LONG).show()
            }

        }

    }
}