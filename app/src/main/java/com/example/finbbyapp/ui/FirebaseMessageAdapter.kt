package com.example.finbbyapp.ui

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finbbyapp.R
import com.example.finbbyapp.databinding.ItemMessageBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class FirebaseMessageAdapter(
    options: FirebaseRecyclerOptions<DataMessage>,
    private val currentUserName: String?
) : FirebaseRecyclerAdapter<DataMessage, FirebaseMessageAdapter.MessageViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_message, parent, false)
        val binding = ItemMessageBinding.bind(view)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MessageViewHolder,
        position: Int,
        model: DataMessage
    ) {
        holder.bind(model, position)
    }

    inner class MessageViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataMessage, position: Int) {
            binding.message.text = item.message
           setPosition(this, position, item.message as String, item.name as String)
            binding.name.text = item.name
            Glide.with(itemView.context)
                .load(item.photo)
                .circleCrop()
                .into(binding.imgProfil)
            if (item.timestamp != null) {
                binding.date.text = DateUtils.getRelativeTimeSpanString(item.timestamp)
            }
        }
        private fun setPosition(holder: MessageViewHolder, position: Int, message: String, name: String) {
            val layoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams

        if (currentUserName != name) {
            // Item dengan posisi genap (mulai dari 0) ditempatkan di sebelah kiri.
            layoutParams.setMargins(8, 25, 96, 0)
        } else {
            if(message.length > 60) {

                layoutParams.setMargins(80, 25, 8, 0)
            } else if(message.length > 35) {
                layoutParams.setMargins(190, 25, 8, 0)
            }
            else if(message.length > 20) {
                layoutParams.setMargins(230, 25, 8, 0)
            } else {
                layoutParams.setMargins(260, 25, 8, 0)
            }
            // Item dengan posisi ganjil ditempatkan di sebelah kanan.

            //layoutParams.width = holder.itemView.context.resources.displayMetrics.widthPixels - 90
        }

        holder.itemView.layoutParams = layoutParams
        }
    }
}