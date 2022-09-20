package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_service.*
import kotlinx.android.synthetic.main.activity_delete_service.*
import kotlinx.android.synthetic.main.activity_delete_service.nombreService

class DeleteService : AppCompatActivity() {

    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_service)

        val objetoIntent: Intent = intent

        var Nombre = objetoIntent.getStringExtra("nit")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("¿Estás seguro de borrar?")

        borrarService.setOnClickListener{

            builder.setPositiveButton(android.R.string.ok) {
                dialog, which ->

                db.collection("veterinarias").document(Nombre.toString()).collection("productos")
                    .document(nombreService.text.toString()).delete()

                Toast.makeText(this, "Borrado correctamente", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, VeterinariaMainServicios::class.java).putExtra("Nombre", Nombre ))
            }

            builder.setNegativeButton("Cancelar", null)

            builder.show()
        }
    }
}