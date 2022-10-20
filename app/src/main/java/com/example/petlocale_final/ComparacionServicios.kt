package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_comparacion_servicios.*

class ComparacionServicios : AppCompatActivity() {
    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    private lateinit var opinionesArrayList: ArrayList<Opinion>

    private lateinit var opinionesArrayList2: ArrayList<Opinion>

    var calificacion_servicio1 : Double = 0.0
    var calificacion_servicio2 : Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comparacion_servicios)
        cargarDatos()
    }

    private fun cargarDatos() {

        opinionesArrayList = arrayListOf()

        opinionesArrayList2 = arrayListOf()

        //Se obtienen los datos del servicio 1
        val objetoIntent: Intent = intent

        var email = objetoIntent.getStringExtra("email")

        var nombre_servicio1 = objetoIntent.getStringExtra("nombre_servicio1")

        var nombre_veterinaria1 = objetoIntent.getStringExtra("nombre_servicio1")

        var nit_servicio1 = objetoIntent.getStringExtra("nit_servicio1")

        //Se obtienen los datos del servicio 2

        var nombre_servicio2 = objetoIntent.getStringExtra("nombre_servicio2")

        var nombre_veterinaria2 = objetoIntent.getStringExtra("nombre_veterinaria2")

        var nit_servicio2 = objetoIntent.getStringExtra("nit_servicio2")

        //Variables de comparación
        var mayor : Double = 0.0
        var nombreMayor : String = "No calificado"

        var menorCosto : Double = 0.0
        var nombreMenorCosto : String = "No calificado"


        //Se llenan los datos del servicio

        db.collection("veterinarias")
            .document(nit_servicio1.toString())
            .collection("servicios")
            .document(nombre_servicio1.toString())
            .get()
            .addOnSuccessListener {
                nombreComparacionServicio2.setText(it.get("nombre") as String?)
                tipoComparacionServicio2.setText(it.get("tipo") as String?)
                costoComparacionServicio2.setText(it.get("precio") as String?)
                categoriaComparacionServicio2.setText(it.get("categoria") as String?)
                nombreVeterinariaComparacionServicio2.setText(it.get("nombre_veterinaria") as String?)

                //Se guarda el menorcosto
                menorCosto = costoComparacionServicio2.text.toString().toDouble()

                //Se calcula lo del servicio 2
                db.collection("veterinarias")
                    .document(nit_servicio2.toString())
                    .collection("servicios")
                    .document(nombre_servicio2.toString())
                    .get()
                    .addOnSuccessListener {
                        nombreComparacionServicio4.setText(it.get("nombre") as String?)
                        tipoComparacionServicio4.setText(it.get("tipo") as String?)
                        costoComparacionServicio4.setText(it.get("precio") as String?)
                        categoriaComparacionServicio4.setText(it.get("categoria") as String?)
                        nombreVeterinariaComparacionServicio4.setText(it.get("nombre_veterinaria") as String?)

                        if(costoComparacionServicio4.text.toString().toDouble() == menorCosto ){
                            calificacionMejorCostoServicio2.setText("Empate")
                        }

                        if(costoComparacionServicio4.text.toString().toDouble() < menorCosto ){
                            menorCosto = costoComparacionServicio4.text.toString().toDouble()
                            calificacionMejorCostoServicio2.setText(menorCosto.toString() + " ($nombre_servicio2)")
                        }

                        if(costoComparacionServicio4.text.toString().toDouble() > menorCosto ){
                            Log.d("Costo", "Entro al segundo if")
                            calificacionMejorCostoServicio2.setText(menorCosto.toString() + " ($nombre_servicio1)")
                        }

                    }
            }




        //Se trae los datos de la calificacion en la bd
        db.collection("veterinarias")
            .document(nit_servicio1.toString())
            .collection("servicios")
            .document(nombre_servicio1.toString())
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
                    calificacionComparacionServicio2.setText(promedio.toString())
                    nombreMayor = nombre_servicio1.toString()
                    calificacion_servicio1 = promedio
                }
                Log.d("Mayor", "Mayor1 ${mayor}")


                //Calificación servicio 2
                db.collection("veterinarias")
                    .document(nit_servicio2.toString())
                    .collection("servicios")
                    .document(nombre_servicio2.toString())
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
                            calificacionComparacionServicio4.setText(promedio2.toString())
                            calificacion_servicio2 = promedio2


                            if(calificacion_servicio2 == mayor){
                                calificacionMejorPrecioServicio2.setText("Empate")
                            }

                            if(calificacion_servicio2 > mayor){
                                nombreMayor = nombre_servicio2.toString()
                                mayor = calificacion_servicio2
                                calificacionMejorPrecioServicio2.setText(calificacion_servicio2.toString() + "($nombreMayor)")
                            }
                            if(calificacion_servicio2 > mayor){
                                calificacionMejorPrecioServicio2.setText(mayor.toString() + "($nombreMayor)")
                            }

                        }
                        Log.d("Mayor", "Mayor??${mayor}")
                    }
            }
    }
}