// WasteItem.kt
package com.example.semaforo.model


/*
 Enumeración que representa los tipos de tacho disponibles en el juego.
 Cada valor indica a qué tipo de contenedor debe ir un residuo:
 - MARRON: residuos orgánicos.
 - VERDE: materiales reciclables.
 - NEGRO: residuos no reciclables.
 - PELIGROSO: residuos peligrosos que requieren manejo especial.
 Se usa tanto en el juego de Semáforo de Tachos como en EcoMemory
 para unificar la lógica de clasificación.
 */
enum class BinType {
    MARRON,      // orgánico
    VERDE,       // reciclable
    NEGRO,       // no reciclable
    PELIGROSO    // residuos peligrosos
}

data class WasteItem(
    val name: String,
    val imageResId: Int,
    val binType: BinType
)