package com.example.semaforo.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.semaforo.R
import com.example.semaforo.data.CintaResult
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
class CintaResultAdapter(private val results: List<CintaResult>
) : RecyclerView.Adapter<CintaResultAdapter.CintaResultViewHolder>()  {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CintaResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cinta_result, parent, false)
        return CintaResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: CintaResultViewHolder, position: Int) {
        holder.bind(results[position], dateFormat)
    }

    override fun getItemCount(): Int = results.size

    class CintaResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvScore: TextView = itemView.findViewById(R.id.tvItemCintaScore)
        private val tvCorrect: TextView = itemView.findViewById(R.id.tvItemCintaCorrect)
        private val tvLives: TextView = itemView.findViewById(R.id.tvItemCintaLives)
        private val tvDate: TextView = itemView.findViewById(R.id.tvItemCintaDate)

        fun bind(result: CintaResult, dateFormat: SimpleDateFormat) {
            tvScore.text = "Puntaje: ${result.score}"
            tvCorrect.text = "Aciertos: ${result.correctCount}"
            tvLives.text = "Vidas restantes: ${result.livesRemaining}"

            val date = Date(result.timestamp)
            tvDate.text = "Fecha: ${dateFormat.format(date)}"
        }
    }
}