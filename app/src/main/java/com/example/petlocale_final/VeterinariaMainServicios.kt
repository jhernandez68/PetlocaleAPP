package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.adapter.ServiciosAdapter
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.activity_veterinaria_main_productos.*
import kotlinx.android.synthetic.main.activity_veterinaria_main_servicios.*
import java.util.*
import kotlin.collections.ArrayList

class VeterinariaMainServicios : AppCompatActivity() {

    private lateinit var tempArrayList : ArrayList<Servicio>
    private lateinit var recyclerView: RecyclerView
    private lateinit var servicioArrayList: ArrayList<Servicio>
    private lateinit var myAdapter: ServiciosAdapter
    private lateinit var db: FirebaseFirestore

    private lateinit var Nombre2 : String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinaria_main_servicios)


        //Se obtiene el nit
        val objetoIntent: Intent = intent

        var Nombre = objetoIntent.getStringExtra("Nombre")

        Nombre2 = Nombre.toString()

        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        servicioArrayList = arrayListOf()

        tempArrayList = arrayListOf()

        myAdapter = ServiciosAdapter(tempArrayList)

        recyclerView.adapter = myAdapter

        EventChangeListener()

        addServiceButton.setOnClickListener {
            startActivity(Intent(this, AddService::class.java).putExtra("nit",  Nombre))
        }

        deleteServiceButton.setOnClickListener{
            startActivity(Intent(this, DeleteService::class.java).putExtra("nit",  Nombre))
        }

        textView5.setOnClickListener{
            startActivity(Intent(this, VeterinariaMain::class.java).putExtra("nit",  Nombre))
        }
    }


    //Lógica para filtrar
    //Lógica para filtrar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_item, menu)

        val item = menu?.findItem(R.id.search_action)

        val searchView = item?.actionView as SearchView

        searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(p0: String?): Boolean {
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

                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    //Lógica de FB
    private fun EventChangeListener() {

        db = FirebaseFirestore.getInstance()

        db.collection("veterinarias").document(Nombre2).collection("servicios")
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
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