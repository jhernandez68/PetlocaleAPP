package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_registro.*

class Registro : AppCompatActivity() {

    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    //Tipos de mascota
    val tipos = arrayOf("Gato y Perro", "Gato", "Perro")

    //Variable para guardar el tipo
    private lateinit var tipo_mascota : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        //Spinner 1 - tipo de mascota
        val spinner = findViewById<Spinner>(R.id.spinnerTipoMascotaRegistro)
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tipos)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //Variable para guardar el tipo
                tipo_mascota = tipos[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                tipo_mascota = tipos[0]
            }

        }

        registrarse.setOnClickListener{
            if(editTextTextPassword.text.isEmpty() ||
                editTextTextEmailAddress.text.isEmpty() ||
                editTextTextPassword2.text.isEmpty()){
                Toast.makeText(this, "Rellena todos los campos!", Toast.LENGTH_LONG).show()
            }

            if(editTextTextEmailAddress.text.isNotEmpty() &&
                editTextTextPassword.text.isNotEmpty() &&
                editTextTextPassword2.text.isNotEmpty()){

                if( editTextTextPassword.text.toString() !=
                    editTextTextPassword2.text.toString() ){
                    Toast.makeText(this, "Las contraseñas no son iguales!", Toast.LENGTH_LONG).show()
                }

                if(editTextTextPassword.text.toString() == editTextTextPassword2.text.toString()){

                    //Se verifica que la contraseña tenga al menos 6 dígitos
                        if(editTextTextPassword.text.toString().length > 5){
                            FirebaseAuth.getInstance()
                                .createUserWithEmailAndPassword(editTextTextEmailAddress.text.toString()
                                    , editTextTextPassword.text.toString())
                                .addOnCompleteListener{
                                    if(it.isSuccessful){

                                        db.collection("usuarios")
                                            .document(editTextTextEmailAddress.text.toString()).set(
                                                hashMapOf(
                                                    "email" to editTextTextEmailAddress.text.toString(),
                                                    "tipo_mascota" to tipo_mascota
                                                ))
                                        startActivity(Intent(this, Logeo::class.java))
                                    }
                                }.addOnFailureListener{
                                    exception ->
                                    handleException(exception)

                                    showAlert(handleException(exception))

                                    Log.d("Datos", "${handleException(exception)}")
                                }
                        }
                    if(editTextTextPassword.text.toString().length < 6){
                        Toast.makeText(this, "La contraseña es muy corta!", Toast.LENGTH_LONG).show()
                    }

                }
            }

        }
    }





    //Manejo de excepciones
    private fun handleException(exception: Exception?): String{
        //exception is null
        when(exception){
            is FirebaseAuthUserCollisionException -> return "Correo registrado"
            is FirebaseNetworkException -> return "Revisa tu conexión a internet"
            else -> return "Error producido al registrar el usuario"
        }
    }


    private fun showAlert( mensaje : String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")

        if(mensaje == null || mensaje == "null"){
            builder.setMessage("Error producido al registrar el usuario")
        }else{
            builder.setMessage(mensaje)

        }
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showMain(){
        val homeIntent = Intent(this,MainActivity::class.java).apply {
        }
        startActivity(homeIntent)
    }
}