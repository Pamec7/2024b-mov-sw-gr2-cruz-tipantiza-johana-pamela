package com.example.reporteincidentesurbanos

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DetalleReporteActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_reporte)

        // Obtener datos del intent
        val titulo = intent.getStringExtra("titulo") ?: ""
        val descripcion = intent.getStringExtra("descripcion") ?: ""
        val tipo = intent.getStringExtra("tipo") ?: ""
        val fecha = intent.getStringExtra("fecha") ?: ""
        val fotoUri = intent.getStringExtra("foto") ?: ""
        latitud = intent.getDoubleExtra("latitud", 0.0)
        longitud = intent.getDoubleExtra("longitud", 0.0)

        // Configurar vistas
        findViewById<TextView>(R.id.tvTituloDetalle).text = titulo
        findViewById<TextView>(R.id.tvDescripcionDetalle).text = descripcion
        findViewById<TextView>(R.id.tvTipoDetalle).text = "Tipo: $tipo"
        findViewById<TextView>(R.id.tvFechaDetalle).text = "Fecha: $fecha"

        val imageView = findViewById<ImageView>(R.id.ivImagenDetalle)

        if (!fotoUri.isNullOrEmpty()) {
            try {
                val inputStream = contentResolver.openInputStream(Uri.parse(fotoUri))
                val bitmap = BitmapFactory.decodeStream(inputStream)
                imageView.setImageBitmap(bitmap)
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
                imageView.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        } else {
            imageView.setImageResource(android.R.drawable.ic_menu_gallery)

        }

        // Configurar mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapaReporte) as SupportMapFragment
        mapFragment.getMapAsync(this)
        verificarPermisos()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val ubicacion = LatLng(latitud, longitud)
        map.addMarker(MarkerOptions().position(ubicacion).title("Ubicación del reporte"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 17f))
    }

    private fun verificarPermisos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Para Android 13+
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), 100)
            }
        } else {
            // Para Android 6+ hasta Android 12
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 100)
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }

}
