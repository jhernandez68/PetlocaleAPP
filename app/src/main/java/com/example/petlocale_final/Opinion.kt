package com.example.petlocale_final

data class Opinion (var usuario:String ?= null,
                     var calificacion: String?= null,
                     var reseña:String ?= null,
                     val nit: String ?= null)