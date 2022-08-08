package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_logeo.*

class Logeo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logeo)

        val registroIntent = Intent(this,Registro::class.java)
        textView3.setOnClickListener{
            startActivity(registroIntent)
        }

        entrar.setOnClickListener{

            if(password.text.isEmpty() || password.text.isEmpty()){
                Toast.makeText(this, "Rellena todos los campos!", Toast.LENGTH_LONG).show()
            }

            if(password.text.isNotEmpty() && password.text.isNotEmpty() && username.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(username.text.toString()
                    , password.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        showMain(it.result?.user?.email?: "", ProviderType.BASIC)
                    }else{
                        showAlert()
                    }
                }
            }

        }


    }
    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Contraseña o correo incorrecto")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showMain(email: String, provider: ProviderType){
        val homeIntent = Intent(this,MainActivity::class.java).apply {

        }
        startActivity(homeIntent)
    }
}