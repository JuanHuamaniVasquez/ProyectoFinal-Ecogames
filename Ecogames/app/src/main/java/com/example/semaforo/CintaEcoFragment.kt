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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.semaforo.R
import com.example.semaforo.model.BinType
import com.example.semaforo.model.WasteItem
import android.view.DragEvent
import android.widget.Toast
import android.content.ClipData
import android.os.Build
import android.view.MotionEvent



class CintaEcoFragment : Fragment() {
    private lateinit var tvScore: TextView
    private lateinit var tvStreak: TextView
    private lateinit var tvLives: TextView
    private lateinit var ivItem: ImageView
    private lateinit var tvItemName: TextView
    private lateinit var progressItemTime: ProgressBar

    private lateinit var btnBrown: Button
    private lateinit var btnGreen: Button
    private lateinit var btnBlack: Button
    private lateinit var btnHazard: Button

    private lateinit var items: List<WasteItem>
    private var currentItem: WasteItem? = null

    private var score = 0
    private var streak = 0
    private var lives = 3
    private var correctCount = 0

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

    private fun initItems() {
        // Reutiliza los drawables que ya tienes
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

    private fun setupButtons() {
        btnBrown.setOnClickListener { onBinSelected(BinType.MARRON) }
        btnGreen.setOnClickListener { onBinSelected(BinType.VERDE) }
        btnBlack.setOnClickListener { onBinSelected(BinType.NEGRO) }
        btnHazard.setOnClickListener { onBinSelected(BinType.PELIGROSO) }
    }

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

    private fun updateHud() {
        tvScore.text = "Puntaje: $score"
        tvStreak.text = "Racha: $streak"
        tvLives.text = "Vidas: $lives"
    }

    private fun showNextItem() {
        if (gameOver) return

        currentItem = items.random()
        val item = currentItem!!

        ivItem.setImageResource(item.imageResId)
        tvItemName.text = item.name

        startItemTimer()
    }

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

    private fun onItemEscaped() {
        if (gameOver) return
        onWrong(escaped = true)
    }

    private fun onCorrect() {
        score += 20
        streak += 1
        correctCount += 1
        updateHud()

        // Acelerar cinta cada 5 aciertos
        if (correctCount % 5 == 0 && timePerItemMillis > minTimePerItemMillis) {
            timePerItemMillis -= 500L // restamos 0.5s
        }
        showNextItem()
    }

    private fun onWrong(escaped: Boolean = false) {
        score -= 5
        if (score < 0) score = 0
        streak = 0
        lives -= 1
        updateHud()

        if (lives <= 0) {
            endGame()
            } else {
                // Mostrar feedback rápido
                val msg = if (escaped) "Se escapó sin clasificar" else "Incorrecto"
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

                showNextItem()
            }
    }

    private fun endGame() {
        gameOver = true
        itemTimer?.cancel()

        AlertDialog.Builder(requireContext())
            .setTitle("Fin del juego")
            .setMessage("Puntaje final: $score\nAciertos: $correctCount\nVidas agotadas.")
            .setPositiveButton("Jugar de nuevo") { dialog, _ ->
                dialog.dismiss()
                startGame()
            }
            .setNegativeButton("Salir") { dialog, _ ->
                dialog.dismiss()
                // si tienes navgraph general, podrías hacer:
                // findNavController().popBackStack()
            }
            .setCancelable(false)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        itemTimer?.cancel()
    }
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

    private fun setupDropTargets() {
        val dragListener = View.OnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    v.alpha = 0.7f
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    v.alpha = 1f
                    true
                }
                DragEvent.ACTION_DROP -> {
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