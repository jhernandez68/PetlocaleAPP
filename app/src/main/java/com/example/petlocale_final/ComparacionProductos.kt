package com.example.petlocale_final

import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.petlocale_final.databinding.ActivityComparacionProductosBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_comparacion_productos.*
import java.io.File

class ComparacionProductos : AppCompatActivity() {

    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    private lateinit var opinionesArrayList: ArrayList<Opinion>

    private lateinit var opinionesArrayList2: ArrayList<Opinion>

    lateinit var binding : ActivityComparacionProductosBinding

    var calificacion_producto1 : Double = 0.0
    var calificacion_producto2 : Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComparacionProductosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cargarDatos()
    }

    private fun cargarDatos() {

        opinionesArrayList = arrayListOf()

        opinionesArrayList2 = arrayListOf()

        //Se obtienen los datos del producto 1
        val objetoIntent: Intent = intent

        var email = objetoIntent.getStringExtra("email")

        var nombre_producto1 = objetoIntent.getStringExtra("nombre_producto1")

        var nombre_veterinaria1 = objetoIntent.getStringExtra("nombre_veterinaria1")

        var nit_producto1 = objetoIntent.getStringExtra("nit_producto1")



        val storageRef = FirebaseStorage.getInstance().reference.child("images/${nit_producto1}/${nombre_producto1}.jpg")
        val localfile = File.createTempFile("tempImage", "jpg")

        storageRef.getFile(localfile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.imagenProducto1.setImageBitmap(bitmap)

        }


        //Se obtienen los datos del producto 2

        var nombre_producto2 = objetoIntent.getStringExtra("nombre_producto2")

        var nombre_veterinaria2 = objetoIntent.getStringExtra("nombre_veterinaria2")

        var nit_producto2 = objetoIntent.getStringExtra("nit_producto2")


        val storageRef2 = FirebaseStorage.getInstance().reference.child("images/${nit_producto2}/${nombre_producto2}.jpg")
        val localfile2 = File.createTempFile("tempImage", "jpg")


        storageRef2.getFile(localfile2).addOnSuccessListener {

            val bitmap2 = BitmapFactory.decodeFile(localfile2.absolutePath)
            binding.imagenProducto2.setImageBitmap(bitmap2)

        }


        //Variables de comparación
        var mayor : Double = 0.0
        var nombreMayor : String = "No calificado"

        var menorCosto : Double = 0.0
        var nombreMenorCosto : String = "No calificado"


        //Se llenan los datos del producto1

        db.collection("veterinarias")
            .document(nit_producto1.toString())
            .collection("productos")
            .document(nombre_producto1.toString())
            .get()
            .addOnSuccessListener {
                nombreComparacionProducto2.setText(it.get("nombre") as String?)
                tipoComparacionProducto2.setText(it.get("tipo") as String?)
                costoComparacionProducto2.setText(it.get("precio") as String?)
                categoriaComparacionProducto2.setText(it.get("categoria") as String?)
                nombreVeterinariaComparacionProducto2.setText(it.get("nombre_veterinaria") as String?)

                //Se guarda el menorcosto
                menorCosto = costoComparacionProducto2.text.toString().toDouble()

                //Se calcula lo del producto2
                db.collection("veterinarias")
                    .document(nit_producto2.toString())
                    .collection("productos")
                    .document(nombre_producto2.toString())
                    .get()
                    .addOnSuccessListener {
                        nombreComparacionProducto4.setText(it.get("nombre") as String?)
                        tipoComparacionProducto4.setText(it.get("tipo") as String?)
                        costoComparacionProducto4.setText(it.get("precio") as String?)
                        categoriaComparacionProducto4.setText(it.get("categoria") as String?)
                        nombreVeterinariaComparacionProducto4.setText(it.get("nombre_veterinaria") as String?)

                        if(costoComparacionProducto4.text.toString().toDouble() == menorCosto ){
                            calificacionMejorCostoProducto2.setText("Empate")
                        }

                        if(costoComparacionProducto4.text.toString().toDouble() < menorCosto ){
                            menorCosto = costoComparacionProducto4.text.toString().toDouble()
                            calificacionMejorCostoProducto2.setText(menorCosto.toString() + " ($nombre_veterinaria2)")
                        }

                        if(costoComparacionProducto4.text.toString().toDouble() > menorCosto ){
                            Log.d("Costo", "Entro al segundo if")
                            calificacionMejorCostoProducto2.setText(menorCosto.toString() + " ($nombre_veterinaria1)")
                        }

                    }
            }




        //Se trae los datos de la calificacion en la bd
        db.collection("veterinarias")
            .document(nit_producto1.toString())
            .collection("productos")
            .document(nombre_producto1.toString())
            .collection("reseñas")
            .get().addOnSuccessListener { resultado ->
                for (document in resultado) {
                    opinionesArrayList.add(document.toObject(Opinion::class.java))
                    Log.d("Datos", "${document.id} => ${document.data}")
                }

                var sumatoria : Double = 0.0
                var promedio : Double = 0.0

                opinionesArrayList.forEach {
                    sumatoria = it.calificacion!!.toDouble() + sumatoria
                }

                Log.d("Datos", "${opinionesArrayList.size}")

                promedio = sumatoria / opinionesArrayList.size

                if(opinionesArrayList.size > 0){
                    mayor = promedio
                    calificacionComparacionProducto2.setText(promedio.toString())
                    nombreMayor = nombre_veterinaria1.toString()
                    calificacion_producto1 = promedio
                }
                Log.d("Mayor", "Mayor1 ${mayor}")


                //Calificación producto 2
                db.collection("veterinarias")
                    .document(nit_producto2.toString())
                    .collection("productos")
                    .document(nombre_producto2.toString())
                    .collection("reseñas")
                    .get().addOnSuccessListener { resultado ->
                        for (document in resultado) {
                            opinionesArrayList2.add(document.toObject(Opinion::class.java))
                        }

                        var sumatoria2 : Double = 0.0
                        var promedio2 : Double = 0.0

                        opinionesArrayList2.forEach {
                            sumatoria2 = it.calificacion!!.toDouble() + sumatoria2
                        }

                        promedio2 = sumatoria2 / opinionesArrayList2.size

                        if(opinionesArrayList2.size > 0){
                            calificacionComparacionProducto4.setText(promedio2.toString())
                            calificacion_producto2 = promedio2


                            if(calificacion_producto2 == mayor){
                                calificacionMejorPrecioProducto2.setText("Empate")
                            }

                            if(calificacion_producto2 > mayor){
                                nombreMayor = nombre_veterinaria2.toString()
                                mayor = calificacion_producto2
                                calificacionMejorPrecioProducto2.setText(calificacion_producto2.toString() + "($nombreMayor)")
                            }
                            if(calificacion_producto2 < mayor){
                                calificacionMejorPrecioProducto2.setText(mayor.toString() + "($nombreMayor)")
                            }

                        }
                    }
            }
    }



}