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
import kotlinx.android.synthetic.main.activity_veterinaria_main_servicios_detailed.*

class VeterinariaMainServiciosDetailed : AppCompatActivity() {

    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    //Variables de firebase para el spinner
    private lateinit var tipo_mascota_firebase : String

    //Variables de firebase para el spinner
    private lateinit var categoria_firebase : String

    //Variable para guardar el tipo
    private lateinit var tipo_mascota : String

    //Variable para guardar la categoria
    private lateinit var categoria_mascota : String

    //Variable para el nombre de la veterinaria
    private lateinit var nombre_veterinaria : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinaria_main_servicios_detailed)

        val objetoIntent: Intent = intent

        var nombre_servicio = objetoIntent.getStringExtra("nombre_servicio")

        var nombre_veterinaria = objetoIntent.getStringExtra("nombre_veterinaria")

        var nit_servicio = objetoIntent.getStringExtra("nit")

        db.collection("veterinarias")
            .document(nit_servicio.toString())
            .collection("servicios")
            .document(nombre_servicio.toString())
            .get()
            .addOnSuccessListener {

                tipoServiceEditFirebase.setText(it.get("tipo") as String?)
                categoriaServiceEditFirebase.setText(it.get("categoria") as String?)

                tipo_mascota_firebase = tipoServiceEditFirebase.text.toString()
                categoria_firebase = categoriaServiceEditFirebase.text.toString()


                //Tipos de mascota
                val tipos = arrayOf("Actualmente $tipo_mascota_firebase","Gato", "Perro")

                //Tipos de mascota
                val categorias = arrayOf("Actualmente $categoria_firebase","Medicina", "Accesorios", "Juguetes", "Ropa","Alimentos", "Higiene", "Limpieza" )

                //Spinner 1 - tipo de mascota
                val spinner = findViewById<Spinner>(R.id.spinnerServiceEdit)
                val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tipos)
                spinner.adapter = arrayAdapter

                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        //Variable para guardar el tipo
                        if(p2 != 0){
                            tipo_mascota = tipos[p2]
                        }

                        if(p2 == 0){
                            tipo_mascota = tipo_mascota_firebase
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        tipo_mascota = tipoServiceEditFirebase.text.toString()
                    }

                }


                //Spinner 2 - categorias
                val spinnerCategoria = findViewById<Spinner>(R.id.spinnerEditService3)
                val arrayAdapterCategoria = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categorias)
                spinnerCategoria.adapter = arrayAdapterCategoria

                spinnerCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        //Variable para guardar el tipo
                        if(p2 != 0){
                            categoria_mascota = categorias[p2]
                        }

                        if(p2 == 0){
                            categoria_mascota = categoria_firebase
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        categoria_mascota = categoriaServiceEditFirebase.text.toString()
                    }

                }

                nombreEditService.setText(it.get("nombre") as String?)
                costoEditService.setText(it.get("precio") as String?)
                descripcionEditService.setText(it.get("descripcion") as String?)
            }

        editServicebutton2.setOnClickListener {

            if(nombreEditService.text.isNotEmpty() &&
                descripcionEditService.text.isNotEmpty() &&
                costoEditService.text.isNotEmpty()){
                db.collection("veterinarias")
                    .document(nit_servicio.toString())
                    .collection("servicios")
                    .document(nombre_servicio.toString()).set(
                        hashMapOf(
                            "nombre" to nombreEditService.text.toString(),
                            "precio" to costoEditService.text.toString(),
                            "descripcion" to descripcionEditService.text.toString(),
                            "tipo" to tipo_mascota,
                            "nombre_veterinaria" to nombre_veterinaria,
                            "categoria" to categoria_mascota,
                            "nit" to nit_servicio
                        ))
                startActivity(Intent(this, VeterinariaMainServicios::class.java).putExtra("Nombre", nit_servicio ))
            }

            if(nombreEditService.text.isEmpty() &&
                descripcionEditService.text.isEmpty() &&
                costoEditService.text.isEmpty()){
                Toast.makeText(this, "¡Rellena todos los campos!", Toast.LENGTH_LONG).show()
            }
        }

    }
}