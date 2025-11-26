package com.example.semaforo.data

import android.content.Context
import com.example.semaforo.data.GameResult
import org.json.JSONArray
import org.json.JSONObject

/*
 Repositorio para gestionar los resultados del minijuego "Semáforo de Tachos".
 Decisión de diseño: ¿por qué usar un Repository?
 - Separamos la lógica de acceso a datos de la lógica de la UI (Fragment/Activity),
   siguiendo una idea similar al patrón Repository.
 - Si en el futuro cambiamos el mecanismo de almacenamiento (por ejemplo,
   de SharedPreferences a Room o a un backend remoto), solo tendríamos que
   modificar este objeto, sin tocar el resto de la app.
 - Evitamos tener código repetido de lectura/escritura en varios fragments.

 Decisión de almacenamiento:
 - Se usa SharedPreferences + JSON porque la cantidad de datos es pequeña
   (historial simple de puntajes) y no justifica la complejidad de una base
   de datos completa.
 - Se guardatodo en una sola clave (`KEY_RESULTS`) como un arreglo JSON
   para mantener la estructura y facilitar el parseo.
 */
object GameResultRepository {
    // Nombre del archivo de preferencias donde se guardan los resultados
    private const val PREFS_NAME = "semaforo_prefs"
    // Clave donde se almacena el arreglo JSON de resultados
    private const val KEY_RESULTS = "game_results"
    /*
     Guarda un nuevo resultado en el historial persistente.
     Pasos:
     1. Lee la cadena JSON existente desde SharedPreferences.
     2. La convierte en JSONArray.
     3. Agrega un nuevo objeto JSON con `score` y `timestamp`.
     4. Vuelve a guardar el arreglo completo en SharedPreferences.
     */
    fun saveResult(context: Context, result: GameResult) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_RESULTS, "[]")
        val jsonArray = JSONArray(jsonString)
        // Creamos un objeto JSON con los campos que nos interesan del resultado
        val obj = JSONObject().apply {
            put("score", result.score)
            put("timestamp", result.timestamp)
        }
        // Añadimos el nuevo resultado al arreglo
        jsonArray.put(obj)
        // Persistimos la versión actualizada del historial
        prefs.edit()
            .putString(KEY_RESULTS, jsonArray.toString())
            .apply()
    }
    //Obtiene la lista de resultados guardados, ordenados del más reciente al más antiguo.
    fun getResults(context: Context): List<GameResult> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_RESULTS, "[]")
        val jsonArray = JSONArray(jsonString)

        val list = mutableListOf<GameResult>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val score = obj.getInt("score")
            val timestamp = obj.getLong("timestamp")
            list.add(GameResult(score, timestamp))
        }

        // ordenamos del más reciente al más antiguo
        return list.sortedByDescending { it.timestamp }
    }
    //Devuelve el mejor puntaje registrado en el historial.
    //Si no hay datos, retorna 0.
    fun getBestScore(context: Context): Int {
        return getResults(context).maxOfOrNull { it.score } ?: 0
    }
    /*
    Elimina todos el historial de resultados de SharedPreferences.
    Al eliminar la clave `KEY_RESULTS`, el metodo `getResults` usará "[]"
    como valor por defecto y devolverá una lista vacía.
     */
    fun clearHistory(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .remove(KEY_RESULTS) // al leer, getResults usará "[]" por defecto
            .apply()
    }
}