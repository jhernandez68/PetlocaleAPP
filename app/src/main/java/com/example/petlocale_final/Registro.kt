package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registro.*

class Registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        registrarse.setOnClickListener{
            if(editTextTextPassword.text.isEmpty() || editTextTextEmailAddress.text.isEmpty() || editTextTextPassword2.text.isEmpty()){
                Toast.makeText(this, "Rellena todos los campos!", Toast.LENGTH_LONG).show()
            }

            if(editTextTextEmailAddress.text.isNotEmpty() && editTextTextPassword.text.isNotEmpty() && editTextTextPassword2.text.isNotEmpty()){

                if(editTextTextPassword.text.toString() == editTextTextPassword2.text.toString()){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(editTextTextEmailAddress.text.toString(), editTextTextPassword.text.toString())
                        .addOnCompleteListener{
                            if(it.isSuccessful){
                                showMain(it.result?.user?.email ?:"" , ProviderType.BASIC)
                            }else{
                                showAlert()
                            }
                        }
                }

                if( editTextTextPassword.text.toString() != editTextTextPassword2.text.toString() ){
                    Toast.makeText(this, "Las contraseñas no son iguales!", Toast.LENGTH_LONG).show()
                }

            }

        }
    }
    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error producido al registrar el usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showMain(email: String, provider: ProviderType){
        val homeIntent = Intent(this,MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }
}