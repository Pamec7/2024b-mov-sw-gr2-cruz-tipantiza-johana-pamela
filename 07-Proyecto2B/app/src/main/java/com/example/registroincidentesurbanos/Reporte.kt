package com.example.registroincidentesurbanos

data class Reporte(
    val id: Int = 0,
    val usuarioId: String,
    val titulo: String,
    val descripcion: String,
    val tipo: String,
    val fotoUri: String,
    val latitud: Double,
    val longitud: Double,
    val fecha: String
)