package com.example.examen02_cruz

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class CiudadesActivity : AppCompatActivity() {

    private lateinit var tvPais: TextView
    private lateinit var lvCiudades: ListView
    private lateinit var btnNuevaCiudad: Button
    private lateinit var btnVolver: Button
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var pais: Pais
    private var ciudades = mutableListOf<Ciudad>()
    private lateinit var adapter: CiudadAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ciudades)
        dbHelper = DatabaseHelper(this)

        // Obtener ID del país
        val paisId = intent.getIntExtra("PAIS_ID", -1)
        pais = dbHelper.obtenerTodosPaises().find { it.id == paisId }!!

        // Configurar vistas
        tvPais = findViewById(R.id.tvPais)
        lvCiudades = findViewById(R.id.lvCiudades)
        btnNuevaCiudad = findViewById(R.id.btnNuevaCiudad)
        btnVolver = findViewById(R.id.btnVolverPais)

        tvPais.text = "Ciudades de: ${pais.nombre}"
        actualizarLista()

        // Botón para nueva ciudad
        btnNuevaCiudad.setOnClickListener {
            val intent = Intent(this, FormCiudadActivity::class.java).apply {
                putExtra("PAIS_ID", pais.id)
            }
            startActivityForResult(intent, 1)
        }

        // Botón para volver
        btnVolver.setOnClickListener {
            finish()
        }

        // Registrar menú contextual
        registerForContextMenu(lvCiudades)
    }

    private fun actualizarLista() {
        ciudades = dbHelper.obtenerCiudadesDePais(pais.id).toMutableList()
        adapter = CiudadAdapter(this, ciudades)
        lvCiudades.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    // Menú contextual para ciudades
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_contextual_ciudad, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val ciudad = ciudades[info.position]

        return when (item.itemId) {
            R.id.menu_editar_ciudad -> {
                val intent = Intent(this, FormCiudadActivity::class.java).apply {
                    putExtra("PAIS_ID", pais.id)
                    putExtra("CIUDAD_ID", ciudad.id)
                }
                startActivityForResult(intent, 2)
                true
            }
            R.id.menu_eliminar_ciudad -> {
                AlertDialog.Builder(this)
                    .setTitle("Eliminar ciudad")
                    .setMessage("¿Eliminar ${ciudad.nombre}?")
                    .setPositiveButton("Sí") { _, _ ->
                        dbHelper.eliminarCiudad(ciudad.id)
                        actualizarLista()
                        Snackbar.make(lvCiudades, "Ciudad eliminada", Snackbar.LENGTH_LONG)
                            .setAction("Deshacer") {
                                dbHelper.insertarCiudad(ciudad, pais.id)
                                actualizarLista()
                            }.show()
                    }
                    .setNegativeButton("No", null)
                    .show()
                true
            }
            R.id.menu_ver_mapa -> {
                val ciudad = ciudades[info.position]
                val intent = Intent(this, GGoogleMaps::class.java).apply {
                    putExtra("latitud", ciudad.latitud) // Key en minúscula
                    putExtra("longitud", ciudad.longitud)
                    putExtra("titulo", ciudad.nombre)
                }
                startActivity(intent)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) actualizarLista()
    }
}