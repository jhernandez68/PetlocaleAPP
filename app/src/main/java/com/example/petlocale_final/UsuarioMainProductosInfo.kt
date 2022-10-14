package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_usuario_main_info.*
import kotlinx.android.synthetic.main.activity_usuario_main_productos_info.*

class UsuarioMainProductosInfo : AppCompatActivity() {

    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario_main_productos_info)

        val objetoIntent: Intent = intent

        var nombre_producto = objetoIntent.getStringExtra("nombre_producto")

        var nombre_veterinaria = objetoIntent.getStringExtra("nombre_veterinaria")

        var nit_product = objetoIntent.getStringExtra("nit")

        db.collection("veterinarias")
            .document(nit_product.toString()).collection("productos").document(nombre_producto.toString()).get().addOnSuccessListener {
           main_name_product_detailed_info2.setText(it.get("nombre") as String?)
            main_name_vet_product_detailed_info2.setText(it.get("nombre_veterinaria") as String?)
            main_precio_product_detailed_info2.setText(it.get("precio") as String?)
        }

        rateButton.setOnClickListener {
            startActivity(Intent(this, RateProduct::class.java).putExtra("nit", nit_product ))
        }
    }


}