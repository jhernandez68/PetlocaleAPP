package com.example.petlocale_final

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_veterinaria_main_maps.*
import kotlin.math.log


class VeterinariaMainMaps : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private lateinit var map: GoogleMap

    private lateinit var longitud_veterinaria : String

    private lateinit var latitud_veterinaria : String

    private lateinit var nombre_veterinaria_maps : String

    private var control_marcadores  = "No creado"


    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    companion object{
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinaria_main_maps)

        //Se obtiene el nit
        val objetoIntent: Intent = intent

        var nit = objetoIntent.getStringExtra("Nombre")


        createFragment()

        //Se trae los datos del producto en la bd
        db.collection("veterinarias")
            .document(nit.toString()).get().addOnSuccessListener {
                nombre_veterinariaMaps.setText(it.get("nombre") as String?)
                nombre_veterinaria_maps = nombre_veterinariaMaps.text.toString()
            }

        //Se trae los datos del producto en la bd
        db.collection("veterinarias")
            .document(nit.toString())
            .collection("ubicaciones")
            .document("ubicacion").get().addOnSuccessListener {
                longitudVeterinaria.setText(it.get("longitud") as String?)
                latitudVeterinaria.setText(it.get("latitud") as String?)
                longitud_veterinaria = longitudVeterinaria.text.toString()
                latitud_veterinaria = latitudVeterinaria.text.toString()

                if(longitud_veterinaria.isNotEmpty() && latitud_veterinaria.isNotEmpty()){
                    createMarker(latitud_veterinaria.toDouble(), longitud_veterinaria.toDouble(),nombre_veterinariaMaps.text.toString())
                    control_marcadores = "Creado"
                }else{
                    Toast.makeText(this, "Toca el botón superior derecho para ir a tu ubicación", Toast.LENGTH_SHORT).show()
                }
            }


        cancelarUbicacionButton.setOnClickListener {
            startActivity(Intent(this, VeterinariaMainInfo::class.java).putExtra("Nombre",  nit))
        }

        createmarkerButton.setOnClickListener {

            //Primero se evalua si se ha creado un marcador.

            if(control_marcadores == "Creado"){
                Toast.makeText(this, "Solo puedes crear un marcador!", Toast.LENGTH_LONG).show()
            }

            if(control_marcadores != "Creado"){
                Toast.makeText(this, "Mantenlo presionado y muevelo hasta la ubicación de tu negocio, ahí guardalo!", Toast.LENGTH_LONG).show()
                if(latitud_veterinaria.isNotEmpty() && longitud_veterinaria.isNotEmpty()){
                    createMarker(latitud_veterinaria.toDouble(), longitud_veterinaria.toDouble(), "hola")
                    control_marcadores = "Creado"
                }

                if(latitud_veterinaria.isEmpty() && longitud_veterinaria.isEmpty()){
                    Toast.makeText(this, "Primero toca el botón azul!", Toast.LENGTH_LONG).show()
                }
            }
        }

        saveUbicationButton.setOnClickListener {
            db.collection("veterinarias")
                .document(nit.toString())
                .collection("ubicaciones")
                .document("ubicacion").set(
                    hashMapOf(
                        "nit" to nit.toString(),
                        "latitud" to latitud_veterinaria,
                        "longitud" to longitud_veterinaria,
                    ))

            db.collection("ubicaciones_veterinarias")
                .document(nit.toString()).set(
                    hashMapOf(
                        "nombre" to nombre_veterinariaMaps.text.toString(),
                        "latitud" to latitud_veterinaria,
                        "longitud" to longitud_veterinaria,
                    ))
            Toast.makeText(this, "Ubicación guardada con éxito!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, VeterinariaMainInfo::class.java).putExtra("Nombre",  nit))
        }
    }

    private fun createFragment(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapVeterinaria) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        enableLocation()
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        map.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {}
            override fun onMarkerDragEnd(marker: Marker) {
                latitud_veterinaria = marker.position.latitude.toString()
                longitud_veterinaria = marker.position.longitude.toString()
            }
            override fun onMarkerDrag(marker: Marker) {}
        })

    }

    private fun createMarker(latitud: Double, longitud: Double , nombre: String){
        val coordinates = com.google.android.gms.maps.model.LatLng(latitud, longitud)
        val marker = MarkerOptions().position(coordinates).title(nombre).draggable(true)
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates,18f), 4000, null
        )
    }

    private fun isLocationPermissionGranted() =
        ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun enableLocation(){
        if(!::map.isInitialized) return
        if(isLocationPermissionGranted()){
            map.isMyLocationEnabled = true
        }else{
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(this, "Activa los permisos", Toast.LENGTH_SHORT).show()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled = true
            }else{
                Toast.makeText(this, "Activa los permisos", Toast.LENGTH_SHORT).show()
            }else ->{}
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if(!::map.isInitialized) return
        if(!isLocationPermissionGranted()){
            map.isMyLocationEnabled = false
            Toast.makeText(this, "Activa los permisos", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        if(control_marcadores == "Creado"){
            Toast.makeText(this, "Diriengose a tu ubicación", Toast.LENGTH_LONG).show()
        }
        if(control_marcadores != "Creado"){
            Toast.makeText(this, "Toca el botón azul y crea un marcador", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        if(control_marcadores == "Creado"){
            Toast.makeText(this, "Ya has creado un marcador!", Toast.LENGTH_LONG).show()
        }
        if(control_marcadores != "Creado"){
            Toast.makeText(this, "Ahora crea el marcador", Toast.LENGTH_SHORT).show()
            longitud_veterinaria = p0.longitude.toString()
            latitud_veterinaria = p0.latitude.toString()
        }
    }

}