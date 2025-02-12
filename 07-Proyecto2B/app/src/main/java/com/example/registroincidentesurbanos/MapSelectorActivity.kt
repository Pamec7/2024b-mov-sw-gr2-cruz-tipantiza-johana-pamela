package com.example.registroincidentesurbanos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.registroincidentesurbanos.databinding.ActivityMapSelectorAactivityBinding

class MapSelectorActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapa: GoogleMap
    private var selectedLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_selector)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        findViewById<Button>(R.id.btn_confirm_location).setOnClickListener {
            selectedLocation?.let {
                val resultIntent = Intent().apply {
                    putExtra("selected_location", it)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapa = googleMap
        mapa.uiSettings.isZoomControlsEnabled = true

        mapa.setOnMapClickListener { latLng ->
            selectedLocation = latLng
            mapa.clear()
            mapa.addMarker(MarkerOptions().position(latLng))
        }

        // Centrar en ubicación actual
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            mapa.isMyLocationEnabled = true
        }
    }
}