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
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.adapter.ProductosAdapter
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.activity_usuario_main_productos_comparacion.*
import java.util.*
import kotlin.collections.ArrayList

class UsuarioMainProductosComparacion : AppCompatActivity() {
    //Variables para hacer el recyclerview
    private lateinit var tempArrayList : ArrayList<Productos>
    private lateinit var recyclerView: RecyclerView
    private lateinit var productoArrayList: ArrayList<Productos>
    private lateinit var myAdapter: ProductosAdapter
    private lateinit var db: FirebaseFirestore


    //Variable para guardar el correo de usuario
    private lateinit var correo : String

    //Tipos de mascota
    var categorias = arrayOf("Filtrar - Todos", "Medicina", "Accesorios", "Juguetes", "Ropa","Alimentos", "Higiene", "Limpieza" )

    //Variable para guardar la categoria
    private lateinit var categoria_mascota : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario_main_productos_comparacion)

        //Se obtiene el email
        val objetoIntent: Intent = intent

        var email = objetoIntent.getStringExtra("email")

        var nombre_producto1 = objetoIntent.getStringExtra("nombre_producto1")

        var nombre_veterinaria1 = objetoIntent.getStringExtra("nombre_veterinaria1")

        var nit_producto1 = objetoIntent.getStringExtra("nit_producto1")

        var categoria_producto1 = objetoIntent.getStringExtra("categoria_producto1")

        correo = email.toString()

        recyclerView = findViewById(R.id.recyclerViewComparacionProductos)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        productoArrayList = arrayListOf()

        tempArrayList = arrayListOf()

        myAdapter = ProductosAdapter(tempArrayList)

        recyclerView.adapter = myAdapter

        Toast.makeText(this, "Selecciona un producto para comparar!", Toast.LENGTH_LONG).show()

        //Spinner - categorias
        val spinnerCategoria = findViewById<Spinner>(R.id.spinnerComparacionProducto)
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

        searchCategoriaComparacion.setOnClickListener {
            FiltroCategoria(categoria_mascota)
        }


        myAdapter.setOnClickItemListener(object : ProductosAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                var nombre_veterinaria = tempArrayList[position].nombre_veterinaria
                var nombre_producto = tempArrayList[position].nombre
                var nit_product = tempArrayList[position].nit
                var categoria_mascota = tempArrayList[position].categoria

                Log.d("Datos", "c1 : $categoria_mascota c2: $categoria_producto1")

                if(nombre_veterinaria1 == nombre_veterinaria
                    && nombre_producto1 == nombre_producto
                    && nit_producto1 == nit_product){
                    Toast.makeText(this@UsuarioMainProductosComparacion, "No puedes comparar el mismo producto!", Toast.LENGTH_LONG).show()
                }
                else if(categoria_mascota != categoria_producto1){
                    Toast.makeText(this@UsuarioMainProductosComparacion, "La categoria tiene que ser igual!", Toast.LENGTH_LONG).show()
                }
                else {
                    val intent = Intent(this@UsuarioMainProductosComparacion, ComparacionProductos::class.java)
                    //Se envian los datos del producto 1
                    intent.putExtra("nombre_veterinaria1", nombre_veterinaria1)
                    intent.putExtra("nombre_producto1", nombre_producto1)
                    intent.putExtra("nit_producto1", nit_producto1)

                    //Se envian los datos del producto 2
                    intent.putExtra("nombre_veterinaria2", nombre_veterinaria)
                    intent.putExtra("nombre_producto2", nombre_producto)
                    intent.putExtra("nit_producto2", nit_product)
                    intent.putExtra("email", email)

                    startActivity(intent) }

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
                if(categoria_mascota == "Filtrar - Todos"){
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
                }

                if(categoria_mascota != "Filtrar - Todos"){
                    tempArrayList.clear()

                    val searchText = p0!!.toLowerCase(Locale.getDefault())

                    if (searchText.isNotEmpty()){
                        productoArrayList.forEach{
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

                if(categoria_mascota == "Filtrar - Todos"){
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
                }

                if(categoria_mascota != "Filtrar - Todos"){
                    tempArrayList.clear()

                    val searchText = p0!!.toLowerCase(Locale.getDefault())

                    if (searchText.isNotEmpty()){
                        productoArrayList.forEach{
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
            tipo_mascota_comparacion_productos.setText(it.get("tipo_mascota") as String?)

            if(tipo_mascota_comparacion_productos.text.toString() == "Gato y Perro"){
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
                db.collectionGroup("productos")
                    .whereEqualTo("tipo", tipo_mascota_comparacion_productos.text.toString())
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

    private fun FiltroCategoria( categoria : String){

        if(categoria == "Filtrar - Todos"){
            tempArrayList.clear()
            tempArrayList.addAll(productoArrayList)
            recyclerView.adapter!!.notifyDataSetChanged()
        }

        if(categoria != "Filtrar - Todos"){
            if(categoria.isNotEmpty()){
                tempArrayList.clear()
                productoArrayList.forEach{
                    if(it.categoria?.contains(categoria) == true){
                        tempArrayList.add(it)
                    }
                }
                recyclerView.adapter!!.notifyDataSetChanged()
            }else{
                tempArrayList.clear()
                tempArrayList.addAll(productoArrayList)
                recyclerView.adapter!!.notifyDataSetChanged()
            }
        }

    }
}