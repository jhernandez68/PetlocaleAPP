package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_veterinaria_main_detailed_info.*

class VeterinariaMainDetailedInfo : AppCompatActivity() {

    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinaria_main_detailed_info)

        //Se obtiene el nit
        val objetoIntent: Intent = intent

        var nit = objetoIntent.getStringExtra("Nombre")

        db.collection("veterinarias").document(nit.toString()).get().addOnSuccessListener {
            nombreDetailed2Product.setText(it.get("nombre") as String?)
            yearsDetailed2Product.setText(it.get("years") as String?)
            nitDetailed2Product.setText(it.get("nit") as String?)
            emailDetailed2Product.setText(it.get("email") as String?)
        }

    }
}