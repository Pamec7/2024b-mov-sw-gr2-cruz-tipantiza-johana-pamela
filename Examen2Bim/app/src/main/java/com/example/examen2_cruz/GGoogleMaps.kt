package com.example.examen2_cruz

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class GGoogleMaps : AppCompatActivity() {

    private lateinit var mapa: GoogleMap
    private var permisos = false
    private val nombrePermisoFine = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val nombrePermisoCoarse = android.Manifest.permission.ACCESS_COARSE_LOCATION

    // Coordenadas de Quito, Ecuador
    private val quito = LatLng(-0.180653, -78.467834)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ggoogle_maps)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        solicitarPermisos()
        inicializarLogicaMapa()
    }

    private fun tengoPermisos(): Boolean {
        return (ContextCompat.checkSelfPermission(this, nombrePermisoFine) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, nombrePermisoCoarse) == PackageManager.PERMISSION_GRANTED)
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

    private fun inicializarLogicaMapa() {
        val fragmentoMapa = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        fragmentoMapa.getMapAsync { googleMap ->
            mapa = googleMap
            establecerConfiguracionMapa()
            moverAQuito()
        }
    }

    @SuppressLint("MissingPermission")
    private fun establecerConfiguracionMapa() {
        with(mapa) {
            if (tengoPermisos()) {
                isMyLocationEnabled = true
                uiSettings.isMyLocationButtonEnabled = true
            }
            uiSettings.isZoomControlsEnabled = true
        }
    }

    private fun moverAQuito() {
        anadirMarcador(quito, "Quito, Ecuador")
        moverCamaraConZoom(quito)
    }

    private fun moverCamaraConZoom(latLang: LatLng, zoom: Float = 12f) {
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang, zoom))
    }

    private fun anadirMarcador(latLang: LatLng, title: String) {
        mapa.addMarker(MarkerOptions().position(latLang).title(title))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty()) {
            if (tengoPermisos()) {
                establecerConfiguracionMapa()
            } else {
                Snackbar.make(findViewById(R.id.main), "Permisos denegados", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}