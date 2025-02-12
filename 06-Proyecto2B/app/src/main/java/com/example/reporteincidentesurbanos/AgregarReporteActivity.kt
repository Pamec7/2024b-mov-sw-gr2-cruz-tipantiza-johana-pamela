package com.example.reporteincidentesurbanos

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.reporteincidentesurbanos.database.DatabaseHelper
import java.util.*

class AgregarReporteActivity : AppCompatActivity() {
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0
    private var imageUri: Uri? = null
    private lateinit var imageView: ImageView
    private lateinit var spinner: Spinner
    private lateinit var dbHelper: DatabaseHelper

    private val REQUEST_GALLERY = 2
    private var idReporte = 0
    private var modoEdicion = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_reporte)

        // Inicializar DBHelper
        dbHelper = DatabaseHelper(this)

        // Verificar si es modo edición
        modoEdicion = intent.getBooleanExtra("MODO_EDICION", false)
        idReporte = intent.getIntExtra("ID_REPORTE", 0)

        // Inicializar vistas
        imageView = findViewById(R.id.ivImagen)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarReporte)
        val btnUbicacion = findViewById<Button>(R.id.btnSeleccionarUbicacion)
        val btnFoto = findViewById<Button>(R.id.btnSeleccionarFoto)

        val etTitulo = findViewById<EditText>(R.id.etTitulo)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val tvUbicacion = findViewById<TextView>(R.id.tvUbicacionSeleccionada)

        // Configurar Spinner
        spinner = findViewById(R.id.spTipoReporte)
        val tipos = arrayOf("Incidente", "Reclamo", "Sugerencia")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Si es modo edición, cargar los datos
        if (modoEdicion) {
            cargarDatosExistentes()
            btnGuardar.text = "Actualizar Reporte"
        }

        // Botón para seleccionar ubicación
        btnUbicacion.setOnClickListener {
            val intent = Intent(this, MapaActivity::class.java)
            startActivityForResult(intent, 1)
        }

        // Botón para seleccionar foto de la galería

                btnFoto.setOnClickListener {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "image/*"

                        // Intentar abrir la carpeta de Descargas
                        val downloadsUri = DocumentsContract.buildRootUri(
                            "com.android.externalstorage.documents", "primary:Download"
                        )
                        putExtra(DocumentsContract.EXTRA_INITIAL_URI, downloadsUri)
                    }
                    startActivityForResult(intent, REQUEST_GALLERY)
                }



        // Botón Guardar/Actualizar
        btnGuardar.setOnClickListener {
            val titulo = etTitulo.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()
            val tipo = spinner.selectedItem.toString()
            val fecha = if (modoEdicion) obtenerFechaExistente() else obtenerFechaActual()

            if (titulo.isEmpty() || descripcion.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val reporte = Reporte(
                idReporte,  // Mantener el ID si es edición
                titulo,
                descripcion,
                tipo,
                imageUri?.toString() ?: "",
                latitud,
                longitud,
                fecha
            )

            if (modoEdicion) {
                dbHelper.actualizarReporte(reporte)
                Toast.makeText(this, "Reporte actualizado", Toast.LENGTH_SHORT).show()
            } else {
                dbHelper.agregarReporte(reporte)
                Toast.makeText(this, "Reporte guardado", Toast.LENGTH_SHORT).show()
            }

            startActivity(Intent(this, ListaReportesActivity::class.java))
            finish()
        }
    }

    private fun obtenerFechaActual(): String {
        return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
    }

    private fun obtenerFechaExistente(): String {
        return dbHelper.obtenerReportePorId(idReporte)?.fecha ?: obtenerFechaActual()
    }

    private fun cargarDatosExistentes() {
        val reporte = dbHelper.obtenerReportePorId(idReporte) ?: return

        findViewById<EditText>(R.id.etTitulo).setText(reporte.titulo)
        findViewById<EditText>(R.id.etDescripcion).setText(reporte.descripcion)

        // Asegurar que el Spinner se actualiza correctamente
        val adapter = spinner.adapter as ArrayAdapter<String>
        val position = adapter.getPosition(reporte.tipo)
        if (position >= 0) spinner.setSelection(position)

        latitud = reporte.latitud
        longitud = reporte.longitud
        findViewById<TextView>(R.id.tvUbicacionSeleccionada).text =
            "Ubicación: ${"%.4f".format(latitud)}, ${"%.4f".format(longitud)}"

        if (!reporte.foto.isNullOrEmpty()) {
            imageUri = Uri.parse(reporte.foto)
            imageView.setImageURI(imageUri)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_GALLERY -> {
                    imageUri = data?.data
                    imageView.setImageURI(imageUri)// Permiso persistente para acceder a la imagen después del reinicio
                    imageUri?.let { uri ->
                        contentResolver.takePersistableUriPermission(
                            uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    }
                }
                1 -> { // Selección de ubicación
                    latitud = data?.getDoubleExtra("latitud", 0.0) ?: 0.0
                    longitud = data?.getDoubleExtra("longitud", 0.0) ?: 0.0
                    findViewById<TextView>(R.id.tvUbicacionSeleccionada).text =
                        "Ubicación: ${"%.4f".format(latitud)}, ${"%.4f".format(longitud)}"
                }
            }
        }
    }
}
