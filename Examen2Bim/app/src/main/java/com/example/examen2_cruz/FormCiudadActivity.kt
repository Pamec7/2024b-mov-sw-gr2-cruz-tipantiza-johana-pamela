package com.example.examen2_cruz

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class FormCiudadActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etPoblacion: EditText
    private lateinit var etAltitud: EditText
    private lateinit var etFechaFundacion: EditText
    private lateinit var etLatitud: EditText
    private lateinit var etLongitud: EditText
    private lateinit var cbEsCapital: CheckBox
    private lateinit var btnGuardar: Button
    private lateinit var dbHelper: DatabaseHelper
    private var ciudadEditando: Ciudad? = null
    private lateinit var pais: Pais

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_ciudad)
        dbHelper = DatabaseHelper(this)

        // Obtener ID del país
        val paisId = intent.getIntExtra("PAIS_ID", -1)
        pais = dbHelper.obtenerTodosPaises().find { it.id == paisId }!!

        // Vincular vistas
        etNombre = findViewById(R.id.etNombre)
        etPoblacion = findViewById(R.id.etPoblacion)
        etAltitud = findViewById(R.id.etAltitud)
        etFechaFundacion = findViewById(R.id.etFechaFundacion)
        etLatitud = findViewById(R.id.etLatitud)
        etLongitud = findViewById(R.id.etLongitud)
        cbEsCapital = findViewById(R.id.cbEsCapital)
        btnGuardar = findViewById(R.id.btnGuardar)

        // Cargar datos si es edición
        val ciudadId = intent.getIntExtra("CIUDAD_ID", -1)
        if (ciudadId != -1) {
            ciudadEditando = dbHelper.obtenerCiudadesDePais(pais.id).find { it.id == ciudadId }
            ciudadEditando?.let {
                etNombre.setText(it.nombre)
                etPoblacion.setText(it.poblacion.toString())
                etAltitud.setText(it.altitud.toString())
                etFechaFundacion.setText(it.fechaFundacion)
                etLatitud.setText(it.latitud.toString())
                etLongitud.setText(it.longitud.toString())
                cbEsCapital.isChecked = it.esCapital
            }
        }

        btnGuardar.setOnClickListener {
            if (validarCampos()) guardarCiudad()
        }
    }

    private fun validarCampos(): Boolean {
        var valido = true
        if (etNombre.text.isEmpty()) etNombre.error = "Campo obligatorio"
        if (etPoblacion.text.isEmpty()) etPoblacion.error = "Campo obligatorio"
        if (etLatitud.text.isEmpty()) etLatitud.error = "Campo obligatorio"
        if (etLongitud.text.isEmpty()) etLongitud.error = "Campo obligatorio"
        return valido
    }

    private fun guardarCiudad() {
        val nuevaCiudad = Ciudad(
            id = ciudadEditando?.id ?: 0,
            nombre = etNombre.text.toString(),
            poblacion = etPoblacion.text.toString().toInt(),
            altitud = etAltitud.text.toString().toDouble(),
            fechaFundacion = etFechaFundacion.text.toString(),
            esCapital = cbEsCapital.isChecked,
            latitud = etLatitud.text.toString().toDouble(),
            longitud = etLongitud.text.toString().toDouble()
        )

        if (ciudadEditando == null) {
            dbHelper.insertarCiudad(nuevaCiudad, pais.id)
        }
        Snackbar.make(btnGuardar, "Ciudad guardada", Snackbar.LENGTH_SHORT).show()
        setResult(RESULT_OK)
        finish()
    }
}