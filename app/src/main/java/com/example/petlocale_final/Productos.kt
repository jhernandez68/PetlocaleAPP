package com.example.petlocale_final

data class Productos(val nombre:String ?= null,
                     val tipo: String ?= null,
                     val precio: String ?= null,
                     val cantidad : String ?= null,
                     val descripcion : String ?= null,
                     val categoria : String ?= null,
                     val nombre_veterinaria: String ?= null) {

}
