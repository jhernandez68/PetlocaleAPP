package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_veterinaria_main_horarios.*

class VeterinariaMainHorarios : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinaria_main_horarios)

        //Se obtiene el nit
        val objetoIntent: Intent = intent

        var nit = objetoIntent.getStringExtra("nit")

        horarioDia1.setOnClickListener { showTimePickerDialog() }
        horarioDia2.setOnClickListener { showTimePickerDialog2() }
        horarioDia3.setOnClickListener { showTimePickerDialog3() }
        horarioDia4.setOnClickListener { showTimePickerDialog4() }
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
        horarioDia1.setText("Seleccionado: $time")
    }

    private fun onTimeSelected2(time: String){
        horarioDia2.setText("Seleccionado: $time")
    }
    private fun onTimeSelected3(time: String){
        horarioDia3.setText("Seleccionado: $time")
    }
    private fun onTimeSelected4(time: String){
        horarioDia4.setText("Seleccionado: $time")
    }
}