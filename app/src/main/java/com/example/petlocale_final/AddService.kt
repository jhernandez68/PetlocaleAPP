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
import kotlinx.android.synthetic.main.activity_add_service.*
import kotlinx.android.synthetic.main.activity_add_service.costoProduct
import kotlinx.android.synthetic.main.activity_add_service.descripcionProduct
import kotlinx.android.synthetic.main.activity_add_service.nombreDeleteProduct

class AddService : AppCompatActivity() {

    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    //Tipos de mascota
    val tipos = arrayOf("Gato", "Perro")

    //Tipos de mascota
    val categorias = arrayOf("Medicina", "Accesorios", "Juguetes", "Ropa","Alimentos", "Higiene", "Limpieza" )

    //Variable para guardar el tipo
    private lateinit var tipo_mascota : String

    //Variable para guardar la categoria
    private lateinit var categoria_mascota : String

    //Variable para el nombre de la veterinaria
    private lateinit var nombre_veterinaria : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service)

        //Se trae el nombre de la veterinaria
        val objetoIntent: Intent = intent

        var Nombre = objetoIntent.getStringExtra("nit")


        //Variable para nombre de veterinaria

        db.collection("veterinarias").document(Nombre.toString()).get().addOnSuccessListener {
            nombreVeterinariaXDService.setText(it.get("nombre") as String?)
            nombre_veterinaria = nombreVeterinariaXDService.text.toString()
        }

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

        //Spinner 2 - categorias
        val spinnerCategoria = findViewById<Spinner>(R.id.spinnerProduct3)
        val arrayAdapterCategoria = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categorias)
        spinnerCategoria.adapter = arrayAdapterCategoria

        spinnerCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //Variable para guardar el tipo
                categoria_mascota = categorias[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                categoria_mascota = categorias[0]
            }

        }

        addServicebutton2.setOnClickListener {

            if(nombreDeleteProduct.text.isNotEmpty() &&
                descripcionProduct.text.isNotEmpty() &&
                costoProduct.text.isNotEmpty()){
                db.collection("veterinarias").document(Nombre.toString()).collection("servicios").document(nombreDeleteProduct.text.toString()).set(
                    hashMapOf(
                        "nombre" to nombreDeleteProduct.text.toString(),
                        "precio" to costoProduct.text.toString(),
                        "descripcion" to descripcionProduct.text.toString(),
                        "tipo" to tipo_mascota,
                        "nombre_veterinaria" to nombre_veterinaria,
                        "categoria" to categoria_mascota
                    ))
                startActivity(Intent(this, VeterinariaMainServicios::class.java).putExtra("Nombre", Nombre ))
            }

            if(nombreDeleteProduct.text.isEmpty() &&
                descripcionProduct.text.isEmpty() &&
                costoProduct.text.isEmpty()){
                Toast.makeText(this, "¡Rellena todos los campos!", Toast.LENGTH_LONG).show()
            }

        }

    }
}



