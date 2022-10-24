package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_veterinaria_main_detailed_info.*
import kotlinx.android.synthetic.main.activity_veterinaria_main_detailed_info.main_name_vet_product_detailed2
import kotlinx.android.synthetic.main.activity_veterinaria_main_detailed_info.main_name_product_detailed2XD

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
            main_name_product_detailed2XD.setText(it.get("nombre") as String?)
            yearsDetailed2Product.setText(it.get("years") as String?)
            main_name_vet_product_detailed2.setText(it.get("nit") as String?)
            emailDetailed2Product.setText(it.get("email") as String?)
            passwordFBVet.setText(it.get("password") as String?)

            buttonCancelarInfoVeterinaria3.setOnClickListener{
                startActivity(Intent(this, VeterinariaMain::class.java).putExtra("nit", nit ))
            }
        }


        if(main_name_product_detailed2XD.text.isNotEmpty() && main_name_vet_product_detailed2.text.isNotEmpty()){
            buttonGuardarInfoVeterinaria.setOnClickListener {
                db.collection("veterinarias").document(nit.toString()).set(
                    hashMapOf(
                        "nombre" to main_name_product_detailed2XD.text.toString(),
                        "email" to emailDetailed2Product.text.toString(),
                        "years" to yearsDetailed2Product.text.toString(),
                        "nit" to main_name_vet_product_detailed2.text.toString(),
                        "password" to passwordFBVet.text.toString()
                    ))

                startActivity(Intent(this, VeterinariaMain::class.java).putExtra("Nombre", nit ))
            }
        }

        if(main_name_product_detailed2XD.text.isEmpty() && main_name_vet_product_detailed2.text.isEmpty()){
            Toast.makeText(this, "¡Rellena todos los campos!", Toast.LENGTH_LONG).show()
        }

    }
}