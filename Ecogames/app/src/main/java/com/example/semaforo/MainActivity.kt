package com.example.semaforo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/*
Actividad principal de la aplicación.
En este avance solo se encarga de cargar el layout `activity_main`,
que actúa como contenedor de la interfaz (por ejemplo, fragments o el menú).
Más adelante aquí se puede inicializar navegación, toolbar, etc.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}