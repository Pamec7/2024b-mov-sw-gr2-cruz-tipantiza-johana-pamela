package com.example.gr2sw2024b_jpct

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class GGoogleMaps : AppCompatActivity() {

    private lateinit var  mapa:GoogleMap
    var permisos = false
    val nombrePermisoFine = android.Manifest.permission.ACCESS_FINE_LOCATION
    val nombrePermisoCoarse = android.Manifest.permission.ACCESS_COARSE_LOCATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ggoogle_maps)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fun tengoPermisoos():Boolean{
            val contexto = applicationContext
            val permisoFine = ContextCompat.checkSelfPermission(contexto, nombrePermisoFine)
            val permisoCoarse = ContextCompat.checkSelfPermission(contexto,nombrePermisoCoarse)
            val tienePermisos = permisoFine == PackageManager.PERMISSION_GRANTED &&
                    permisoCoarse == PackageManager.PERMISSION_GRANTED
            if(tienePermisos){
                permisos = true
            }else{
                return permisos
            }
        }

        fun solicitarPermisos() {
            if (!tengoPermisoos()) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(nombrePermisoFine, nombrePermisoCoarse), 1
                )
            }
        }


        fun inicializarLogicaMapa() {
            val fragmentoMapa = supportFragmentManager.findFragmentById(
                R.id.map
            ) as SupportMapFragment
            fragmentoMapa.getMapAsync { googleMap ->
                with(googleMap) {
                    mapa = googleMap
                    establecerConfiguracionMapa()
                    moverQuicentro()
                }
            }
        }

        fun moverQuicentro(){
            val quicentro = LatLng(-0.17633018352832922,-78.47853222234333)
            val titulo = "Quicentro"
            val marcadorQuicentro = anadirMarcador(quicentro, titulo)
            marcadorQuicentro.taq = titulo
            moverCamaraZoom(quicentro)
        }

        @SuppressLint("MissingPermission")
        fun establecerConfiguracionMapa() {
            with(mapa) {
                if (tengoPermisoos()) {
                    mapa.isMyLocationEnabled = true
                    uiSettings.isMyLocationButtonEnabled = true
                }
                uiSettings.isZoomControlsEnabled = true
            }
        }

        fun moverCamaraConZoom(latLang:LatLng, zoom:Float = 17f){
            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang,zoom))
        }

        fun anadirMarcador(latLang:LatLng, title: String): Marker {
            return mapa.addMarker(MarkerOptions().position(latLang).title(title))!!
        }

        fun mostrarSnackbar(texto:String){
            val snack = Snackbar.make(
                findViewById(R.id.main),
                texto,
                Snackbar.LENGTH_INDEFINITE
            )
                snack.show()
        }

    }

}