package com.example.petlocale_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petlocale_final.adapter.VeterinariasAdapter
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.collections.ArrayList

class UsuarioMainVeterinarias : AppCompatActivity() {
    private lateinit var tempArrayList : ArrayList<Veterinaria>
    private lateinit var recyclerView: RecyclerView
    private lateinit var veterinariaArrayList: ArrayList<Veterinaria>
    private lateinit var myAdapter: VeterinariasAdapter
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario_main_veterinarias)

        recyclerView = findViewById(R.id.recyclerViewVeterinasList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        veterinariaArrayList = arrayListOf()
        tempArrayList = arrayListOf()


        myAdapter = VeterinariasAdapter(tempArrayList)

        recyclerView.adapter = myAdapter

        EventChangeListener()

    }


    //Lógica para buscar en la lista

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_item, menu)

        val item = menu?.findItem(R.id.search_action)

        val searchView = item?.actionView as androidx.appcompat.widget.SearchView

        searchView.setOnQueryTextListener( object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                tempArrayList.clear()

                val searchText = p0!!.toLowerCase(Locale.getDefault())

                if (searchText.isNotEmpty()){
                    veterinariaArrayList.forEach{
                        if(it.nombre?.toLowerCase(Locale.getDefault())!!.contains(searchText)){

                            tempArrayList.add(it)
                        }
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
                }else{
                    tempArrayList.clear()
                    tempArrayList.addAll(veterinariaArrayList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                tempArrayList.clear()

                val searchText = p0!!.toLowerCase(Locale.getDefault())

                if (searchText.isNotEmpty()){
                    veterinariaArrayList.forEach{
                        if(it.nombre?.toLowerCase(Locale.getDefault())!!.contains(searchText)){

                            tempArrayList.add(it)
                        }
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
                }else{
                    tempArrayList.clear()
                    tempArrayList.addAll(veterinariaArrayList)
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

        db.collection("veterinarias")
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
                            veterinariaArrayList.add(dc.document.toObject(Veterinaria::class.java))
                        }
                    }
                    tempArrayList.addAll(veterinariaArrayList)
                    myAdapter.notifyDataSetChanged()
                }
            })
    }
}