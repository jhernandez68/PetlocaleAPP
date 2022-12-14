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
import com.example.petlocale_final.databinding.ActivityAddProductBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_product.*
import java.io.File

class AddProduct : AppCompatActivity() {

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

    lateinit var binding : ActivityAddProductBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Se trae el nombre de la veterinaria
        val objetoIntent: Intent = intent

        var Nombre = objetoIntent.getStringExtra("nit")

        var nombre_producto_imagen = objetoIntent.getStringExtra("nombre_producto_imagen")

        if(nombre_producto_imagen != "null"){
            cargarImagen(nombre_producto_imagen.toString(), Nombre.toString())
        }

        setUp()


    }

    private fun setUp() {
        //Se trae el nombre de la veterinaria
        val objetoIntent: Intent = intent

        var Nombre = objetoIntent.getStringExtra("nit")

        db.collection("veterinarias")
            .document(Nombre.toString()).get().addOnSuccessListener {
            nombreVeterinariaXD.setText(it.get("nombre") as String?)
            nombre_veterinaria = nombreVeterinariaXD.text.toString()
        }

        //Spinner 1 - tipo de mascota
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
        val spinnerCategoria = findViewById<Spinner>(R.id.spinnerProduct2)
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


        addProductButton2.setOnClickListener {

            if(nombreDeleteProduct.text.isNotEmpty() &&
                descripcionProduct.text.isNotEmpty() &&
                costoProduct.text.isNotEmpty() &&
                costoProduct2.text.isNotEmpty()){
                db.collection("veterinarias")
                    .document(Nombre.toString())
                    .collection("productos")
                    .document(nombreDeleteProduct.text.toString()).set(
                    hashMapOf(
                        "nombre" to nombreDeleteProduct.text.toString(),
                        "precio" to costoProduct.text.toString(),
                        "descripcion" to descripcionProduct.text.toString(),
                        "tipo" to tipo_mascota,
                        "cantidad" to costoProduct2.text.toString(),
                        "nombre_veterinaria" to nombre_veterinaria,
                        "categoria" to categoria_mascota,
                        "nit" to Nombre
                    ))
                startActivity(Intent(this, VeterinariaMainProductos::class.java).putExtra("Nombre", Nombre ))
            }

            if(nombreDeleteProduct.text.isEmpty() ||
                descripcionProduct.text.isEmpty() ||
                costoProduct.text.isEmpty() ||
                costoProduct2.text.isEmpty()){
                Toast.makeText(this, "??Rellena todos los campos!", Toast.LENGTH_LONG).show()
            }
        }

        imageView6.setOnClickListener {

            if(nombreDeleteProduct.text.isNotEmpty()){
                startActivity(Intent(this, AddProductImage::class.java).putExtra("Nombre", Nombre ).putExtra("Nombre_Producto",nombreDeleteProduct.text.toString() ))
            }

            if(nombreDeleteProduct.text.isEmpty()){
                Toast.makeText(this, "??Primero digita el nombre!!", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun cargarImagen(nombre_producto : String, nit_veterinaria : String ) {
        if(nombre_producto != "null" && nombre_producto != nit_veterinaria){
            nombreDeleteProduct.setText(nombre_producto)

            val storageRef = FirebaseStorage.getInstance().reference.child("images/$nit_veterinaria/$nombre_producto.jpg")
            val localfile = File.createTempFile("tempImage", "jpg")


            storageRef.getFile(localfile).addOnSuccessListener {

                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                 binding.imageView6.setImageBitmap(bitmap)

            }

        }
    }
}