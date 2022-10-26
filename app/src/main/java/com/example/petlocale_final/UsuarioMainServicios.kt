package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.adapter.ServiciosAdapter
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.activity_usuario_main_servicios.*
import java.util.*
import kotlin.collections.ArrayList

class UsuarioMainServicios : AppCompatActivity() {

    private lateinit var tempArrayList : ArrayList<Servicio>
    private lateinit var recyclerView: RecyclerView
    private lateinit var servicioArrayList: ArrayList<Servicio>
    private lateinit var myAdapter: ServiciosAdapter
    private lateinit var db: FirebaseFirestore

    //Variable para guardar el correo de usuario
    private lateinit var correo : String

    //Tipos de categoria
    val categorias = arrayOf("Filtrar - Todos", "Consulta general", "Cirugia",
        "Consulta especializada", "Vacunación", "Desparacitación", "Higiene", "Baño", "Peluqueria", "Limpieza de oidos" )

    //Variable para guardar la categoria
    private lateinit var categoria_mascota : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario_main_servicios)

        //Se obtiene el email
        val objetoIntent: Intent = intent

        var email = objetoIntent.getStringExtra("email")

        correo = email.toString()

        recyclerView = findViewById(R.id.recyclerViewUserService)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        servicioArrayList = arrayListOf()

        tempArrayList = arrayListOf()

        myAdapter = ServiciosAdapter(tempArrayList)

        recyclerView.adapter = myAdapter

        textViewMainServiciosInfo.setOnClickListener{
            val intent = Intent(this@UsuarioMainServicios, MainActivity::class.java)
            intent.putExtra("email", email)
            startActivity(intent)
        }

        //Spinner - categorias
        val spinnerCategoria = findViewById<Spinner>(R.id.spinnerFiltrarCategoriaServicio)
        val arrayAdapterCategoria = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categorias)
        spinnerCategoria.adapter = arrayAdapterCategoria

        spinnerCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //Variable para guardar el tipo
                categoria_mascota = categorias[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                categoria_mascota = categorias[0]
            }

        }

        searchCategoriaServicio.setOnClickListener {
            FiltroCategoria(categoria_mascota)
        }


        myAdapter.setOnClickItemListener(object : ServiciosAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                var nombre_veterinaria = tempArrayList[position].nombre_veterinaria
                var nombre_servicio = tempArrayList[position].nombre
                var nit_service = tempArrayList[position].nit
                var categoria_servicio = tempArrayList[position].categoria

                val intent = Intent(this@UsuarioMainServicios, UsuarioMainServiciosInfo::class.java)
                intent.putExtra("nombre_veterinaria", nombre_veterinaria)
                intent.putExtra("nombre_servicio", nombre_servicio)
                intent.putExtra("categoria_servicio", categoria_servicio)
                intent.putExtra("nit", nit_service)
                intent.putExtra("email", email)

                startActivity(intent)

            }

        })

        EventChangeListener()
    }


    //Lógica para filtrar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_item, menu)

        val item = menu?.findItem(R.id.search_action)

        val searchView = item?.actionView as SearchView

        searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                tempArrayList.clear()

                val searchText = p0!!.toLowerCase(Locale.getDefault())

                if(categoria_mascota == "Filtrar - Todos"){
                    tempArrayList.clear()

                    val searchText = p0!!.toLowerCase(Locale.getDefault())

                    if (searchText.isNotEmpty()){
                        servicioArrayList.forEach{
                            if(it.nombre?.toLowerCase(Locale.getDefault())!!.contains(searchText)){
                                tempArrayList.add(it)
                            }
                        }
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }else{
                        tempArrayList.clear()
                        tempArrayList.addAll(servicioArrayList)
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                }

                if(categoria_mascota != "Filtrar - Todos"){
                    tempArrayList.clear()

                    val searchText = p0!!.toLowerCase(Locale.getDefault())

                    if (searchText.isNotEmpty()){
                        servicioArrayList.forEach{
                            if(it.nombre?.toLowerCase(Locale.getDefault())!!.contains(searchText) && it.categoria?.contains(categoria_mascota) == true){
                                tempArrayList.add(it)
                            }
                        }
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }else{
                        FiltroCategoria(categoria_mascota)
                    }
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                tempArrayList.clear()

                val searchText = p0!!.toLowerCase(Locale.getDefault())

                if(categoria_mascota == "Filtrar - Todos"){
                    tempArrayList.clear()

                    val searchText = p0!!.toLowerCase(Locale.getDefault())

                    if (searchText.isNotEmpty()){
                        servicioArrayList.forEach{
                            if(it.nombre?.toLowerCase(Locale.getDefault())!!.contains(searchText)){
                                tempArrayList.add(it)
                            }
                        }
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }else{
                        tempArrayList.clear()
                        tempArrayList.addAll(servicioArrayList)
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                }

                if(categoria_mascota != "Filtrar - Todos"){
                    tempArrayList.clear()

                    val searchText = p0!!.toLowerCase(Locale.getDefault())

                    if (searchText.isNotEmpty()){
                        servicioArrayList.forEach{
                            if(it.nombre?.toLowerCase(Locale.getDefault())!!.contains(searchText) && it.categoria?.contains(categoria_mascota) == true){
                                tempArrayList.add(it)
                            }
                        }
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }else{
                        FiltroCategoria(categoria_mascota)
                    }
                }
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun EventChangeListener() {

        db = FirebaseFirestore.getInstance()

        db.collection("usuarios").document(correo.toString()).get().addOnSuccessListener{
            tipo_mascota_servicios_usuario.setText(it.get("tipo_mascota") as String?)

            if(tipo_mascota_servicios_usuario.text.toString() == "Gato y Perro"){
                db.collectionGroup("servicios")
                    .addSnapshotListener(object : EventListener<QuerySnapshot> {
                        override fun onEvent(
                            value: QuerySnapshot?,
                            error: FirebaseFirestoreException?
                        ) {
                            if(error != null){
                                Log.e("Firestore error", error.message.toString())
                                return
                            }
                            for (dc : DocumentChange in value?.documentChanges!!){
                                if(dc.type == DocumentChange.Type.ADDED){
                                    servicioArrayList.add(dc.document.toObject(Servicio::class.java))
                                }
                            }
                            tempArrayList.addAll(servicioArrayList)

                            myAdapter.notifyDataSetChanged()
                        }
                    })
            }else{
                db.collectionGroup("servicios").whereEqualTo("tipo", tipo_mascota_servicios_usuario.text.toString())
                    .addSnapshotListener(object : EventListener<QuerySnapshot> {
                        override fun onEvent(
                            value: QuerySnapshot?,
                            error: FirebaseFirestoreException?
                        ) {
                            if(error != null){
                                Log.e("Firestore error", error.message.toString())
                                return
                            }
                            for (dc : DocumentChange in value?.documentChanges!!){
                                if(dc.type == DocumentChange.Type.ADDED){
                                    servicioArrayList.add(dc.document.toObject(Servicio::class.java))
                                }
                            }
                            tempArrayList.addAll(servicioArrayList)

                            myAdapter.notifyDataSetChanged()
                        }
                    })
            }
        }
    }

    private fun FiltroCategoria( categoria : String){

        if(categoria == "Filtrar - Todos"){
            tempArrayList.clear()
            tempArrayList.addAll(servicioArrayList)
            recyclerView.adapter!!.notifyDataSetChanged()
        }

        if(categoria != "Filtrar - Todos"){
            if(categoria.isNotEmpty()){
                tempArrayList.clear()
                servicioArrayList.forEach{
                    if(it.categoria?.contains(categoria) == true){
                        tempArrayList.add(it)
                    }
                }
                recyclerView.adapter!!.notifyDataSetChanged()
            }else{
                tempArrayList.clear()
                tempArrayList.addAll(servicioArrayList)
                recyclerView.adapter!!.notifyDataSetChanged()
            }
        }

    }

}