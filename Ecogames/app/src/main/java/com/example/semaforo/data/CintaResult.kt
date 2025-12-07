package com.example.semaforo.data

/*
Modelo de datos que representa el resultado de una partida
del minijuego "Cinta Eco".

Se usa para:
 - Guardar el puntaje final obtenido en la partida.
 - Registrar cuántos aciertos tuvo el jugador.
 - Registrar cuántas vidas le quedaron al finalizar la partida.
 - Guardar la fecha y hora en que se jugó (timestamp) para poder
   ordenar y mostrar un historial.

Decisión de diseño:
 - Se almacena `correctCount` y `livesRemaining` porque son métricas
   muy relacionadas con la dificultad del juego (cinta que acelera,
   errores por residuos que se escapan, etc.).
 - El `timestamp` permite ordenar las partidas de más reciente a más antigua
   en el `CintaResultRepository`.
 - Si en el futuro se necesitan más datos (por ejemplo, tiempo total jugado
   o velocidad máxima alcanzada), se pueden agregar nuevos campos a este data class.
 */
data class CintaResult(
    val score: Int,
    val correctCount: Int,
    val livesRemaining: Int,
    val timestamp: Long
)
