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

    private lateinit  var horario5 : String

    private lateinit  var horario6 : String

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
        horarioDia5.setOnClickListener { showTimePickerDialog5() }
        horarioDia7.setOnClickListener { showTimePickerDialog6() }

        cancelarHorariosButton2.setOnClickListener{
            startActivity(Intent(this, VeterinariaMainInfo::class.java).putExtra("Nombre", nit))
        }

        guardarHorariosButton.setOnClickListener {
            if(horario1.isNotEmpty()
                && horario2.isNotEmpty()
                && horario3.isNotEmpty()
                && horario4.isNotEmpty()
                && horario5.isNotEmpty()
                && horario6.isNotEmpty()){

                //Se guarda en la bd
                db.collection("veterinarias")
                    .document(nit.toString())
                    .collection("horarios")
                    .document("horario_${nit.toString()}").set(
                        hashMapOf(
                            "nit_horarios" to nit.toString(),
                            "semana1" to horario1,
                            "semana1_fin" to horario2,
                            "sabado_inicio" to horario3,
                            "sabado_fin" to horario4,
                            "domingo_inicio" to horario5,
                            "domingo_fin" to horario6
                        )
                    )
                //Se obtiene el nit
                val objetoIntent: Intent = intent

                var nitxd = objetoIntent.getStringExtra("Nombre")

                startActivity(Intent(this, VeterinariaMain::class.java)
                    .putExtra("Nombre", nitxd))
            }

            if(horario1.isEmpty()
                || horario2.isEmpty()
                || horario3.isEmpty()
                || horario4.isEmpty()
                || horario5.isEmpty()
                || horario6.isEmpty()){
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
                horarioDia3.setText(it.get("sabado_inicio") as String?)
                horarioDia4.setText(it.get("sabado_fin") as String?)
                horarioDia5.setText(it.get("domingo_inicio") as String?)
                horarioDia7.setText(it.get("domingo_fin") as String?)
                horario1 = horarioDia1.text.toString()
                horario2 = horarioDia2.text.toString()
                horario3 = horarioDia3.text.toString()
                horario4 = horarioDia4.text.toString()
                horario5 = horarioDia5.text.toString()
                horario6 = horarioDia7.text.toString()
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

    private fun showTimePickerDialog5() {
        val timePicker = TimePickerFragment {time -> onTimeSelected5(time)}
        timePicker.show(supportFragmentManager, "time")
    }

    private fun showTimePickerDialog6() {
        val timePicker = TimePickerFragment {time -> onTimeSelected6(time)}
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
    private fun onTimeSelected5(time: String){
        horarioDia5.setText("$time")
        horario5 = time
    }
    private fun onTimeSelected6(time: String){
        horarioDia7.setText("$time")
        horario6 = time
    }
}