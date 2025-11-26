package com.example.semaforo

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semaforo.adapter.GameResultAdapter
import com.example.semaforo.data.GameResult
import com.example.semaforo.data.GameResultRepository

/*
 Fragmento que muestra los resultados del minijuego "Semáforo de Tachos".
 Aquí se ve:
 - El puntaje de la última partida.
 - El mejor puntaje histórico.
 - Un historial de todas las partidas jugadas.

 Además permite:
 - Volver al menú principal.
 - Borrar el historial de resultados.
 */
class ResultFragment : Fragment() {
    // Vistas para mostrar el puntaje actual y el mejor puntaje
    private lateinit var tvCurrentScore: TextView
    private lateinit var tvBestScore: TextView

    // RecyclerView para listar el historial de partidas
    private lateinit var rvHistory: RecyclerView

    // Botones de navegación y acciones
    private lateinit var btnBackToMenu: Button
    private lateinit var btnClearHistory: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvCurrentScore = view.findViewById(R.id.tvCurrentScore)
        tvBestScore = view.findViewById(R.id.tvBestScore)
        rvHistory = view.findViewById(R.id.rvHistory)
        btnBackToMenu = view.findViewById(R.id.btnBackToMenu)
        btnClearHistory = view.findViewById(R.id.btnClearHistory)
        // Puntaje actual que llega como argumento desde GameFragment
        val currentScore = arguments?.getInt(getString(R.string.game_score)) ?: 0
        // Mejor puntaje histórico y lista de resultados guardados
        val bestScore = GameResultRepository.getBestScore(requireContext())
        val results = GameResultRepository.getResults(requireContext())

        tvCurrentScore.text = getString(R.string.game_puntaje_actual, currentScore)
        tvBestScore.text = getString(R.string.result_mejor_puntaje, bestScore)
        // Configurar RecyclerView con layout vertical y adaptador de historial
        rvHistory.layoutManager = LinearLayoutManager(requireContext())
        rvHistory.adapter = GameResultAdapter(results)
        // Botón para volver al menú principal
        btnBackToMenu.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_menuFragment)
        }
        // Botón para borrar historial de resultados
        btnClearHistory.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.result_borrar_historial))
                .setMessage(getString(R.string.result_message))
                .setPositiveButton(getString(R.string.result_si_borrar)) { dialog, _ ->
                    GameResultRepository.clearHistory(requireContext())

                    // recargar lista vacía
                    val emptyList = emptyList<GameResult>()
                    rvHistory.adapter = GameResultAdapter(emptyList)
                    tvBestScore.text = getString(R.string.tv_result_mejor_puntaje)

                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.game_cancelar)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

    }
}