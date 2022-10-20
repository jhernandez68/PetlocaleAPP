package com.example.petlocale_final

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.petlocale_final.databinding.ActivityRateServiceBinding
import com.example.petlocale_final.databinding.ActivityUsuarioMainServiciosInfoBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_usuario_main_servicios_info.*
import java.io.File

class UsuarioMainServiciosInfo : AppCompatActivity() {
    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    lateinit var binding: ActivityUsuarioMainServiciosInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuarioMainServiciosInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val objetoIntent: Intent = intent

        var nombre_servicio = objetoIntent.getStringExtra("nombre_servicio")

        var nombre_veterinaria = objetoIntent.getStringExtra("nombre_veterinaria")

        var nit_servicio = objetoIntent.getStringExtra("nit")

        var email = objetoIntent.getStringExtra("email")

        val storageRef = FirebaseStorage.getInstance().reference.child("images/${nit_servicio}/${nombre_servicio}.jpg")
        val localfile = File.createTempFile("tempImage", "jpg")


        storageRef.getFile(localfile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.mainServiceDetailed.setImageBitmap(bitmap)

        }

        db.collection("veterinarias")
            .document(nit_servicio.toString())
            .collection("servicios")
            .document(nombre_servicio.toString())
            .get().addOnSuccessListener {
                main_name_service_detailed_info2.setText(it.get("nombre") as String?)
                main_name_vet_service_detailed_info2.setText(it.get("nombre_veterinaria") as String?)
                main_precio_service_detailed_info2.setText(it.get("precio") as String?)
            }

        rateButtonService.setOnClickListener {
            val intent = Intent(this@UsuarioMainServiciosInfo, RateService::class.java)
            intent.putExtra("nombre_veterinaria", nombre_veterinaria)
            intent.putExtra("nombre_servicio", nombre_servicio)
            intent.putExtra("nit", nit_servicio)
            intent.putExtra("email", email)
            startActivity(intent)
        }

        opinionesServiceButton.setOnClickListener {
            val intent = Intent(this@UsuarioMainServiciosInfo, UsuarioMainServiciosInfoOpiniones::class.java)
            intent.putExtra("nombre_veterinaria", nombre_veterinaria)
            intent.putExtra("nombre_servicio", nombre_servicio)
            intent.putExtra("nit", nit_servicio)
            intent.putExtra("email", email)
            startActivity(intent)
        }

        compararButtonService.setOnClickListener {
            val intent = Intent(this@UsuarioMainServiciosInfo, UsuarioMainServiciosComparacion::class.java)
            intent.putExtra("nombre_veterinaria1", nombre_veterinaria)
            intent.putExtra("nombre_servicio1", nombre_servicio)
            intent.putExtra("nit_servicio1", nit_servicio)
            intent.putExtra("email", email)
            startActivity(intent)
        }
    }
}