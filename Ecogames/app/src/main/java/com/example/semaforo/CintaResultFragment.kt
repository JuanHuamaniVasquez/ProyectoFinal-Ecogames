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
import com.example.semaforo.adapter.CintaResultAdapter
import com.example.semaforo.data.CintaResult
import com.example.semaforo.data.CintaResultRepository

/*
Pantalla de resultados del minijuego "Cinta Eco".

Se encarga de:
 - Mostrar los datos de la partida que acaba de terminar.
 - Mostrar el mejor puntaje histórico.
 - Listar el historial de partidas guardadas.
 - Permitir borrar el historial.
 - Volver al menú principal.
 */
class CintaResultFragment : Fragment() {

    // Vistas de la UI
    private lateinit var tvCurrentScore: TextView
    private lateinit var tvCurrentCorrect: TextView
    private lateinit var tvCurrentLives: TextView
    private lateinit var tvBestScore: TextView
    private lateinit var rvHistory: RecyclerView
    private lateinit var btnClearHistory: Button
    private lateinit var btnBackToMenu: Button

    // Infla el layout del fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cinta_result, container, false)
    }

    // Se ejecuta cuando la vista ya está creada: se inicializan vistas y se cargan los datos
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Referencias a las vistas
        tvCurrentScore = view.findViewById(R.id.tvCintaCurrentScore)
        tvCurrentCorrect = view.findViewById(R.id.tvCintaCurrentCorrect)
        tvCurrentLives = view.findViewById(R.id.tvCintaCurrentLives)
        tvBestScore = view.findViewById(R.id.tvCintaBestScore)
        rvHistory = view.findViewById(R.id.rvCintaHistory)
        btnClearHistory = view.findViewById(R.id.btnClearCintaHistory)
        btnBackToMenu = view.findViewById(R.id.btnBackToMenuFromCinta)

        // Datos de la partida actual enviados por argumentos
        val currentScore = arguments?.getInt("score") ?: 0
        val currentCorrect = arguments?.getInt("correctCount") ?: 0
        val currentLives = arguments?.getInt("lives") ?: 0

        tvCurrentScore.text = getString(R.string.puntaje_actual, currentScore)
        tvCurrentCorrect.text = getString(R.string.aciertos, currentCorrect)
        tvCurrentLives.text = getString(R.string.vidas_restantes, currentLives)

        // Cargar historial desde el repositorio
        val results: List<CintaResult> =
            CintaResultRepository.getResults(requireContext())
        val bestScore = results.maxOfOrNull { it.score } ?: 0
        tvBestScore.text = getString(R.string.mejor_puntaje_hist_rico, bestScore)

        // Configurar RecyclerView con el historial
        rvHistory.layoutManager = LinearLayoutManager(requireContext())
        rvHistory.adapter = CintaResultAdapter(results)

        // Botón para borrar historial
        btnClearHistory.setOnClickListener {
            showClearHistoryDialog()
        }

        // Botón para volver al menú principal
        btnBackToMenu.setOnClickListener {
            findNavController().navigate(R.id.menuFragment)
        }
    }

    // Muestra un diálogo de confirmación antes de borrar el historial
    private fun showClearHistoryDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.borrar_historial))
            .setMessage(getString(R.string.seguro_que_quieres_borrar_el_historial_de_cinta_eco))
            .setPositiveButton(getString(R.string.s_borrar)) { dialog, _ ->
                // Si confirma, borra el historial y actualiza la UI
                CintaResultRepository.clearHistory(requireContext())
                tvBestScore.text = getString(R.string.mejor_puntaje_hist_rico_0)
                rvHistory.adapter = CintaResultAdapter(emptyList())
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancelar)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
