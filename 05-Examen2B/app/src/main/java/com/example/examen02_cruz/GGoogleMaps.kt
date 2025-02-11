package com.example.examen02_cruz

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.Manifest
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.examen02_cruz.databinding.ActivityGgoogleMapsBinding
import com.google.android.gms.maps.model.Marker

class GGoogleMaps : AppCompatActivity() {

    private lateinit var mapa: GoogleMap
    private val nombrePermisoFine = Manifest.permission.ACCESS_FINE_LOCATION
    private val nombrePermisoCoarse = Manifest.permission.ACCESS_COARSE_LOCATION
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ggoogle_maps)

        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish() // Regresa a CiudadesActivity
        }

        val latitud = intent.getDoubleExtra("latitud", 0.0)
        val longitud = intent.getDoubleExtra("longitud", 0.0)
        val titulo = intent.getStringExtra("titulo") ?: "Ubicación"

        // Validación estricta
        if (latitud !in -90.0..90.0 || longitud !in -180.0..180.0) {
            Toast.makeText(this, "Coordenadas fuera de rango", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        Log.d("MAPA", "Coordenadas: $latitud, $longitud")

        if (latitud != 0.0 || longitud != 0.0) {
            solicitarPermisos()
            inicializarLogicaMapa(latitud, longitud, titulo)
        } else {
            Toast.makeText(this, "Coordenadas inválidas", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun inicializarLogicaMapa(lat: Double, lon: Double, titulo: String) {
        val fragmentoMapa = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        fragmentoMapa.getMapAsync { googleMap ->
            mapa = googleMap
            establecerConfiguracionMapa()
            val ubicacion = LatLng(lat, lon)
            anadirMarcador(ubicacion, titulo)?.showInfoWindow()
            moverCamaraZoom(ubicacion)
            progressBar.visibility = View.GONE
        }
    }

    @SuppressLint("MissingPermission")
    private fun establecerConfiguracionMapa() {
        with(mapa) {
            uiSettings.apply {
                isZoomControlsEnabled = true
                isMyLocationButtonEnabled = tengoPermisos()
            }
            if (tengoPermisos()) isMyLocationEnabled = true
        }
    }

    private fun moverCamaraZoom(latLang: LatLng, zoom: Float = 12f) {
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang, zoom))
    }

    private fun anadirMarcador(latLang: LatLng, title: String): Marker? {
        return mapa.addMarker(
            MarkerOptions()
                .position(latLang)
                .title(title)
                .snippet("Coordenadas: ${"%.4f".format(latLang.latitude)}, ${"%.4f".format(latLang.longitude)}")
        )
    }

    private fun tengoPermisos(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            nombrePermisoFine
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun solicitarPermisos() {
        if (!tengoPermisos()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(nombrePermisoFine, nombrePermisoCoarse),
                1
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                establecerConfiguracionMapa()
            }
        }
    }
}