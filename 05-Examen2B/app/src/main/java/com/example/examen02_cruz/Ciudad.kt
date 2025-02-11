package com.example.examen02_cruz

data class Ciudad(
    val id: Int = 0,
    val nombre: String,
    val poblacion: Int,
    val altitud: Double,
    val fechaFundacion: String,
    val esCapital: Boolean,
    val latitud: Double,
    val longitud: Double
)