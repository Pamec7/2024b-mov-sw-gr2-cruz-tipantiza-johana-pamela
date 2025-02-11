package com.example.examen2_cruz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapsPlaceHolder : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var latitud: Double = -0.180653  // Quito por defecto
    private var longitud: Double = -78.467834

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps_place_holder)

        // Obtener coordenadas del Intent
        latitud = intent.getDoubleExtra("LATITUD", latitud)
        longitud = intent.getDoubleExtra("LONGITUD", longitud)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val ubicacion = LatLng(latitud, longitud)
        mMap.addMarker(MarkerOptions().position(ubicacion).title("Ubicación guardada"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15f))
    }
}