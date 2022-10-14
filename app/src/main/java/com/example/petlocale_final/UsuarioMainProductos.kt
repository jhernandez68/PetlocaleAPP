package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.R.menu
import com.example.petlocale_final.adapter.ProductosAdapter
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.activity_usuario_main_productos.*
import kotlinx.android.synthetic.main.item_producto.*
import java.util.*
import kotlin.collections.ArrayList


class UsuarioMainProductos : AppCompatActivity() {

    //Variables para hacer el recyclerview
    private lateinit var tempArrayList : ArrayList<Productos>
    private lateinit var recyclerView: RecyclerView
    private lateinit var productoArrayList: ArrayList<Productos>
    private lateinit var myAdapter: ProductosAdapter
    private lateinit var db: FirebaseFirestore


    //Variable para guardar el correo de usuario
    private lateinit var correo : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario_main_productos)

        //Se obtiene el email
        val objetoIntent: Intent = intent

        var email = objetoIntent.getStringExtra("email")

        correo = email.toString()

        recyclerView = findViewById(R.id.recyclerViewUserProduct)

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

                val intent = Intent(this@UsuarioMainProductos, UsuarioMainProductosInfo::class.java)
                intent.putExtra("nombre_veterinaria", nombre_veterinaria)
                intent.putExtra("nombre_producto", nombre_producto)
                intent.putExtra("nit", nit_product)
                intent.putExtra("email", email)

                startActivity(intent)

            }

        })

        EventChangeListener()

    }

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

    private fun EventChangeListener() {

        db = FirebaseFirestore.getInstance()

        db.collection("usuarios").document(correo.toString()).get().addOnSuccessListener{
            tipo_mascota_productos_usuario.setText(it.get("tipo_mascota") as String?)

            if(tipo_mascota_productos_usuario.text.toString() == "Gato y Perro"){
                db.collectionGroup("productos")
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
            }else{
                db.collectionGroup("productos").whereEqualTo("tipo", tipo_mascota_productos_usuario.text.toString())
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



    }
}