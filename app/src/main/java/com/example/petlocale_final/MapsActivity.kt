package com.example.petlocale_final

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Marker

import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.activity_veterinaria_main_maps.*
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationChangeListener {

    private lateinit var map:GoogleMap

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object{
        const val REQUEST_CODE_LOCATION = 0
    }

    //Instancia de la DB
    private val db = FirebaseFirestore.getInstance()

    private lateinit var ubicacionesArrayList: ArrayList<UbicacionVeterinaria>
    private lateinit var ubicacionesArrayList2: ArrayList<UbicacionVeterinaria>

    private lateinit var distanciasArrayList : ArrayList<Double>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        distanciasArrayList = arrayListOf()

        ubicacionesArrayList = arrayListOf()
        ubicacionesArrayList2 = arrayListOf()

        inicio()

        createFragment()

    }

    private fun inicio() {

        val objetoIntent: Intent = intent

        var latitud_usuario = objetoIntent.getStringExtra("latitud_usuario")

        var longitud_usuario = objetoIntent.getStringExtra("longitud_usuario")

        var email_usuario = objetoIntent.getStringExtra("email")


        val resultados = FloatArray(1000)

        //Se trae los datos en la bd
        db.collectionGroup("ubicaciones_veterinarias")
            .get().addOnSuccessListener { resultado ->
                for (document in resultado) {
                    ubicacionesArrayList.add(document.toObject(UbicacionVeterinaria::class.java))
                    Log.d("Datos", "${document.id} => ${document.data}")
                }
                ubicacionesArrayList2.addAll(ubicacionesArrayList)

                ubicacionesArrayList2.forEach {

                    //Se crea el marcador
                    createMarker(it.latitud!!.toDouble(), it.longitud!!.toDouble(), it.nombre.toString())

                    //Se calcula la distancia entre el marcador y el usuario
                    Location.distanceBetween(latitud_usuario!!.toDouble(),longitud_usuario!!.toDouble(), it.latitud!!.toDouble(), it.longitud!!.toDouble(), resultados)

                    val s = String.format("%.1f", resultados[0]/100)
                    Log.d("Resultados", "$s")
                    distanciasArrayList.add((resultados[0]/100).toDouble())
                }

                Log.d("Resultados", "${distanciasArrayList[0]}")
                Log.d("Resultados", "${distanciasArrayList.size}")

                //Se calcula la distancia menor
                var distancia_menor = distanciasArrayList[0]
                distanciasArrayList.forEach {
                    if(distancia_menor > it){
                        distancia_menor = it
                    }
                }

                //Según la distancia menor se crea el ultimo marcador

                for (i in distanciasArrayList.indices){
                    if(distanciasArrayList[i] == distancia_menor){
                        createLastMarker(ubicacionesArrayList2[i].latitud!!.toDouble(),
                            ubicacionesArrayList2[i].longitud!!.toDouble(),
                            ubicacionesArrayList2[i].nombre!!.toString())
                    }
                }

                //Variable para aproximar el double

                val df = DecimalFormat("#.##")
                df.roundingMode = RoundingMode.DOWN

                for (i in distanciasArrayList.indices){

                    if(distanciasArrayList[i] < 10.0){
                        //Se guarda en la bd
                        db.collection("usuarios")
                            .document(email_usuario.toString())
                            .collection("ubicaciones")
                            .document(ubicacionesArrayList2[i].nombre.toString()).set(
                                hashMapOf(
                                    "nombre" to ubicacionesArrayList2[i].nombre.toString(),
                                    "distancia" to df.format(distanciasArrayList[i]).toString(),
                                    "years" to ubicacionesArrayList2[i].years.toString(),
                                    "email" to ubicacionesArrayList2[i].email.toString()
                                )
                            )
                    }

                }

            }

        listaVeterinariasCercanasMapsMainButton.setOnClickListener {
            startActivity(Intent(this, UsuarioMainVeterinariasCercanas::class.java).putExtra("latitud_usuario", latitud_usuario ).putExtra("longitud_usuario", longitud_usuario).putExtra("email", email_usuario))
        }

    }

    private fun createFragment(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        //createMarker()
        enableLocation()
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        map.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {}
            override fun onMarkerDragEnd(marker: Marker) {}
            override fun onMarkerDrag(marker: Marker) {}
        })

    }

    private fun createMarker(latitud: Double, longitud: Double , nombre: String){
        val coordinates = com.google.android.gms.maps.model.LatLng(latitud, longitud)
        val marker = MarkerOptions().position(coordinates).title(nombre)
        map.addMarker(marker)
    }

    private fun createLastMarker(latitud: Double, longitud: Double , nombre: String){
        val coordinates = com.google.android.gms.maps.model.LatLng(latitud, longitud)
        val marker = MarkerOptions().position(coordinates).title(nombre)
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates,16f), 2000, null
        )
        Toast.makeText(this, "Veterinaria más cercana: $nombre", Toast.LENGTH_SHORT).show()
    }

    private fun isLocationPermissionGranted() =
        ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

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

    override fun onMyLocationClick(p0: Location) {

    }

    override fun onMyLocationChange(p0: Location) {
        val target = Location("target")
        for (point in arrayOf<LatLng>()) {
            target.latitude = point.latitude
            target.longitude = point.longitude
            if (p0.distanceTo(target) < 100) {
                // bingo!
            }
        }
    }


}