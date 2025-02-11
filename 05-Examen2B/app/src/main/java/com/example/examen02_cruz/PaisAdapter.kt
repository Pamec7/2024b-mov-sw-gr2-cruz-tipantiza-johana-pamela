package com.example.examen02_cruz

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import java.text.NumberFormat

class PaisAdapter(
    private val context: Context,
    private val paises: List<Pais>
) : ArrayAdapter<Pais>(context, R.layout.item_pais, paises) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_pais, parent, false)
        val pais = paises[position]

        view.findViewById<TextView>(R.id.tvNombrePais).text = pais.nombre
        view.findViewById<TextView>(R.id.tvCodigoISO).text = "ISO: ${pais.codigoISO}"
        view.findViewById<TextView>(R.id.tvContinente).text = "Continente: ${pais.continente}"
        view.findViewById<TextView>(R.id.tvPoblacionPais).text = "Población: ${NumberFormat.getInstance().format(pais.poblacion)}"
        view.findViewById<CheckBox>(R.id.cbONU).isChecked = pais.esMiembroONU

        return view
    }
}