package com.example.semaforo.data

/*
 Enumeración para los tipos de tacho usados en el juego de memoria (EcoMemory).
 Posible mejora: unificar con BinType de Semáforo de Tachos.
 */
enum class BinType { MARRON, VERDE, NEGRO, PELIGROSO }

data class MemoryCard(
    val imageResId: Int,
    val binType: BinType, // a qué tacho pertenece
    val isBin: Boolean,   // true si es carta “tacho”, false si es carta “objeto”
    var isFaceUp: Boolean = false,
    var isMatched: Boolean = false
)
