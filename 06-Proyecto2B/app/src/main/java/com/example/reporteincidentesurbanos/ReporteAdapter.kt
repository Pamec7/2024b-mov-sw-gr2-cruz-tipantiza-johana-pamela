package com.example.reporteincidentesurbanos

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.reporteincidentesurbanos.database.DatabaseHelper

class ReporteAdapter(
    private val context: Context,
    private val reportes: MutableList<Reporte>,
    private val dbHelper: DatabaseHelper
    ) : RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReporteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_reporte, parent, false)
        return ReporteViewHolder(view)
    }

    class ReporteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvTitulo)
        val tvTipo: TextView = view.findViewById(R.id.tvTipo)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
        val tvUbicacion: TextView = view.findViewById(R.id.tvUbicacion)
        val tvFecha: TextView = view.findViewById(R.id.tvFecha)
        val btnOpciones: ImageView = view.findViewById(R.id.btnOpciones)
    }

    override fun onBindViewHolder(holder: ReporteViewHolder, position: Int) {
        val reporte = reportes[position]
        holder.tvTitulo.text = reporte.titulo
        holder.tvTipo.text = reporte.tipo
        holder.tvDescripcion.text = reporte.descripcion
        holder.tvUbicacion.text = "Ubicación: ${reporte.latitud}, ${reporte.longitud}"
        holder.tvFecha.text = "Fecha: ${reporte.fecha}"

        holder.btnOpciones.setOnClickListener {
            val popup = PopupMenu(context, holder.btnOpciones)
            popup.menuInflater.inflate(R.menu.menu_reporte, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_ver_detalles -> {
                        val intent = Intent(context, DetalleReporteActivity::class.java).apply {
                            putExtra("titulo", reporte.titulo)
                            putExtra("descripcion", reporte.descripcion)
                            putExtra("tipo", reporte.tipo)
                            putExtra("fecha", reporte.fecha)
                            putExtra("foto", reporte.foto)
                            putExtra("latitud", reporte.latitud)
                            putExtra("longitud", reporte.longitud)
                        }
                        context.startActivity(intent)
                        true
                    }
                    R.id.menu_editar -> {
                        val intent = Intent(context, AgregarReporteActivity::class.java).apply {
                            putExtra("MODO_EDICION", true)
                            putExtra("ID_REPORTE", reporte.id)
                        }
                        context.startActivity(intent)
                        true
                    }

                    R.id.menu_eliminar -> {
                        AlertDialog.Builder(context)
                            .setTitle("Eliminar Reporte")
                            .setMessage("¿Estás seguro de eliminar este reporte?")
                            .setPositiveButton("Eliminar") { _, _ ->
                                dbHelper.eliminarReporte(reporte.id)
                                reportes.removeAt(position)
                                notifyItemRemoved(position)
                            }
                            .setNegativeButton("Cancelar", null)
                            .show()
                        true
                    }

                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int {
        return reportes.size
    }

    private fun eliminarReporte(reporte: Reporte, position: Int) {
        dbHelper.eliminarReporte(reporte.id)  // Elimina de la base de datos
        reportes.removeAt(position)  // Elimina de la lista local
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)  // Actualiza la vista
        Toast.makeText(context, "Reporte eliminado", Toast.LENGTH_SHORT).show()
    }

    // Método para editar reporte
    private fun editarReporte(reporte: Reporte) {
        Toast.makeText(context, "Editar reporte: ${reporte.titulo}", Toast.LENGTH_SHORT).show()
    }
}




