package com.example.examen2_cruz

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class FormPaisActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etCodigoISO: EditText
    private lateinit var etContinente: EditText
    private lateinit var etPoblacion: EditText
    private lateinit var cbEsMiembroONU: CheckBox
    private lateinit var btnGuardar: Button
    private lateinit var dbHelper: DatabaseHelper
    private var paisEditando: Pais? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_pais)
        dbHelper = DatabaseHelper(this)

        etNombre = findViewById(R.id.etNombre)
        etCodigoISO = findViewById(R.id.etCodigoISO)
        etContinente = findViewById(R.id.etContinente)
        etPoblacion = findViewById(R.id.etPoblacion)
        cbEsMiembroONU = findViewById(R.id.cbEsMiembroONU)
        btnGuardar = findViewById(R.id.btnGuardar)

        val paisId = intent.getIntExtra("PAIS_ID", -1)
        if (paisId != -1) {
            paisEditando = dbHelper.obtenerTodosPaises().find { it.id == paisId }
            paisEditando?.let {
                etNombre.setText(it.nombre)
                etCodigoISO.setText(it.codigoISO)
                etContinente.setText(it.continente)
                etPoblacion.setText(it.poblacion.toString())
                cbEsMiembroONU.isChecked = it.esMiembroONU
            }
        }

        btnGuardar.setOnClickListener {
            if (validarCampos()) guardarPais()
        }
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
            id = paisEditando?.id ?: 0,
            nombre = etNombre.text.toString(),
            codigoISO = etCodigoISO.text.toString(),
            continente = etContinente.text.toString(),
            poblacion = etPoblacion.text.toString().toInt(),
            esMiembroONU = cbEsMiembroONU.isChecked
        )

        if (paisEditando == null) {
            dbHelper.insertarPais(nuevoPais)
        } else {
            // Implementar actualización si es necesario
        }

        Snackbar.make(btnGuardar, "País guardado", Snackbar.LENGTH_SHORT).show()
        setResult(RESULT_OK)
        finish()
    }
}