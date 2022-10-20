package com.example.petlocale_final

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RatingBar
import android.widget.Toast
import com.example.petlocale_final.databinding.ActivityRateServiceBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_rate_service.*
import java.io.File


//Instancia de la DB
private val db = FirebaseFirestore.getInstance()

private lateinit var valor : String

private lateinit var prueba : String

lateinit var binding : ActivityRateServiceBinding

class RateService : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRateServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val objetoIntent: Intent = intent

        var nombre_servicio = objetoIntent.getStringExtra("nombre_servicio")

        var nombre_veterinaria = objetoIntent.getStringExtra("nombre_veterinaria")

        var nit_servicio = objetoIntent.getStringExtra("nit")

        var email = objetoIntent.getStringExtra("email")

        val ratingBar = findViewById<RatingBar>(R.id.ratingBarService)

        //datos de a imagen
        val storageRef = FirebaseStorage.getInstance().reference.child("images/${nit_servicio}/${nombre_servicio}.jpg")
        val localfile = File.createTempFile("tempImage", "jpg")


        storageRef.getFile(localfile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.imageViewService.setImageBitmap(bitmap)

        }


        //Se trae los datos del servicio en la bd
        db.collection("veterinarias")
            .document(nit_servicio.toString())
            .collection("servicios")
            .document(nombre_servicio.toString())
            .get().addOnSuccessListener {
                textView10Service.setText(it.get("nombre") as String?)
            }

        //Se trae la calificación del usuario
        db.collection("usuarios")
            .document(email.toString())
            .collection("reseñas")
            .document(nit_servicio.toString() + "_reseñas")
            .collection("reseñas_servicios")
            .document(nombre_servicio.toString()).get().addOnSuccessListener {
                reseña2Service.setText(it.get("reseña") as String?)
                calificacion_servicio_firebase.setText(it.get("calificación") as String?)

                //Se guarda la calificación si el usuario ya la hizo

                if(calificacion_servicio_firebase.text.isNotEmpty()){
                    prueba = calificacion_servicio_firebase.text.toString()
                    ratingBar.rating = prueba.toFloat()
                }
            }

        ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
            valor = fl.toString() }

        guardarRatingButtonService.setOnClickListener{

            if(reseña2Service.text.isEmpty()){
                Toast.makeText(this, "Rellena todos los campos!", Toast.LENGTH_LONG).show()
            }

            if(reseña2Service.text.isNotEmpty()){
                //Se guardan los datos para el usuario
                db.collection("usuarios")
                    .document(email.toString())
                    .collection("reseñas")
                    .document(nit_servicio.toString() + "_reseñas")
                    .collection("reseñas_servicios")
                    .document(nombre_servicio.toString())
                    .set(
                        hashMapOf(
                            "servicio" to nombre_servicio.toString(),
                            "reseña" to reseña2Service.text.toString(),
                            "calificacion" to ratingBar.rating.toString()
                        )
                    )

                //Se guardan los datos para la veterinaria
                db.collection("veterinarias")
                    .document(nit_servicio.toString())
                    .collection("servicios")
                    .document(nombre_servicio.toString())
                    .collection("reseñas")
                    .document(email.toString())
                    .set(
                        hashMapOf(
                            "usuario" to email.toString(),
                            "servicio" to nombre_servicio.toString(),
                            "reseña" to reseña2Service.text.toString(),
                            "calificacion" to ratingBar.rating.toString()
                        )
                    )
                //Se pasa a la nueva actividad
                val intent = Intent(this@RateService, UsuarioMainServiciosInfo::class.java)
                intent.putExtra("nombre_veterinaria", nombre_veterinaria)
                intent.putExtra("nombre_servicio", nombre_servicio)
                intent.putExtra("nit", nit_servicio)
                intent.putExtra("email", email)

                startActivity(intent)
            }
        }

        cancelarRatingButtonService.setOnClickListener{
            //Se pasa a la nueva actividad
            val intent = Intent(this@RateService, UsuarioMainServiciosInfo::class.java)
            intent.putExtra("nombre_veterinaria", nombre_veterinaria)
            intent.putExtra("nombre_servicio", nombre_servicio)
            intent.putExtra("nit", nit_servicio)
            intent.putExtra("email", email)

            startActivity(intent)
        }
    }
}
