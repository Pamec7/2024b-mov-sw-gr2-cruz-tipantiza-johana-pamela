package com.example.gr2sw2024b_jpct

class BBaseDatosMemoria {
    companion object {
        var arregloBEntrenador = arrayListOf<BEntrenador>()

        init {
            arregloBEntrenador.add(BEntrenador(1, "Pamela", "a@a.com"))
            arregloBEntrenador.add(BEntrenador(2, "Johana", "c@a.com"))
            arregloBEntrenador.add(BEntrenador(3, "Vicente", "d@a.com"))
        }
    }
}