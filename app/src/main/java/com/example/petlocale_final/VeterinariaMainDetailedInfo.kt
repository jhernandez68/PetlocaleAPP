package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_usuario_main_info.*
import kotlinx.android.synthetic.main.activity_veterinaria_main_detailed_info.*
import kotlinx.android.synthetic.main.activity_veterinaria_main_detailed_info.emailUserInfo2XD
import kotlinx.android.synthetic.main.activity_veterinaria_main_detailed_info.nombreUserInfo2

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
            nombreUserInfo2.setText(it.get("nombre") as String?)
            yearsDetailed2Product.setText(it.get("years") as String?)
            emailUserInfo2XD.setText(it.get("nit") as String?)
            emailDetailed2Product.setText(it.get("email") as String?)
            passwordFBVet.setText(it.get("password") as String?)
        }

        if(nombreUserInfo2.text.isNotEmpty() && emailUserInfo2XD.text.isNotEmpty()){
            buttonGuardarInfoVeterinaria.setOnClickListener {
                db.collection("veterinarias").document(nit.toString()).set(
                    hashMapOf(
                        "nombre" to nombreUserInfo2.text.toString(),
                        "email" to emailDetailed2Product.text.toString(),
                        "years" to yearsDetailed2Product.text.toString(),
                        "nit" to emailUserInfo2XD.text.toString(),
                        "password" to passwordFBVet.text.toString()
                    ))
                startActivity(Intent(this, VeterinariaMainInfo::class.java).putExtra("Nombre", nit ))
            }
        }

        if(nombreUserInfo2.text.isEmpty() && emailUserInfo2XD.text.isEmpty()){
            Toast.makeText(this, "¡Rellena todos los campos!", Toast.LENGTH_LONG).show()
        }

    }
}