package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_usuario_main_servicios_info.*

class UsuarioMainServiciosInfo : AppCompatActivity() {
    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario_main_servicios_info)

        val objetoIntent: Intent = intent

        var nombre_servicio = objetoIntent.getStringExtra("nombre_servicio")

        var nombre_veterinaria = objetoIntent.getStringExtra("nombre_veterinaria")

        var nit_servicio = objetoIntent.getStringExtra("nit")

        var email = objetoIntent.getStringExtra("email")

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
    }
}