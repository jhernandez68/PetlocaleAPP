package com.example.petlocale_final

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.petlocale_final.databinding.ActivityAddServiceBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_service.*
import java.io.File

class AddService : AppCompatActivity() {

    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    //Tipos de mascota
    val tipos = arrayOf("Gato", "Perro")

    //Tipos de categoria
    val categorias = arrayOf("Consulta general", "Cirugia",
        "Consulta especializada", "Vacunación", "Desparacitación", "Higiene", "Baño", "Peluqueria", "Limpieza de oidos" )

    //Variable para guardar el tipo
    private lateinit var tipo_mascota : String

    //Variable para guardar la categoria
    private lateinit var categoria_mascota : String

    //Variable para el nombre de la veterinaria
    private lateinit var nombre_veterinaria : String

    lateinit var binding : ActivityAddServiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Se trae el nombre de la veterinaria
        val objetoIntent: Intent = intent

        var Nombre = objetoIntent.getStringExtra("nit")

        var nombre_servicio_imagen = objetoIntent.getStringExtra("nombre_servicio_imagen")


        cargarImagen(nombre_servicio_imagen.toString(), Nombre.toString())

        setUp()

    }

    private fun setUp() {

        //Se trae el nombre de la veterinaria
        val objetoIntent: Intent = intent

        var Nombre = objetoIntent.getStringExtra("nit")

        //Variable para nombre de veterinaria

        db.collection("veterinarias").document(Nombre.toString()).get().addOnSuccessListener {
            nombreVeterinariaXDService.setText(it.get("nombre") as String?)
            nombre_veterinaria = nombreVeterinariaXDService.text.toString()
        }

        //Spinner
        val spinner = findViewById<Spinner>(R.id.spinnerAddService)
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
        val spinnerCategoria = findViewById<Spinner>(R.id.spinnerAddService3)
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

            if(nombreAddService.text.isNotEmpty() &&
                descripcionAddService.text.isNotEmpty() &&
                costoAddService.text.isNotEmpty()){
                db.collection("veterinarias").document(Nombre.toString()).collection("servicios").document(nombreAddService.text.toString()).set(
                    hashMapOf(
                        "nombre" to nombreAddService.text.toString(),
                        "precio" to costoAddService.text.toString(),
                        "descripcion" to descripcionAddService.text.toString(),
                        "tipo" to tipo_mascota,
                        "nombre_veterinaria" to nombre_veterinaria,
                        "categoria" to categoria_mascota,
                        "nit" to Nombre.toString()
                    ))
                startActivity(Intent(this, VeterinariaMainServicios::class.java).putExtra("Nombre", Nombre ))
            }

            if(nombreAddService.text.isEmpty() ||
                descripcionAddService.text.isEmpty() ||
                costoAddService.text.isEmpty()){
                Toast.makeText(this, "¡Rellena todos los campos!", Toast.LENGTH_LONG).show()
            }
        }

        imageView6Service.setOnClickListener {

            if(nombreAddService.text.isNotEmpty()){
                startActivity(Intent(this, AddServiceImage::class.java).putExtra("Nombre", Nombre ).putExtra("Nombre_Servicio",nombreAddService.text.toString() ))
            }

            if(nombreAddService.text.isEmpty()){
                Toast.makeText(this, "¡Primero digita el nombre!!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun cargarImagen(nombre_servicio: String, nit_veterinaria : String) {
        if(nombre_servicio != "null"){
            nombreAddService.setText(nombre_servicio)

            val storageRef = FirebaseStorage.getInstance().reference.child("images/${nit_veterinaria}/$nombre_servicio.jpg")
            val localfile = File.createTempFile("tempImage", "jpg")


            storageRef.getFile(localfile).addOnSuccessListener {

                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                binding.imageView6Service.setImageBitmap(bitmap)

            }

        }
    }
}



