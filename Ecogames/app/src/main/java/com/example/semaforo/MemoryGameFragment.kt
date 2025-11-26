package com.example.semaforo

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semaforo.adapter.MemoryGameAdapter
import com.example.semaforo.data.BinType
import com.example.semaforo.data.MemoryCard
import com.example.semaforo.data.MemoryResult
import com.example.semaforo.data.MemoryResultRepository

/*
 Fragmento que implementa el minijuego de memoria "EcoMemory".
 El tablero se compone de pares: un residuo y su tacho correspondiente.
 El jugador debe encontrar las parejas correctas en el menor número de
 movimientos y en el menor tiempo posible.
 */
class MemoryGameFragment : Fragment() {
    // Vistas de la interfaz
    private lateinit var tvMoves: TextView      // Muestra el número de movimientos realizados
    private lateinit var tvScore: TextView      // Muestra el puntaje actual
    private lateinit var tvTime: TextView       // Muestra el tiempo transcurrido
    private lateinit var rvGrid: RecyclerView   // Grid donde se muestran las cartas

    // Datos del juego
    private lateinit var cards: MutableList<MemoryCard>  // Lista de cartas del tablero
    private lateinit var adapter: MemoryGameAdapter      // Adaptador para el RecyclerView

    // Estado del juego
    private var moves = 0                      // Número de movimientos del jugador
    private var score = 0                      // Puntaje acumulado
    private var indexOfSingleSelectedCard: Int? = null   // Índice de la primera carta seleccionada en un turno
    private var boardLocked = false            // Indica si el tablero está temporalmente bloqueado (para evitar clicks mientras se voltean cartas)

    // Control de tiempo
    private var startTime: Long = 0L           // Momento de inicio de la partida (en milisegundos)
    private var elapsedMillis: Long = 0L       // Tiempo transcurrido desde el inicio
    private val timeHandler = Handler(Looper.getMainLooper()) // Handler para actualizar el tiempo en el hilo principal
    private val timeRunnable = object : Runnable {     // Runnable que se ejecuta cada segundo para actualizar el TextView del tiempo
        override fun run() {
            elapsedMillis = System.currentTimeMillis() - startTime
            val seconds = elapsedMillis / 1000
            tvTime.text = getString(R.string.game_tiempo_s, seconds)
            timeHandler.postDelayed(this, 1000L)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_memory_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvMoves = view.findViewById(R.id.tvMoves)
        tvScore = view.findViewById(R.id.tvMemoryScore)
        tvTime = view.findViewById(R.id.tvTimeMemory)
        rvGrid = view.findViewById(R.id.rvMemoryGrid)

        setupGame()

    }
    /*
     Inicializa o reinicia completamente el estado del juego:
     - Reinicia contadores de movimientos y puntaje.
     - Reinicia el cronómetro.
     - Crea y baraja las cartas.
     - Configura el RecyclerView con su adaptador y el layout en cuadrícula.
     */
    private fun setupGame() {
        moves = 0
        score = 0
        indexOfSingleSelectedCard = null
        boardLocked = false
        // Actualizar textos iniciales
        tvMoves.text = getString(R.string.game_movimientos, moves)
        tvScore.text = getString(R.string.game_puntaje, score)
        tvTime.text = getString(R.string.game_tiempo_0s)

        // Configuración del tiempo: se reinicia el cronómetro
        startTime = System.currentTimeMillis()
        elapsedMillis = 0L
        timeHandler.removeCallbacksAndMessages(null)
        timeHandler.post(timeRunnable)
        // Crear y barajar las cartas del tablero
        cards = createCards().toMutableList()
        // Crear adaptador y definir qué ocurre cuando se hace click en una carta
        adapter = MemoryGameAdapter(cards) { position ->
            onCardClicked(position)
        }
        // Definir el layout del RecyclerView como un grid de 4 columnas (4x4)
        rvGrid.layoutManager = GridLayoutManager(requireContext(), 4)
        rvGrid.adapter = adapter
        // Posible mejora:
        // - Hacer el tamaño del tablero dinámico (por ejemplo, según la dificultad o el tamaño de pantalla).
    }
    /*
     Crea la lista completa de cartas del juego:
     - Mitad de las cartas son objetos (residuos).
     - La otra mitad son tachos correspondientes al tipo de residuo.
     Cada par está formado por (objeto, tacho) con el mismo BinType.
     */
    private fun createCards(): List<MemoryCard> {
        // Cartas de objetos (residuos). Cada una tiene un tipo de tacho asociado.
        val objectCards = listOf(
            MemoryCard(R.drawable.ic_caja_carton, BinType.VERDE, isBin = false),
            MemoryCard(R.drawable.ic_botella_plastico, BinType.VERDE, isBin = false),
            MemoryCard(R.drawable.ic_cascara_platano, BinType.MARRON, isBin = false),
            MemoryCard(R.drawable.ic_cascara_huevo, BinType.MARRON, isBin = false),
            MemoryCard(R.drawable.ic_panal, BinType.NEGRO, isBin = false),
            MemoryCard(R.drawable.ic_papel_higienico, BinType.NEGRO, isBin = false),
            MemoryCard(R.drawable.ic_pila, BinType.PELIGROSO, isBin = false),
            MemoryCard(R.drawable.ic_medicamento, BinType.PELIGROSO, isBin = false),
            MemoryCard(R.drawable.ic_jeringa, BinType.PELIGROSO, isBin = false),
            MemoryCard(R.drawable.ic_colilla, BinType.NEGRO, isBin = false),
            MemoryCard(R.drawable.ic_lata_gaseosa, BinType.VERDE, isBin = false),
            MemoryCard(R.drawable.ic_vajilla_rota, BinType.NEGRO, isBin = false)
        )

        // Por cada objeto, se genera una carta de tacho con el mismo tipo de reciclaje (BinType)
        val binCards = objectCards.map { obj ->
            val binImage = when (obj.binType) {
                BinType.MARRON -> R.drawable.ic_tacho_marron
                BinType.VERDE -> R.drawable.ic_tacho_verde
                BinType.NEGRO -> R.drawable.ic_tacho_negro
                BinType.PELIGROSO -> R.drawable.ic_tacho_rojo
            }
            MemoryCard(binImage, obj.binType, isBin = true)
        }

        val allCards = objectCards + binCards
        return allCards.shuffled()
    }
    /*
     Maneja la lógica cuando el usuario toca una carta del tablero.
     Casos:
     - Si es la primera carta del turno: se voltea y se guarda su índice.
     - Si es la segunda carta del turno: se verifica si hace pareja con la primera.
       - Si hacen pareja: se marcan como encontradas y se suma puntaje.
       - Si no hacen pareja: se resta puntaje y se vuelven a voltear tras un pequeño retraso.
     */
    private fun onCardClicked(position: Int) {
        if (boardLocked) return

        val card = cards[position]
        // No permitir interacción con cartas ya descubiertas o encontradas
        if (card.isFaceUp || card.isMatched) return

        if (indexOfSingleSelectedCard == null) {
            // primera carta seleccionada
            restoreUnmatchedCards()
            card.isFaceUp = true
            indexOfSingleSelectedCard = position
            adapter.notifyItemChanged(position)
        } else {
            // segunda carta seleccionada
            val firstIndex = indexOfSingleSelectedCard!!
            val firstCard = cards[firstIndex]

            card.isFaceUp = true
            adapter.notifyItemChanged(position)

            moves++
            tvMoves.text = getString(R.string.game_movimientos, moves)

            // Comprobamos si hay match:
            // - Mismo tipo de tacho (binType).
            // - Una carta es tacho (isBin = true) y la otra es objeto (isBin = false).
            val isMatch = firstCard.binType == card.binType &&
                    firstCard.isBin != card.isBin

            if (isMatch) {
                firstCard.isMatched = true
                card.isMatched = true
                score += 100
                tvScore.text = getString(R.string.game_puntaje, score)
                indexOfSingleSelectedCard = null

                if (cards.all { it.isMatched }) {
                    onGameWon()
                }
            } else {
                score -= 10
                if (score < 0) score = 0
                tvScore.text = getString(R.string.game_puntaje, score)

                boardLocked = true
                timeHandler.postDelayed({
                    firstCard.isFaceUp = false
                    card.isFaceUp = false
                    adapter.notifyItemChanged(firstIndex)
                    adapter.notifyItemChanged(position)
                    boardLocked = false
                }, 700)

                indexOfSingleSelectedCard = null
            }
        }
    }
    /*
     Vuelve a poner boca abajo todas las cartas que no han sido emparejadas,
     pero que quedaron boca arriba del turno anterior.
     Se usa al inicio de un nuevo turno, antes de voltear una nueva carta.
    */
    private fun restoreUnmatchedCards() {
        for (card in cards) {
            if (!card.isMatched && card.isFaceUp) {
                card.isFaceUp = false
            }
        }
        adapter.notifyDataSetChanged()
    }
    /*
     Lógica cuando el jugador gana el juego (todas las parejas encontradas):
     - Detiene el cronómetro.
     - Calcula el tiempo final.
     - Guarda el resultado en el histórico.
     - Muestra un diálogo con los datos de la partida y opciones para:
       - Ver resultados.
       - Jugar de nuevo.
     */
    private fun onGameWon() {
        // detener cronómetro
        timeHandler.removeCallbacksAndMessages(null)
        elapsedMillis = System.currentTimeMillis() - startTime

        val seconds = elapsedMillis / 1000

        // guardar resultado en el histórico
        val result = MemoryResult(
            score = score,
            moves = moves,
            timeMillis = elapsedMillis,
            timestamp = System.currentTimeMillis()
        )
        MemoryResultRepository.saveResult(requireContext(), result)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.game_ganaste))
            .setMessage(getString(R.string.memory_game_win_message, seconds, moves, score))
            .setPositiveButton(getString(R.string.game_ver_resultados)) { _, _ ->
                navigateToResult()
            }
            .setNegativeButton(getString(R.string.game_jugar_de_nuevo)) { dialog, _ ->
                dialog.dismiss()
                setupGame()
            }
            .setCancelable(false)
            .show()
    }
    //Navega al fragmento de resultados del juego de memoria,
    private fun navigateToResult() {
        val args = Bundle().apply {
            putInt(getString(R.string.game_score), score)
            putInt(getString(R.string.game_moves), moves)
            putLong(getString(R.string.game_timemillis), elapsedMillis)
        }
        findNavController().navigate(
            R.id.action_memoryGameFragment_to_memoryResultFragment,
            args
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timeHandler.removeCallbacksAndMessages(null)
    }
}