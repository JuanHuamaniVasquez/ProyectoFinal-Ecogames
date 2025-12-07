package com.example.semaforo.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/*
Repositorio para manejar los resultados del minijuego "Cinta Eco".

Se encarga de:
 - Guardar los resultados en SharedPreferences en formato JSON.
 - Devolver la lista de partidas jugadas.
 - Obtener el mejor puntaje.
 - Borrar tdo el historial guardado.
 */
object CintaResultRepository {
    private const val PREFS_NAME = "cinta_prefs"
    private const val KEY_RESULTS = "cinta_results"

    // Guarda un nuevo resultado en el historial
    fun saveResult(context: Context, result: CintaResult) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_RESULTS, "[]")
        val jsonArray = JSONArray(jsonString)

        val obj = JSONObject().apply {
            put("score", result.score)
            put("correctCount", result.correctCount)
            put("livesRemaining", result.livesRemaining)
            put("timestamp", result.timestamp)
        }

        jsonArray.put(obj)

        prefs.edit()
            .putString(KEY_RESULTS, jsonArray.toString())
            .apply()
    }

    // Devuelve la lista de resultados ordenada de más reciente a más antiguo
    fun getResults(context: Context): List<CintaResult> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_RESULTS, "[]")
        val jsonArray = JSONArray(jsonString)

        val list = mutableListOf<CintaResult>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            list.add(
                CintaResult(
                    score = obj.getInt("score"),
                    correctCount = obj.getInt("correctCount"),
                    livesRemaining = obj.getInt("livesRemaining"),
                    timestamp = obj.getLong("timestamp")
                )
            )
        }
        return list.sortedByDescending { it.timestamp }
    }

    // Devuelve el mejor puntaje registrado o 0 si no hay partidas
    fun getBestScore(context: Context): Int {
        return getResults(context).maxOfOrNull { it.score } ?: 0
    }

    // Elimina tdo el historial de resultados
    fun clearHistory(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_RESULTS).apply()
    }
}
