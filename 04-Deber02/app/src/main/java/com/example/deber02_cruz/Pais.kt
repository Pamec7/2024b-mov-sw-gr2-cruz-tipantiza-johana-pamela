package com.example.deber01_cruz

data class Pais(
    var id: Int,
    var nombre: String,
    var codigoISO: String,
    var continente: String,
    var poblacion: Int,
    var esMiembroONU: Boolean,
    val ciudades: MutableList<Ciudad> = mutableListOf()
)