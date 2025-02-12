package com.example.registroincidentesurbanos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FormReportActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var currentPhotoUri: Uri
    private var selectedLocation: LatLng? = null

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_SELECT_LOCATION = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_report)
        dbHelper = DatabaseHelper(this)

        setupUI()
    }

    private fun setupUI() {
        // Botón para tomar foto
        findViewById<FloatingActionButton>(R.id.fab_take_photo).setOnClickListener {
            dispatchTakePictureIntent()
        }

        // Botón para seleccionar ubicación
        findViewById<Button>(R.id.btn_select_location).setOnClickListener {
            val intent = Intent(this, MapSelectorActivity::class.java)
            startActivityForResult(intent, REQUEST_SELECT_LOCATION)
        }

        // Botón guardar
        findViewById<Button>(R.id.btn_save_report).setOnClickListener {
            guardarReporte()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    currentPhotoUri = FileProvider.getUriForFile(
                        this,
                        "${packageName}.provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == RESULT_OK) {
                    findViewById<ImageView>(R.id.iv_photo_preview).setImageURI(currentPhotoUri)
                }
            }
            REQUEST_SELECT_LOCATION -> {
                if (resultCode == RESULT_OK) {
                    selectedLocation = data?.getParcelableExtra("selected_location")
                    selectedLocation?.let {
                        findViewById<TextView>(R.id.tv_location).text =
                            "Lat: ${it.latitude}, Lon: ${it.longitude}"
                    }
                }
            }
        }
    }

    private fun guardarReporte() {
        val titulo = findViewById<EditText>(R.id.et_title).text.toString()
        val descripcion = findViewById<EditText>(R.id.et_description).text.toString()
        val tipo = findViewById<Spinner>(R.id.spinner_type).selectedItem.toString()

        if (validarCampos(titulo, descripcion)) {
            val reporte = Reporte(
                usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                titulo = titulo,
                descripcion = descripcion,
                tipo = tipo,
                fotoUri = currentPhotoUri.toString(),
                latitud = selectedLocation?.latitude ?: 0.0,
                longitud = selectedLocation?.longitude ?: 0.0,
                fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
            )

            dbHelper.insertarReporte(reporte)
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun validarCampos(titulo: String, descripcion: String): Boolean {
        var valido = true
        if (titulo.isEmpty()) {
            findViewById<EditText>(R.id.et_title).error = "Campo obligatorio"
            valido = false
        }
        if (descripcion.isEmpty()) {
            findViewById<EditText>(R.id.et_description).error = "Campo obligatorio"
            valido = false
        }
        if (selectedLocation == null) {
            Toast.makeText(this, "Seleccione una ubicación", Toast.LENGTH_SHORT).show()
            valido = false
        }
        return valido
    }
}