package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.adapter.ProductosAdapter
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.activity_veterinaria_main_productos.*
import kotlinx.android.synthetic.main.activity_veterinaria_main_servicios.*
import java.util.*
import kotlin.collections.ArrayList

class VeterinariaMainProductos : AppCompatActivity() {

    private lateinit var tempArrayList : ArrayList<Productos>
    private lateinit var recyclerView: RecyclerView
    private lateinit var productoArrayList: ArrayList<Productos>
    private lateinit var myAdapter: ProductosAdapter
    private lateinit var db: FirebaseFirestore

    private lateinit var Nombre2 : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinaria_main_productos)

        //Se obtiene el nit
        val objetoIntent: Intent = intent

        var Nombre = objetoIntent.getStringExtra("Nombre")

        Nombre2 = Nombre.toString()

        recyclerView = findViewById(R.id.recyclerViewProduct)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        productoArrayList = arrayListOf()
        tempArrayList = arrayListOf()

        myAdapter = ProductosAdapter(tempArrayList)

        recyclerView.adapter = myAdapter

        myAdapter.setOnClickItemListener(object : ProductosAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                var nombre_veterinaria = tempArrayList[position].nombre_veterinaria
                var nombre_producto = tempArrayList[position].nombre
                var nit_product = tempArrayList[position].nit

                val intent = Intent(this@VeterinariaMainProductos, VeterinariaMainProductosDetailed::class.java)
                intent.putExtra("nombre_veterinaria", nombre_veterinaria)
                intent.putExtra("nombre_producto", nombre_producto)
                intent.putExtra("nit", nit_product)

                startActivity(intent)

            }

        })

        EventChangeListener()

        addProductButton.setOnClickListener {
            startActivity(Intent(this, AddProduct::class.java).putExtra("nit",  Nombre))
        }

        deleteProductButton.setOnClickListener{
            startActivity(Intent(this, DeleteProduct::class.java).putExtra("nit",  Nombre))
        }

        textView4.setOnClickListener{
            startActivity(Intent(this, VeterinariaMain::class.java).putExtra("nit",  Nombre))
        }
        
    }

    //Lógica para buscar en la lista

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_item, menu)

        val item = menu?.findItem(R.id.search_action)

        val searchView = item?.actionView as androidx.appcompat.widget.SearchView

        searchView.setOnQueryTextListener( object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                tempArrayList.clear()

                val searchText = p0!!.toLowerCase(Locale.getDefault())

                if (searchText.isNotEmpty()){
                    productoArrayList.forEach{
                        if(it.nombre?.toLowerCase(Locale.getDefault())!!.contains(searchText)){

                            tempArrayList.add(it)
                        }
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
                }else{
                    tempArrayList.clear()
                    tempArrayList.addAll(productoArrayList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }

                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    //Lógica para traer info de FB
    private fun EventChangeListener() {

        db = FirebaseFirestore.getInstance()

        db.collection("veterinarias").document(Nombre2).collection("productos")
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
                            productoArrayList.add(dc.document.toObject(Productos::class.java))
                        }
                    }
                    tempArrayList.addAll(productoArrayList)
                    myAdapter.notifyDataSetChanged()
                }
            })
    }
}