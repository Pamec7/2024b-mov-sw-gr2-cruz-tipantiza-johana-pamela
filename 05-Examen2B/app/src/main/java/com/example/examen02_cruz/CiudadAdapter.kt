package com.example.examen02_cruz

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CiudadAdapter(
    context: Context,
    private val ciudades: List<Ciudad>
) : ArrayAdapter<Ciudad>(context, R.layout.item_ciudad, ciudades) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_ciudad, parent, false)

        val ciudad = ciudades[position]

        view.findViewById<TextView>(R.id.tvNombre).text = ciudad.nombre
        view.findViewById<TextView>(R.id.tvPoblacion).text = "Población: ${ciudad.poblacion}"
        view.findViewById<TextView>(R.id.tvAltitud).text = "Altitud: ${ciudad.altitud} m"
        view.findViewById<TextView>(R.id.tvCoordenadas).text =
            "Coordenadas: ${"%.4f".format(ciudad.latitud)}, ${"%.4f".format(ciudad.longitud)}"

        return view
    }
}