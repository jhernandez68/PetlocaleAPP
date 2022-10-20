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
import com.example.petlocale_final.databinding.ActivityVeterinariaMainProductosBinding
import com.example.petlocale_final.databinding.ActivityVeterinariaMainProductosDetailedBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_veterinaria_main_productos_detailed.*
import java.io.File

class VeterinariaMainProductosDetailed : AppCompatActivity() {

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

    lateinit var binding : ActivityVeterinariaMainProductosDetailedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVeterinariaMainProductosDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val objetoIntent: Intent = intent

        var nombre_producto = objetoIntent.getStringExtra("nombre_producto")

        var nombre_veterinaria = objetoIntent.getStringExtra("nombre_veterinaria")

        var nit_product = objetoIntent.getStringExtra("nit")

        //datos de a imagen
        val storageRef = FirebaseStorage.getInstance().reference.child("images/${nit_product}/${nombre_producto}.jpg")
        val localfile = File.createTempFile("tempImage", "jpg")


        storageRef.getFile(localfile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.imageView6Edit.setImageBitmap(bitmap)

        }


        db.collection("veterinarias")
            .document(nit_product.toString())
            .collection("productos")
            .document(nombre_producto.toString())
            .get()
            .addOnSuccessListener {

                tipoFirebaseEdit.setText(it.get("tipo") as String?)
                categoriaFirebaseEdit.setText(it.get("categoria") as String?)

                tipo_mascota_firebase = tipoFirebaseEdit.text.toString()
                categoria_firebase = categoriaFirebaseEdit.text.toString()


                //Tipos de mascota
                val tipos = arrayOf("Actualmente $tipo_mascota_firebase","Gato", "Perro")

                //Tipos de mascota
                val categorias = arrayOf("Actualmente $categoria_firebase","Medicina", "Accesorios", "Juguetes", "Ropa","Alimentos", "Higiene", "Limpieza" )

                //Spinner 1 - tipo de mascota
                val spinner = findViewById<Spinner>(R.id.spinnerProductEdit)
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
                        tipo_mascota = tipoFirebaseEdit.text.toString()
                    }

                }


                //Spinner 2 - categorias
                val spinnerCategoria = findViewById<Spinner>(R.id.spinnerProductEdit2)
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
                        categoria_mascota = categoriaFirebaseEdit.text.toString()
                    }

                }

                nombreEditProduct.setText(it.get("nombre") as String?)
                    costoProductEdit.setText(it.get("precio") as String?)
                    descripcionProductEdit.setText(it.get("descripcion") as String?)
                    costoProductEdit2.setText(it.get("cantidad") as String?)
            }

        editProductButton2.setOnClickListener {

            if(nombreEditProduct.text.isNotEmpty() &&
                descripcionProductEdit.text.isNotEmpty() &&
                costoProductEdit.text.isNotEmpty() &&
                costoProductEdit2.text.isNotEmpty()){
                db.collection("veterinarias")
                    .document(nit_product.toString())
                    .collection("productos")
                    .document(nombre_producto.toString()).set(
                    hashMapOf(
                        "nombre" to nombreEditProduct.text.toString(),
                        "precio" to costoProductEdit.text.toString(),
                        "descripcion" to descripcionProductEdit.text.toString(),
                        "tipo" to tipo_mascota,
                        "cantidad" to costoProductEdit2.text.toString(),
                        "nombre_veterinaria" to nombre_veterinaria,
                        "categoria" to categoria_mascota,
                        "nit" to nit_product
                    ))
                startActivity(Intent(this, VeterinariaMainProductos::class.java).putExtra("Nombre", nit_product ))
            }

            if(nombreEditProduct.text.isEmpty() &&
                descripcionProductEdit.text.isEmpty() &&
                costoProductEdit.text.isEmpty() &&
                costoProductEdit2.text.isEmpty()){
                Toast.makeText(this, "¡Rellena todos los campos!", Toast.LENGTH_LONG).show()
            }
        }
    }
}