package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_veterinaria_main_horarios.*

class VeterinariaMainHorarios : AppCompatActivity() {

    private lateinit  var horario1 : String

    private lateinit  var horario2 : String

    private lateinit  var horario3 : String

    private lateinit  var horario4 : String

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinaria_main_horarios)

        //Se obtiene el nit
        val objetoIntent: Intent = intent

        var nit = objetoIntent.getStringExtra("Nombre")


        inicioFirebase(nit.toString())

        horarioDia1.setOnClickListener { showTimePickerDialog() }
        horarioDia2.setOnClickListener { showTimePickerDialog2() }
        horarioDia3.setOnClickListener { showTimePickerDialog3() }
        horarioDia4.setOnClickListener { showTimePickerDialog4() }

        guardarHorariosButton.setOnClickListener {
            if(horario1.isNotEmpty()
                && horario2.isNotEmpty()
                && horario3.isNotEmpty()
                && horario4.isNotEmpty()){

                //Se guarda en la bd
                db.collection("veterinarias")
                    .document(nit.toString())
                    .collection("horarios")
                    .document("horario_${nit.toString()}").set(
                        hashMapOf(
                            "nit_horarios" to nit.toString(),
                            "semana1" to horario1,
                            "semana1_fin" to horario2,
                            "sabado" to horario3,
                            "domingo" to horario4
                        )
                    )
                startActivity(Intent(this, VeterinariaMain::class.java).putExtra("nit", nit))
            }

            if(horario1.isEmpty()
                || horario2.isEmpty()
                || horario3.isEmpty()
                || horario4.isEmpty()){
                Toast.makeText(this, "Rellena todos los campos!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun inicioFirebase(nit : String) {
        db.collection("veterinarias")
            .document(nit.toString())
            .collection("horarios")
            .document("horario_${nit}")
            .get()
            .addOnSuccessListener {
                horarioDia1.setText(it.get("semana1") as String?)
                horarioDia2.setText(it.get("semana1_fin") as String?)
                horarioDia3.setText(it.get("sabado") as String?)
                horarioDia4.setText(it.get("domingo") as String?)
            }
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment {time -> onTimeSelected(time)}
        timePicker.show(supportFragmentManager, "time")
    }

    private fun showTimePickerDialog2() {
        val timePicker = TimePickerFragment {time -> onTimeSelected2(time)}
        timePicker.show(supportFragmentManager, "time")
    }

    private fun showTimePickerDialog3() {
        val timePicker = TimePickerFragment {time -> onTimeSelected3(time)}
        timePicker.show(supportFragmentManager, "time")
    }

    private fun showTimePickerDialog4() {
        val timePicker = TimePickerFragment {time -> onTimeSelected4(time)}
        timePicker.show(supportFragmentManager, "time")
    }

    private fun onTimeSelected(time: String){
        horarioDia1.setText("$time")
        horario1 = time
    }

    private fun onTimeSelected2(time: String){
        horarioDia2.setText("$time")
        horario2 = time
    }
    private fun onTimeSelected3(time: String){
        horarioDia3.setText("$time")
        horario3 = time
    }
    private fun onTimeSelected4(time: String){
        horarioDia4.setText("$time")
        horario4 = time
    }



}