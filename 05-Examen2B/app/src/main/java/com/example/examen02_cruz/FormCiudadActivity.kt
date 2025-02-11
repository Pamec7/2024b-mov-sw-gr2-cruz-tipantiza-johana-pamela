package com.example.examen02_cruz

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class FormCiudadActivity : AppCompatActivity() {

    private lateinit var etNombreCiudad: EditText
    private lateinit var etPoblacionCiudad: EditText
    private lateinit var etAltitudCiudad: EditText
    private lateinit var etFechaFundacionCiudad: EditText
    private lateinit var cbEsCapital: CheckBox
    private lateinit var btnGuardarCiudad: Button
    private lateinit var dbHelper: DatabaseHelper
    private var ciudadEditando: Ciudad? = null
    private lateinit var pais: Pais
    private lateinit var etLatitud: EditText
    private lateinit var etLongitud: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_ciudad)
        dbHelper = DatabaseHelper(this)

        // Obtener ID del país y ciudad
        val paisId = intent.getIntExtra("PAIS_ID", -1)
        val ciudadId = intent.getIntExtra("CIUDAD_ID", -1)
        pais = dbHelper.obtenerTodosPaises().find { it.id == paisId }!!

        // Inicializar vistas
        etNombreCiudad = findViewById(R.id.etNombreCiudad)
        etPoblacionCiudad = findViewById(R.id.etPoblacionCiudad)
        etAltitudCiudad = findViewById(R.id.etAltitudCiudad)
        etFechaFundacionCiudad = findViewById(R.id.etFechaFundacionCiudad)
        cbEsCapital = findViewById(R.id.cbEsCapital)
        btnGuardarCiudad = findViewById(R.id.btnGuardarCiudad)
        etLatitud = findViewById(R.id.etLatitud)
        etLongitud = findViewById(R.id.etLongitud)

        if (ciudadId != -1) {
            ciudadEditando = dbHelper.obtenerCiudadesDePais(pais.id).find { it.id == ciudadId }
            ciudadEditando?.let {
                etNombreCiudad.setText(it.nombre)
                etPoblacionCiudad.setText(it.poblacion.toString())
                etAltitudCiudad.setText(it.altitud.toString())
                etFechaFundacionCiudad.setText(it.fechaFundacion)
                cbEsCapital.isChecked = it.esCapital
                etLatitud.setText(it.latitud.toString())
                etLongitud.setText(it.longitud.toString())
            }
        }

        btnGuardarCiudad.setOnClickListener {
            if (validarCampos()) guardarCiudad()
        }
    }

    private fun validarCampos(): Boolean {
        var valido = true
        if (etNombreCiudad.text.isEmpty()) {
            etNombreCiudad.error = "Campo obligatorio"
            valido = false
        }
        if (etPoblacionCiudad.text.isEmpty()) {
            etPoblacionCiudad.error = "Campo obligatorio"
            valido = false
        }
        if (etLatitud.text.isEmpty()) {
            etLatitud.error = "Campo obligatorio"
            valido = false
        }
        if (etLongitud.text.isEmpty()) {
            etLongitud.error = "Campo obligatorio"
            valido = false
        }
        return valido
    }

    private fun guardarCiudad() {
        val latitud = etLatitud.text.toString().toDoubleOrNull() ?: 0.0
        val longitud = etLongitud.text.toString().toDoubleOrNull() ?: 0.0

        // Validar rangos geográficos
        if (latitud < -90 || latitud > 90 || longitud < -180 || longitud > 180) {
            Toast.makeText(this, "Coordenadas inválidas:\nLat: [-90, 90]\nLon: [-180, 180]", Toast.LENGTH_LONG).show()
            return
        }

        val nuevaCiudad = Ciudad(
            id = ciudadEditando?.id ?: 0,
            nombre = etNombreCiudad.text.toString(),
            poblacion = etPoblacionCiudad.text.toString().toInt(),
            altitud = etAltitudCiudad.text.toString().toDouble(),
            fechaFundacion = etFechaFundacionCiudad.text.toString(),
            esCapital = cbEsCapital.isChecked,
            latitud = latitud,
            longitud = longitud
        )

        if (ciudadEditando == null) {
            dbHelper.insertarCiudad(nuevaCiudad, pais.id)
        } else {
            dbHelper.actualizarCiudad(nuevaCiudad)
        }

        Snackbar.make(btnGuardarCiudad, "Ciudad guardada", Snackbar.LENGTH_SHORT).show()
        setResult(RESULT_OK)
        finish()
    }
}