package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.android.synthetic.main.activity_registro.*

class Registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        registrarse.setOnClickListener{
            if(editTextTextPassword.text.isEmpty() ||
                editTextTextEmailAddress.text.isEmpty() ||
                editTextTextPassword2.text.isEmpty()){
                Toast.makeText(this, "Rellena todos los campos!", Toast.LENGTH_LONG).show()
            }

            if(editTextTextEmailAddress.text.isNotEmpty() &&
                editTextTextPassword.text.isNotEmpty() &&
                editTextTextPassword2.text.isNotEmpty()){

                if( editTextTextPassword.text.toString() != editTextTextPassword2.text.toString() ){
                    Toast.makeText(this, "Las contraseñas no son iguales!", Toast.LENGTH_LONG).show()
                }

                if(editTextTextPassword.text.toString() == editTextTextPassword2.text.toString()){

                    //Se verifica que la contraseña tenga al menos 6 dígitos
                        if(editTextTextPassword.text.toString().length > 5){
                            FirebaseAuth.getInstance()
                                .createUserWithEmailAndPassword(editTextTextEmailAddress.text.toString()
                                    , editTextTextPassword.text.toString()).addOnCompleteListener{
                                    if(it.isSuccessful){
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