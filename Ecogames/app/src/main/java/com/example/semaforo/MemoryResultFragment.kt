package com.example.semaforo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semaforo.adapter.MemoryResultAdapter
import com.example.semaforo.data.MemoryResult
import com.example.semaforo.data.MemoryResultRepository
/*
 Fragmento que muestra los resultados del minijuego de memoria (EcoMemory).

 Aquí se visualiza:
 - El resultado de la última partida (puntaje, movimientos, tiempo).
 - El mejor puntaje histórico.
 - Un listado con el historial de partidas.

 Además, permite:
 - Borrar el historial de resultados.
 - Volver al menú principal.
 */
class MemoryResultFragment : Fragment() {
    // Vistas para mostrar datos de la última partida
    private lateinit var tvCurrentScore: TextView
    private lateinit var tvCurrentMoves: TextView
    private lateinit var tvCurrentTime: TextView

    // Vista para mostrar el mejor puntaje alcanzado
    private lateinit var tvBestScore: TextView

    // RecyclerView para mostrar el historial de partidas
    private lateinit var rvHistory: RecyclerView

    // Botones para acciones sobre el historial y navegación
    private lateinit var btnClearHistory: Button
    private lateinit var btnBackToMenu: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_memory_result, container, false)
    }
    /*
    Aquí se inicializan las vistas, se leen los argumentos recibidos
    y se configura el historial y las acciones de los botones.
    */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvCurrentScore = view.findViewById(R.id.tvMemoryCurrentScore)
        tvCurrentMoves = view.findViewById(R.id.tvMemoryCurrentMoves)
        tvCurrentTime = view.findViewById(R.id.tvMemoryCurrentTime)
        tvBestScore = view.findViewById(R.id.tvMemoryBestScore)
        rvHistory = view.findViewById(R.id.rvMemoryHistory)
        btnClearHistory = view.findViewById(R.id.btnClearMemoryHistory)
        btnBackToMenu = view.findViewById(R.id.btnBackToMenuFromMemory)

        // Datos que llegan desde MemoryGameFragment
        val currentScore = arguments?.getInt(getString(R.string.game_score)) ?: 0
        val currentMoves = arguments?.getInt(getString(R.string.game_moves)) ?: 0
        val currentTimeMillis = arguments?.getLong(getString(R.string.game_timemillis)) ?: 0L
        val currentSeconds = currentTimeMillis / 1000

        tvCurrentScore.text = getString(R.string.memory_game_puntaje_actual, currentScore)
        tvCurrentMoves.text = getString(R.string.memory_game_movimientos, currentMoves)
        tvCurrentTime.text = getString(R.string.game_resulttiempo_s, currentSeconds)

        val results: List<MemoryResult> =
            MemoryResultRepository.getResults(requireContext())
        val bestScore = results.maxOfOrNull { it.score } ?: 0
        tvBestScore.text = getString(R.string.game_result_mejor_puntaje, bestScore)

        rvHistory.layoutManager = LinearLayoutManager(requireContext())
        rvHistory.adapter = MemoryResultAdapter(results)

        btnClearHistory.setOnClickListener {
            showClearHistoryDialog()
        }

        btnBackToMenu.setOnClickListener {
            // vuelve al menú principal (ajusta el id según tu nav_graph)
            findNavController().navigate(R.id.menuFragment)
        }
    }

    /*
     Muestra un diálogo de confirmación para borrar el historial de resultados.
     Si el usuario confirma:
     - Se limpia el historial en el repositorio.
     - Se actualiza el mejor puntaje mostrado.
     - Se recarga el adaptador del RecyclerView con una lista vacía.
     */
    private fun showClearHistoryDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.result_borrar_historial))
            .setMessage(getString(R.string.result_message))
            .setPositiveButton(getString(R.string.result_si_borrar)) { dialog, _ ->
                MemoryResultRepository.clearHistory(requireContext())
                tvBestScore.text = getString(R.string.tv_result_mejor_puntaje)

                rvHistory.adapter = MemoryResultAdapter(emptyList())
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.game_cancelar)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
        // Posible mejora:
        // - Mostrar un pequeño mensaje (Toast o Snackbar) indicando que el historial fue borrado.
    }
}