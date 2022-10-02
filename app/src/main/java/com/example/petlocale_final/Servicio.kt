package com.example.petlocale_final

data class Servicio (var nombre:String ?= null,
                     var tipo: String?= null,
                     var precio:String ?= null,
                     var descripcion: String?=null,
                     val nombre_veterinaria: String ?= null)