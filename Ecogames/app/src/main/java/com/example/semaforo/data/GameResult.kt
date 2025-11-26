package com.example.semaforo.data
/*
Modelo de datos que representa el resultado de una partida
del minijuego "Semáforo de Tachos".

 Se usa para:
 - Guardar el puntaje alcanzado.
 - Registrar la fecha y hora en que se jugó la partida.
 - Persistir un historial de partidas en `GameResultRepository`.

 Decisión de diseño:
 Por ahora solo se almacena el puntaje y el timestamp. Si en el futuro
 se quiere mostrar más estadísticas (tiempo jugado, aciertos, errores, etc.),
 se podrían agregar más campos a este data class.
 */
data class GameResult(
    val score: Int,
    val timestamp: Long
)