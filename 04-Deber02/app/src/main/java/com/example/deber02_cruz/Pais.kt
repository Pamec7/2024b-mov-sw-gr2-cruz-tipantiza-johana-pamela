package com.example.deber02_cruz

data class Pais(
    val id: Int = 0,
    val nombre: String,
    val codigoISO: String,
    val continente: String,
    val poblacion: Int,
    val esMiembroONU: Boolean
)