package com.example.reporteincidentesurbanos

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reporteincidentesurbanos.database.DatabaseHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListaReportesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReporteAdapter
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var reportes: MutableList<Reporte>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_reportes)

        // Botón flotante para nuevo reporte
        val fabNuevoReporte = findViewById<FloatingActionButton>(R.id.fabNuevoReporte)
        fabNuevoReporte.setOnClickListener {
            startActivity(Intent(this, AgregarReporteActivity::class.java))
        }

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewReportes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        cargarReportes()
    }

    private fun cargarReportes() {
        val reportesTemp = dbHelper.obtenerReportes()

        if (reportesTemp.isNullOrEmpty()) {
            reportes = mutableListOf() // Evita un crash si la lista es nula
        } else {
            reportes = reportesTemp.toMutableList()
        }

        adapter = ReporteAdapter(this, reportes, dbHelper)
        recyclerView.adapter = adapter
    }


    override fun onResume() {
        super.onResume()
        cargarReportes()
    }
}
