package com.example.reporteincidentesurbanos

data class Reporte(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val tipo: String,
    val foto: String?,
    val latitud: Double,
    val longitud: Double,
    val fecha: String
)
