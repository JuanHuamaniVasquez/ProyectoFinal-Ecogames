package com.example.semaforo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.semaforo.data.MemoryResult
import com.example.semaforo.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MemoryResultAdapter(
    private val results: List<MemoryResult>
) : RecyclerView.Adapter<MemoryResultAdapter.MemoryResultViewHolder>() {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoryResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_memory_result, parent, false)
        return MemoryResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemoryResultViewHolder, position: Int) {
        holder.bind(results[position], dateFormat)
    }

    override fun getItemCount(): Int = results.size

    class MemoryResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvScore: TextView = itemView.findViewById(R.id.tvItemMemoryScore)
        private val tvMoves: TextView = itemView.findViewById(R.id.tvItemMemoryMoves)
        private val tvTime: TextView = itemView.findViewById(R.id.tvItemMemoryTime)
        private val tvDate: TextView = itemView.findViewById(R.id.tvItemMemoryDate)

        fun bind(result: MemoryResult, dateFormat: SimpleDateFormat) {
            tvScore.text = "Puntaje: ${result.score}"
            tvMoves.text = "Movimientos: ${result.moves}"
            val seconds = result.timeMillis / 1000
            tvTime.text = "Tiempo: ${seconds}s"

            val date = Date(result.timestamp)
            tvDate.text = "Fecha: ${dateFormat.format(date)}"
        }
    }
}