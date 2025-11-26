package com.example.semaforo.data

import android.content.Context
import com.example.semaforo.data.MemoryResult
import org.json.JSONArray
import org.json.JSONObject

/*
 Repositorio para gestionar los resultados del minijuego de memoria "EcoMemory".

 Decisión de diseño: ¿por qué usar un Repository?
 - Centraliza toda la lógica de acceso a datos del minijuego de memoria en una sola clase.
 - Separa la capa de datos de la capa de presentación (Fragments), haciendo el código
   más ordenado y fácil de mantener.
 - Si más adelante se cambia SharedPreferences por Room, archivo local o API remota,
   solo habría que modificar este repositorio, sin tocar los fragments.
 - Evita duplicar código de lectura/escritura de resultados en varias pantallas.

 Decisión de almacenamiento:
 - Se usa SharedPreferences + JSON porque los datos son simples (historial ligero
   de partidas) y no justifican una base de datos completa.
 - Se almacena todos en una sola clave (`KEY_RESULTS`) como un arreglo JSON,
   lo que permite guardar múltiples partidas y leerlas de forma estructurada.
 */
object MemoryResultRepository {

    // Nombre del archivo de preferencias donde se guardan los resultados de memoria
    private const val PREFS_NAME = "memory_prefs"

    // Clave bajo la cual se almacenan los resultados (arreglo JSON)
    private const val KEY_RESULTS = "memory_results"
    /*
     Guarda un nuevo resultado de partida en el historial persistente.
     Pasos:
     1. Lee el JSON actual almacenado en SharedPreferences.
     2. Lo convierte en un JSONArray.
     3. Crea un JSONObject con los datos del nuevo resultado.
     4. Lo agrega al arreglo y lo vuelve a guardar como String.
     */
    fun saveResult(context: Context, result: MemoryResult) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_RESULTS, "[]")
        val jsonArray = JSONArray(jsonString)

        val obj = JSONObject().apply {
            put("score", result.score)
            put("moves", result.moves)
            put("timeMillis", result.timeMillis)
            put("timestamp", result.timestamp)
        }

        jsonArray.put(obj)

        prefs.edit()
            .putString(KEY_RESULTS, jsonArray.toString())
            .apply()
    }
    //Obtiene todos los resultados guardados del minijuego de memoria.
    fun getResults(context: Context): List<MemoryResult> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_RESULTS, "[]")
        val jsonArray = JSONArray(jsonString)

        val list = mutableListOf<MemoryResult>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            list.add(
                MemoryResult(
                    score = obj.getInt("score"),
                    moves = obj.getInt("moves"),
                    timeMillis = obj.getLong("timeMillis"),
                    timestamp = obj.getLong("timestamp")
                )
            )
        }
        return list.sortedByDescending { it.timestamp }
    }
    // Borra todos el historial de resultados del minijuego de memoria.
    fun clearHistory(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_RESULTS).apply()
    }
}
