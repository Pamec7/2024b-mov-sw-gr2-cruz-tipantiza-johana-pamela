package com.example.gr2sw2024b_jpct

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class AClicloVida : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_aciclo_vida)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_ciclo_vida)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mostrarSnackbar("onCreate")
    }

    override fun onStart(){
        super.onStart()
        mostrarSnackbar("onStart")
    }

    override fun onResume(){
        super.onResume()
        mostrarSnackbar("onResume")
    }

    override fun onRestart(){
        super.onRestart()
        mostrarSnackbar("onRestart")
    }

    override fun onPause(){
        super.onPause()
        mostrarSnackbar("onPause")
    }

    override fun onStop(){
        super.onStop()
        mostrarSnackbar("onStop")
    }

    override fun onDestroy(){
        super.onDestroy()
        mostrarSnackbar("onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run{
            putString("variableTextoGuardado", textoGlobal)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val textoRecuperado : String? = savedInstanceState.getString("variableTestoGuardado")
        if(textoRecuperado != null){
            mostrarSnackbar(textoRecuperado)
        }
    }

    var textoGlobal = ""
    fun mostrarSnackbar(texto:String){
        textoGlobal += texto
        var snack = Snackbar.make(
        findViewById(R.id.cl_ciclo_vida),
        textoGlobal,
        Snackbar.LENGTH_INDEFINITE
        )
        snack.show()

    }
}