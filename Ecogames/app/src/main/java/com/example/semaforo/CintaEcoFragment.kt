package com.example.semaforo

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.semaforo.model.BinType
import com.example.semaforo.model.WasteItem
import android.view.DragEvent
import android.widget.Toast
import android.content.ClipData
import android.os.Build
import android.view.MotionEvent
import androidx.navigation.fragment.findNavController
import com.example.semaforo.data.CintaResult
import com.example.semaforo.data.CintaResultRepository

/*
Fragmento del minijuego "Cinta Eco".

Se encarga de:
 - Mostrar un residuo a la vez en pantalla.
 - Permitir arrastrar el residuo hacia el tacho correcto (drag & drop).
 - Gestionar puntaje, racha, vidas y velocidad de la "cinta".
 - Controlar el tiempo disponible por residuo con un CountDownTimer.
 - Guardar el resultado de la partida y navegar a la pantalla de resultados.
 */
class CintaEcoFragment : Fragment() {

    // Elementos de la UI (HUD y residuo actual)
    private lateinit var tvScore: TextView
    private lateinit var tvStreak: TextView
    private lateinit var tvLives: TextView
    private lateinit var ivItem: ImageView
    private lateinit var tvItemName: TextView
    private lateinit var progressItemTime: ProgressBar

    // Botones que representan los tachos (zonas de drop)
    private lateinit var btnBrown: Button
    private lateinit var btnGreen: Button
    private lateinit var btnBlack: Button
    private lateinit var btnHazard: Button

    // Lista de residuos posibles y el residuo actual
    private lateinit var items: List<WasteItem>
    private var currentItem: WasteItem? = null

    // Estado del juego
    private var score = 0
    private var streak = 0
    private var lives = 3
    private var correctCount = 0

    // Tiempo por residuo (en milisegundos)
    private var timePerItemMillis: Long = 5000L // empieza con 5s por objeto
    private val minTimePerItemMillis: Long = 2000L

    private var itemTimer: CountDownTimer? = null
    private var gameOver = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cinta_eco, container, false)
    }

    // Se llama cuando la vista ya está creada: aquí se enlazan las vistas y se inicia el juego
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvScore = view.findViewById(R.id.tvCintaScore)
        tvStreak = view.findViewById(R.id.tvCintaStreak)
        tvLives = view.findViewById(R.id.tvCintaLives)
        ivItem = view.findViewById(R.id.ivCintaItem)
        tvItemName = view.findViewById(R.id.tvCintaItemName)
        progressItemTime = view.findViewById(R.id.progressItemTime)

        btnBrown = view.findViewById(R.id.btnBrown)
        btnGreen = view.findViewById(R.id.btnGreen)
        btnBlack = view.findViewById(R.id.btnBlack)
        btnHazard = view.findViewById(R.id.btnHazard)

        initItems()
        setupDragAndDrop()
        setupDropTargets()
        startGame()
    }

    // Inicializa la lista de residuos que pueden aparecer en la cinta
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
    }

    // Reinicia el estado del juego y muestra el primer residuo
    private fun startGame() {
        score = 0
        streak = 0
        lives = 3
        correctCount = 0
        timePerItemMillis = 5000L
        gameOver = false

        updateHud()
        showNextItem()
    }

    // Actualiza los textos de puntaje, racha y vidas en pantalla
    private fun updateHud() {
        tvScore.text = getString(R.string.puntaje, score)
        tvStreak.text = getString(R.string.racha, streak)
        tvLives.text = getString(R.string.vidas, lives)
    }

    // Selecciona un residuo al azar y lo muestra, luego inicia el temporizador
    private fun showNextItem() {
        if (gameOver) return

        currentItem = items.random()
        val item = currentItem!!

        ivItem.setImageResource(item.imageResId)
        tvItemName.text = item.name

        startItemTimer()
    }

    // Inicia la cuenta regresiva para el residuo actual y actualiza la barra de progreso
    private fun startItemTimer() {
        itemTimer?.cancel()
        progressItemTime.progress = 100

        itemTimer = object : CountDownTimer(timePerItemMillis, 100L) {
            override fun onTick(millisUntilFinished: Long) {
                val percent = (millisUntilFinished * 100 / timePerItemMillis).toInt()
                progressItemTime.progress = percent
            }

            override fun onFinish() {
                onItemEscaped()
            }
        }.start()
    }

    // Se llama cuando el jugador suelta el residuo sobre un tacho
    private fun onBinSelected(selectedBin: BinType) {
        if (gameOver) return
        val item = currentItem ?: return

        itemTimer?.cancel()

        if (selectedBin == item.binType) {
            onCorrect()
        } else {
            onWrong()
        }
    }

    // Se llama si el residuo se “escapa” (se acaba el tiempo)
    private fun onItemEscaped() {
        if (gameOver) return
        onWrong(escaped = true)
    }

    // Lógica cuando el jugador acierta el tacho
    private fun onCorrect() {
        score += 20
        streak += 1
        correctCount += 1
        updateHud()

        // Acelerar cinta cada 5 aciertos, hasta un mínimo de tiempo
        if (correctCount % 5 == 0 && timePerItemMillis > minTimePerItemMillis) {
            timePerItemMillis -= 500L
        }
        showNextItem()
    }

    // Lógica cuando el jugador se equivoca o deja escapar el residuo
    private fun onWrong(escaped: Boolean = false) {
        score -= 5
        if (score < 0) score = 0
        streak = 0
        lives -= 1
        updateHud()

        if (lives <= 0) {
            endGame()
        } else {
            // Mostrar un mensaje corto según el tipo de error
            val msg = if (escaped) getString(R.string.escape_cinta) else getString(R.string.incorrecto)
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

            showNextItem()
        }
    }

    // Termina la partida, guarda el resultado y navega a la pantalla de resultados
    private fun endGame() {
        gameOver = true
        itemTimer?.cancel()

        val result = CintaResult(
            score = score,
            correctCount = correctCount,
            livesRemaining = lives,
            timestamp = System.currentTimeMillis()
        )
        CintaResultRepository.saveResult(requireContext(), result)

        val args = Bundle().apply {
            putInt("score", score)
            putInt("correctCount", correctCount)
            putInt("lives", lives)
        }

        findNavController().navigate(
            R.id.action_cintaEcoFragment_to_cintaResultFragment,
            args
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        itemTimer?.cancel()
    }

    // Configura el arrastre del residuo (drag) al tocar la imagen
    @SuppressLint("ClickableViewAccessibility")
    private fun setupDragAndDrop() {
        ivItem.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val dragData = ClipData.newPlainText("waste", "waste")
                    val shadow = View.DragShadowBuilder(view)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        view.startDragAndDrop(dragData, shadow, null, 0)
                    } else {
                        @Suppress("DEPRECATION")
                        view.startDrag(dragData, shadow, null, 0)
                    }
                    true
                }
                else -> false
            }
        }
    }

    // Configura los tachos como zonas de drop y reacciona cuando se suelta el residuo sobre ellos
    private fun setupDropTargets() {
        val dragListener = View.OnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    // Efecto visual cuando el residuo entra en el área del tacho
                    v.alpha = 0.7f
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    // Restaurar apariencia al salir
                    v.alpha = 1f
                    true
                }
                DragEvent.ACTION_DROP -> {
                    // Cuando el jugador suelta el residuo sobre el tacho
                    v.alpha = 1f
                    val binType = when (v.id) {
                        R.id.btnBrown -> BinType.MARRON
                        R.id.btnGreen -> BinType.VERDE
                        R.id.btnBlack -> BinType.NEGRO
                        R.id.btnHazard -> BinType.PELIGROSO
                        else -> null
                    }
                    binType?.let { onBinSelected(it) }
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    // Fin del drag: restaurar apariencia
                    v.alpha = 1f
                    true
                }
                else -> false
            }
        }

        btnBrown.setOnDragListener(dragListener)
        btnGreen.setOnDragListener(dragListener)
        btnBlack.setOnDragListener(dragListener)
        btnHazard.setOnDragListener(dragListener)
    }
}
