package com.example.semaforo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.semaforo.R
import com.example.semaforo.data.MemoryCard

class MemoryGameAdapter (
    private val cards: List<MemoryCard>,
    private val onCardClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<MemoryGameAdapter.MemoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_memory_card, parent, false)
        return MemoryViewHolder(view)
    }

    override fun getItemCount(): Int = cards.size

    override fun onBindViewHolder(holder: MemoryViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    inner class MemoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivCardImage: ImageView = itemView.findViewById(R.id.ivCardImage)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition   // o absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onCardClicked(position)
                }
            }
        }


        fun bind(card: MemoryCard) {
            val context = itemView.context
            if (card.isFaceUp || card.isMatched) {
                ivCardImage.setImageResource(card.imageResId)
                itemView.alpha = if (card.isMatched) 0.4f else 1f
            } else {
                ivCardImage.setImageResource(R.drawable.ic_card_back)
                itemView.alpha = 1f
            }
        }
    }
}