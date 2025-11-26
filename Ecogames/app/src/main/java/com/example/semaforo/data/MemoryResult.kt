package com.example.semaforo.data
/*
Modelo de datos que representa el resultado de una partida
del minijuego de memoria "EcoMemory".

 Se utiliza para:
 - Guardar las estadísticas de cada partida.
 - Construir el historial que se muestra en la pantalla de resultados.
 - Calcular, por ejemplo, mejores puntajes o tiempos.
 */
data class MemoryResult(
    val score: Int,
    val moves: Int,
    val timeMillis: Long,
    val timestamp: Long
)