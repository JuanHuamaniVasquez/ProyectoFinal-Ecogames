package com.example.semaforo

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.semaforo.data.GameResult
import com.example.semaforo.data.GameResultRepository
import com.example.semaforo.model.BinType
import com.example.semaforo.model.WasteItem

/*
Fragmento que implementa el minijuego "Semáforo de Tachos".
El jugador ve un residuo en pantalla y debe elegir el tacho correcto:
orgánico (marrón), reciclable (verde), no reciclable (negro) o peligroso.
Se lleva un puntaje acumulado y un temporizador. Al terminar el tiempo,
se guardan los resultados y se navega a la pantalla de resultados.
*/

class GameFragment : Fragment() {
    // Referencias a elementos de la interfaz
    private lateinit var tvTimer: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvObjectName: TextView
    private lateinit var tvFeedback: TextView
    private lateinit var ivWasteItem: ImageView
    private lateinit var btnBrown: Button
    private lateinit var btnGreen: Button
    private lateinit var btnBlack: Button
    private lateinit var btnHazard: Button
    // Estado del juego
    private var score: Int = 0                       // Puntaje actual del jugador
    private var remainingMillis: Long = 60_000L      // Tiempo restante en milisegundos (60s)
    private var currentItem: WasteItem? = null       // Ítem de residuo actualmente mostrado
    private var gameFinished = false                 // Indica si el juego ya terminó

    // Datos del juego
    private lateinit var items: List<WasteItem>      // Lista de residuos posibles
    private var timer: CountDownTimer? = null        // Temporizador del juego

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initItems()
        setupButtons()
        startGame()
    }

    private fun initViews(view: View) {
        tvTimer = view.findViewById(R.id.tvTimer)
        tvScore = view.findViewById(R.id.tvScore)
        tvObjectName = view.findViewById(R.id.tvObjectName)
        tvFeedback = view.findViewById(R.id.tvFeedback)
        ivWasteItem = view.findViewById(R.id.ivWasteItem)
        btnBrown = view.findViewById(R.id.btnBrown)
        btnGreen = view.findViewById(R.id.btnGreen)
        btnBlack = view.findViewById(R.id.btnBlack)
        btnHazard = view.findViewById(R.id.btnHazard)
    }
    /*
    Inicializa la lista de residuos que se mostrarán en el juego.
    Cada WasteItem contiene:
    - nombre del residuo (String)
    - recurso de imagen (Int)
    - tipo de tacho asociado (BinType)
    Se usan strings de recursos para facilitar la traducción o cambios de texto.
    */
    private fun initItems() {
        items = listOf(
            // Residuos orgánicos
            WasteItem(getString(R.string.item_platano), R.drawable.ic_cascara_platano, BinType.MARRON),
            WasteItem(getString(R.string.item_papel_cocina), R.drawable.ic_papel_cocina, BinType.MARRON),
            WasteItem(getString(R.string.item_cascara_de_huevo), R.drawable.ic_cascara_huevo, BinType.MARRON),
            // Residuos reciclables
            WasteItem(getString(R.string.item_botella_de_plastico), R.drawable.ic_botella_plastico, BinType.VERDE),
            WasteItem(getString(R.string.item_lata_de_gaseosa), R.drawable.ic_lata_gaseosa, BinType.VERDE),
            WasteItem(getString(R.string.item_botella_de_vidrio), R.drawable.ic_vidrio_roto, BinType.VERDE),
            WasteItem(getString(R.string.item_caja_de_carton_limpia), R.drawable.ic_caja_carton, BinType.VERDE),
            // Residuos no reciclables
            WasteItem(getString(R.string.item_panal_desechable), R.drawable.ic_panal, BinType.NEGRO),
            WasteItem(getString(R.string.item_papel_higienico_sucio), R.drawable.ic_papel_higienico, BinType.NEGRO),
            WasteItem(getString(R.string.item_vajilla_rota), R.drawable.ic_vajilla_rota, BinType.NEGRO),
            WasteItem(getString(R.string.item_collila_de_cigarro), R.drawable.ic_colilla, BinType.NEGRO),
            // Residuos peligrosos
            WasteItem(getString(R.string.item_pilas_usadas), R.drawable.ic_pila, BinType.PELIGROSO),
            WasteItem(getString(R.string.item_jeringa_usada), R.drawable.ic_jeringa, BinType.PELIGROSO),
            WasteItem(getString(R.string.item_medicamento_vencido), R.drawable.ic_medicamento, BinType.PELIGROSO),
            WasteItem(getString(R.string.item_aceite_de_motor), R.drawable.ic_aceite_motor, BinType.PELIGROSO)
        )
        // Posible mejora:
        // - Extraer estos datos a una clase repositorio o a un ViewModel para
        //   poder reutilizarlos en otros minijuegos o pantallas.
    }


    private fun setupButtons() {
        btnBrown.setOnClickListener { onBinSelected(BinType.MARRON) }
        btnGreen.setOnClickListener { onBinSelected(BinType.VERDE) }
        btnBlack.setOnClickListener { onBinSelected(BinType.NEGRO) }
        btnHazard.setOnClickListener { onBinSelected(BinType.PELIGROSO) }
    }

    private fun startGame() {
        score = 0
        gameFinished = false
        tvScore.text = getString(R.string.game_puntaje, score)
        tvFeedback.text = ""
        showNextItem()
        startTimer()
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(remainingMillis, 1_000L) {
            override fun onTick(millisUntilFinished: Long) {
                remainingMillis = millisUntilFinished
                val seconds = millisUntilFinished / 1000
                tvTimer.text = getString(R.string.game_tiempo_s, seconds)
            }

            override fun onFinish() {
                gameFinished = true
                tvTimer.text = getString(R.string.game_tiempo_0s)
                endGame()
            }
        }.start()
    }
    // Selecciona aleatoriamente un residuo de la lista y lo muestra en pantalla.
    private fun showNextItem() {
        if (items.isEmpty()) return
        currentItem = items.random()
        val item = currentItem!!
        ivWasteItem.setImageResource(item.imageResId)
        tvObjectName.text = item.name
    }
    /*
     Lógica que se ejecuta cuando el usuario selecciona un tacho.
     - Si acierta el tipo de tacho, suma puntos.
     - Si se equivoca, pierde puntos (sin bajar de 0).
     Luego muestra un nuevo residuo para continuar el juego.
     */
    private fun onBinSelected(selectedBin: BinType) {
        if (gameFinished) return
        val item = currentItem ?: return

        if (selectedBin == item.binType) {
            score += 10
            tvFeedback.text = getString(R.string.game_correcto)
            tvFeedback.setTextColor(requireContext().getColor(R.color.bin_green))
        } else {
            score -= 5
            if (score < 0) score = 0
            tvFeedback.text = getString(R.string.game_incorrecto)
            tvFeedback.setTextColor(requireContext().getColor(android.R.color.holo_red_dark))
        }

        tvScore.text = getString(R.string.game_puntaje, score)
        showNextItem()
    }
    /*
     Finaliza la partida:
     - Detiene el temporizador.
     - Guarda el resultado en el repositorio.
     - Navega al fragmento de resultados, enviando el puntaje como argumento.
     */
    private fun endGame() {
        timer?.cancel()
        gameFinished = true

        val result = GameResult(score, System.currentTimeMillis())
        GameResultRepository.saveResult(requireContext(), result)

        val args = bundleOf(getString(R.string.game_score) to score)
        findNavController().navigate(R.id.action_gameFragment_to_resultFragment, args)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
    }
}