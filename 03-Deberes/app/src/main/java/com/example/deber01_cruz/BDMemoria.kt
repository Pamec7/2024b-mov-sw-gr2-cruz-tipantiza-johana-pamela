package com.example.deber01_cruz

object BDMemoria {
    val paises = mutableListOf<Pais>()
    var ultimoIdPais = 0
    var ultimoIdCiudad = 0

    init {
        // Datos iniciales
        paises.add(
            Pais(
                id = 1,
                nombre = "Ecuador",
                codigoISO = "EC",
                continente = "América",
                poblacion = 17_800_000,
                esMiembroONU = true
            )
        )
        ultimoIdPais = 1
    }

    fun agregarPais(pais: Pais) {
        paises.add(pais)
        ultimoIdPais = pais.id
    }

    fun eliminarPais(id: Int) {
        paises.removeIf { it.id == id }
    }

    fun actualizarPais(paisActualizado: Pais) {
        val index = paises.indexOfFirst { it.id == paisActualizado.id }
        if (index != -1) paises[index] = paisActualizado
    }

    fun agregarCiudad(paisId: Int, ciudad: Ciudad) {
        paises.find { it.id == paisId }?.ciudades?.add(ciudad)
        ultimoIdCiudad = ciudad.id
    }

    fun eliminarCiudad(paisId: Int, ciudadId: Int) {
            val pais = paises.find { it.id == paisId }
            pais?.ciudades?.removeIf { it.id == ciudadId }
        }

    fun actualizarCiudad(paisId: Int, ciudadActualizada: Ciudad) {
            val pais = paises.find { it.id == paisId }
            val index = pais?.ciudades?.indexOfFirst { it.id == ciudadActualizada.id }
            if (index != null && index != -1) {
                pais.ciudades[index] = ciudadActualizada
            }
        }

}