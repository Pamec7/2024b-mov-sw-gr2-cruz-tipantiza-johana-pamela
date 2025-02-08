package com.example.deber01_cruz

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FormPaisActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etCodigoISO: EditText
    private lateinit var etContinente: EditText
    private lateinit var etPoblacion: EditText
    private lateinit var cbEsMiembroONU: CheckBox
    private lateinit var btnGuardar: Button
    private var paisEditando: Pais? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_pais)

        etNombre = findViewById(R.id.etNombre)
        etCodigoISO = findViewById(R.id.etCodigoISO)
        etContinente = findViewById(R.id.etContinente)
        etPoblacion = findViewById(R.id.etPoblacion)
        cbEsMiembroONU = findViewById(R.id.cbEsMiembroONU)
        btnGuardar = findViewById(R.id.btnGuardar)

        val paisId = intent.getIntExtra("PAIS_ID", -1)
        if (paisId != -1) {
            paisEditando = BDMemoria.paises.find { it.id == paisId }
            paisEditando?.let { cargarDatosPais(it) }
        }

        btnGuardar.setOnClickListener {
            if (validarCampos()) guardarPais()
        }
    }

    private fun cargarDatosPais(pais: Pais) {
        etNombre.setText(pais.nombre)
        etCodigoISO.setText(pais.codigoISO)
        etContinente.setText(pais.continente)
        etPoblacion.setText(pais.poblacion.toString())
        cbEsMiembroONU.isChecked = pais.esMiembroONU
    }

    private fun validarCampos(): Boolean {
        var valido = true
        if (etNombre.text.isEmpty()) {
            etNombre.error = "Campo obligatorio"
            valido = false
        }
        if (etCodigoISO.text.isEmpty()) {
            etCodigoISO.error = "Campo obligatorio"
            valido = false
        }
        if (etContinente.text.isEmpty()) {
            etContinente.error = "Campo obligatorio"
            valido = false
        }
        if (etPoblacion.text.isEmpty()) {
            etPoblacion.error = "Campo obligatorio"
            valido = false
        }
        return valido
    }

    private fun guardarPais() {
        val nuevoPais = Pais(
            id = paisEditando?.id ?: (BDMemoria.ultimoIdPais + 1),
            nombre = etNombre.text.toString(),
            codigoISO = etCodigoISO.text.toString(),
            continente = etContinente.text.toString(),
            poblacion = etPoblacion.text.toString().toInt(),
            esMiembroONU = cbEsMiembroONU.isChecked
        )

        if (paisEditando == null) {
            BDMemoria.agregarPais(nuevoPais)
        } else {
            BDMemoria.actualizarPais(nuevoPais)
        }

        setResult(RESULT_OK)
        Toast.makeText(this, "País guardado", Toast.LENGTH_SHORT).show()
        finish()
    }
}