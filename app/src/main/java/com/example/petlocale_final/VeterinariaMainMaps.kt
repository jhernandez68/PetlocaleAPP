package com.example.petlocale_final

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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


class VeterinariaMainMaps : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private lateinit var map: GoogleMap

    private lateinit var longitud_veterinaria : String

    private lateinit var latitud_veterinaria : String

    private lateinit var nombre_veterinaria_maps : String

    var coordinates = LatLng(4.494263200850947, -74.25779643356155)


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
                    coordinates = LatLng(latitud_veterinaria.toDouble(), longitud_veterinaria.toDouble())

                }
                createFragment()
            }


        cancelarUbicacionButton.setOnClickListener {
            startActivity(Intent(this, VeterinariaMainInfo::class.java).putExtra("Nombre",  nit))
        }

        saveUbicationButton.setOnClickListener {
            db.collection("veterinarias")
                .document(nit.toString())
                .collection("ubicaciones")
                .document("ubicacion").set(
                    hashMapOf(
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
        createMarker()
        enableLocation()
        map.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {}
            override fun onMarkerDragEnd(marker: Marker) {
                latitud_veterinaria = marker.position.latitude.toString()
                longitud_veterinaria = marker.position.longitude.toString()
            }
            override fun onMarkerDrag(marker: Marker) {}
        })

    }

    private fun createMarker(){

        val marker = MarkerOptions()
            .position(coordinates)
            .title(nombre_veterinaria_maps)
            .draggable(true)

        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),4000, null
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
        return false
    }

}