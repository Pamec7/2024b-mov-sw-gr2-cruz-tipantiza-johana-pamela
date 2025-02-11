package com.example.examen2_cruz

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var lvPaises: ListView
    private lateinit var btnCrearPais: Button
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("Registro de Países")
        dbHelper = DatabaseHelper(this)

        lvPaises = findViewById(R.id.lvPaises)
        btnCrearPais = findViewById(R.id.btnCrearPais)

        actualizarLista()

        btnCrearPais.setOnClickListener {
            startActivityForResult(Intent(this, FormPaisActivity::class.java), 1)
        }

        registerForContextMenu(lvPaises)
    }

    private fun actualizarLista() {
        val paises = dbHelper.obtenerTodosPaises()
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            paises.map { "${it.nombre} (${it.codigoISO})" }
        )
        lvPaises.adapter = adapter
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_contextual_pais, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val pais = dbHelper.obtenerTodosPaises()[info.position]

        return when (item.itemId) {
            R.id.menu_editar -> {
                val intent = Intent(this, FormPaisActivity::class.java).apply {
                    putExtra("PAIS_ID", pais.id)
                }
                startActivityForResult(intent, 2)
                true
            }
            R.id.menu_eliminar -> {
                AlertDialog.Builder(this)
                    .setTitle("Eliminar país")
                    .setMessage("¿Eliminar ${pais.nombre}?")
                    .setPositiveButton("Sí") { _, _ ->
                        dbHelper.eliminarPais(pais.id)
                        actualizarLista()
                        Snackbar.make(lvPaises, "País eliminado", Snackbar.LENGTH_LONG).show()
                    }
                    .setNegativeButton("No", null)
                    .show()
                true
            }
            R.id.menu_ver_ciudades -> {
                val intent = Intent(this, CiudadesActivity::class.java).apply {
                    putExtra("PAIS_ID", pais.id)
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