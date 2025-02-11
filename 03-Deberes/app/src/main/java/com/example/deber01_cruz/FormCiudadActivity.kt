package com.example.deber01_cruz

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

class FormCiudadActivity : AppCompatActivity() {

    private lateinit var etNombreCiudad: EditText
    private lateinit var etPoblacionCiudad: EditText
    private lateinit var etAltitudCiudad: EditText
    private lateinit var etFechaFundacionCiudad: EditText
    private lateinit var cbEsCapital: CheckBox
    private lateinit var btnGuardarCiudad: Button
    private lateinit var pais: Pais
    private var ciudadEditando: Ciudad? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_ciudad)

        val paisId = intent.getIntExtra("PAIS_ID", -1)
        val ciudadId = intent.getIntExtra("CIUDAD_ID", -1)
        pais = BDMemoria.paises.find { it.id == paisId }!!

        // Inicializar vistas
        etNombreCiudad = findViewById(R.id.etNombreCiudad)
        etPoblacionCiudad = findViewById(R.id.etPoblacionCiudad)
        etAltitudCiudad = findViewById(R.id.etAltitudCiudad)
        etFechaFundacionCiudad = findViewById(R.id.etFechaFundacionCiudad)
        cbEsCapital = findViewById(R.id.cbEsCapital)
        btnGuardarCiudad = findViewById(R.id.btnGuardarCiudad)

        // Si es edición, cargar datos
        if (ciudadId != -1) {
            ciudadEditando = pais.ciudades.find { it.id == ciudadId }
            ciudadEditando?.let {
                etNombreCiudad.setText(it.nombre)
                etPoblacionCiudad.setText(it.poblacion.toString())
                etAltitudCiudad.setText(it.altitud.toString())
                etFechaFundacionCiudad.setText(it.fechaFundacion)
                cbEsCapital.isChecked = it.esCapital
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
        return valido
    }

    private fun guardarCiudad() {
        val nuevaCiudad = Ciudad(
            id = ciudadEditando?.id ?: (BDMemoria.ultimoIdCiudad + 1),
            nombre = etNombreCiudad.text.toString(),
            poblacion = etPoblacionCiudad.text.toString().toInt(),
            altitud = etAltitudCiudad.text.toString().toDouble(),
            fechaFundacion = etFechaFundacionCiudad.text.toString(),
            esCapital = cbEsCapital.isChecked
        )

        if (ciudadEditando == null) {
            BDMemoria.agregarCiudad(pais.id, nuevaCiudad)
        } else {
            BDMemoria.actualizarCiudad(pais.id, nuevaCiudad)
        }

        setResult(RESULT_OK)
        Toast.makeText(this, "Ciudad guardada", Toast.LENGTH_SHORT).show()

        Snackbar.make(btnGuardarCiudad, "Ciudad guardada exitosamente", Snackbar.LENGTH_SHORT).show()

        finish()
    }
}