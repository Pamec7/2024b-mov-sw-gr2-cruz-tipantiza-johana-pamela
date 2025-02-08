package com.example.deber01_cruz

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

class CiudadesActivity : AppCompatActivity() {

    private lateinit var tvPais: TextView
    private lateinit var lvCiudades: ListView
    private lateinit var btnNuevaCiudad: Button
    private lateinit var pais: Pais
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ciudades)

        val paisId = intent.getIntExtra("PAIS_ID", -1)
        pais = BDMemoria.paises.find { it.id == paisId }!!

        // Configurar vistas
        tvPais = findViewById(R.id.tvPais)
        lvCiudades = findViewById(R.id.lvCiudades)
        btnNuevaCiudad = findViewById(R.id.btnNuevaCiudad)

        tvPais.text = "Ciudades de: ${pais.nombre}"
        actualizarLista()

        // Botón para nueva ciudad
        btnNuevaCiudad.setOnClickListener {
            val intent = Intent(this, FormCiudadActivity::class.java).apply {
                putExtra("PAIS_ID", pais.id)
            }
            startActivityForResult(intent, 1) // Usar startActivityForResult
        }

        // Registrar menú contextual para ciudades
        registerForContextMenu(lvCiudades)


    }

    // Actualizar lista al regresar de FormCiudadActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) actualizarLista()
    }

    private fun actualizarLista() {
        val nombresCiudades = pais.ciudades.map { it.nombre }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nombresCiudades)
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
        val ciudad = pais.ciudades[info.position]

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
                        BDMemoria.eliminarCiudad(pais.id, ciudad.id)
                        actualizarLista()
                    }
                    .setNegativeButton("No", null)
                    .show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}