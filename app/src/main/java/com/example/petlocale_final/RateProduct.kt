package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RatingBar
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_rate_product.*

class RateProduct : AppCompatActivity() {

    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    private lateinit var valor : String

    private lateinit var prueba : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_product)

        val objetoIntent: Intent = intent

        var nombre_producto = objetoIntent.getStringExtra("nombre_producto")

        var nombre_veterinaria = objetoIntent.getStringExtra("nombre_veterinaria")

        var nit_product = objetoIntent.getStringExtra("nit")

        var email = objetoIntent.getStringExtra("email")

        val ratingBar = findViewById<RatingBar>(R.id.ratingBarProduct)

        //Se trae los datos del producto en la bd
        db.collection("veterinarias")
            .document(nit_product.toString())
            .collection("productos")
            .document(nombre_producto.toString())
            .get().addOnSuccessListener {
                textView10.setText(it.get("nombre") as String?)
            }

        //Se trae la calificación del usuario
        db.collection("usuarios")
            .document(email.toString())
            .collection("reseñas")
            .document(nit_product.toString() + "_reseñas")
            .collection("reseñas_productos")
            .document(nombre_producto.toString()).get().addOnSuccessListener {
                reseña2.setText(it.get("reseña") as String?)
                calificacion_producto_firebase.setText(it.get("calificación") as String?)

                //Se guarda la calificación si el usuario ya la hizo

                if(calificacion_producto_firebase.text.isNotEmpty()){
                    prueba = calificacion_producto_firebase.text.toString()
                    ratingBar.rating = prueba.toFloat()
                }
            }

        ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
            valor = fl.toString() }

        guardarRatingButton.setOnClickListener{

            if(reseña2.text.isEmpty()){
                Toast.makeText(this, "Rellena todos los campos!", Toast.LENGTH_LONG).show()
            }

            if(reseña2.text.isNotEmpty()){
                //Se guardan los datos para el usuario
                db.collection("usuarios")
                    .document(email.toString())
                    .collection("reseñas")
                    .document(nit_product.toString() + "_reseñas")
                    .collection("reseñas_productos")
                    .document(nombre_producto.toString())
                    .set(
                        hashMapOf(
                            "producto" to nombre_producto.toString(),
                            "reseña" to reseña2.text.toString(),
                            "calificación" to ratingBar.rating.toString()
                        )
                    )

                //Se guardan los datos para la veterinaria
                db.collection("veterinarias")
                    .document(nit_product.toString())
                    .collection("productos")
                    .document(nombre_producto.toString())
                    .collection("reseñas")
                    .document(email.toString())
                    .set(
                        hashMapOf(
                            "usuario" to email.toString(),
                            "producto" to nombre_producto.toString(),
                            "reseña" to reseña2.text.toString(),
                            "calificación" to ratingBar.rating.toString()
                        )
                    )
                //Se pasa a la nueva actividad
                val intent = Intent(this@RateProduct, UsuarioMainProductosInfo::class.java)
                intent.putExtra("nombre_veterinaria", nombre_veterinaria)
                intent.putExtra("nombre_producto", nombre_producto)
                intent.putExtra("nit", nit_product)
                intent.putExtra("email", email)

                startActivity(intent)
            }
        }

        cancelarRatingButton.setOnClickListener{
            //Se pasa a la nueva actividad
            val intent = Intent(this@RateProduct, UsuarioMainProductosInfo::class.java)
            intent.putExtra("nombre_veterinaria", nombre_veterinaria)
            intent.putExtra("nombre_producto", nombre_producto)
            intent.putExtra("nit", nit_product)
            intent.putExtra("email", email)

            startActivity(intent)
        }
    }
}