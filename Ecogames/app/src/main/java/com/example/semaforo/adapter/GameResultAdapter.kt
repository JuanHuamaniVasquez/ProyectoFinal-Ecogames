package com.example.semaforo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.semaforo.data.GameResult
import com.example.semaforo.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GameResultAdapter(
    private val results: List<GameResult>
) : RecyclerView.Adapter<GameResultAdapter.GameResultViewHolder>() {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game_result, parent, false)
        return GameResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameResultViewHolder, position: Int) {
        val result = results[position]
        holder.bind(result, dateFormat)
    }

    override fun getItemCount(): Int = results.size

    class GameResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvItemScore: TextView = itemView.findViewById(R.id.tvItemScore)
        private val tvItemDate: TextView = itemView.findViewById(R.id.tvItemDate)

        fun bind(result: GameResult, dateFormat: SimpleDateFormat) {
            tvItemScore.text = "Puntaje: ${result.score}"
            val date = Date(result.timestamp)
            tvItemDate.text = "Fecha: ${dateFormat.format(date)}"
        }
    }
}