package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_usuario_main_info.*
import kotlinx.android.synthetic.main.activity_usuario_main_info.nombreUserInfo2

class UsuarioMainInfo : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    //Variable para guardar el tipo
    private lateinit var tipo_mascota : String

    private lateinit var XDD : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario_main_info)

        //Se obtiene el nit
        val objetoIntent: Intent = intent

        var email = objetoIntent.getStringExtra("email")


        db.collection("usuarios").document(email.toString()).get().addOnSuccessListener {

            infoTipoMascotaFirebase.setText(it.get("tipo_mascota") as String?)

            XDD = infoTipoMascotaFirebase.text.toString()


            //Tipos de mascota
            val tipos = arrayOf( "Actualmente: $XDD", "Gato", "Perro", "Gato y Perro")

            val spinner = findViewById<Spinner>(R.id.spinnerMascota)
            val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tipos)
            spinner.adapter = arrayAdapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    //Variable para guardar el tipo
                    tipo_mascota = tipos[p2]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    tipo_mascota = infoTipoMascotaFirebase.text.toString()
                }
            }

        }



        db.collection("usuarios").document(email.toString()).get().addOnSuccessListener {
            nombreUserInfo2.setText(it.get("nombre") as String?)
            emailUserInfo2XD.setText(it.get("email") as String?)
            nombreUserInfo2.setText(it.get("nombre") as String?)
        }

        if(nombreUserInfo2.text.isNotEmpty()){
            guardarInfoUserButton.setOnClickListener {
                db.collection("usuarios").document(email.toString()).set(
                    hashMapOf(
                        "email" to email.toString(),
                        "tipo_mascota" to tipo_mascota,
                        "nombre" to nombreUserInfo2.text.toString()
                    ))
                startActivity(Intent(this, MainActivity::class.java).putExtra("email", email ))
            }
        }

        if(nombreUserInfo2.text.isEmpty()){
            Toast.makeText(this, "¡Rellena todos los campos!", Toast.LENGTH_LONG).show()
        }
    }
}