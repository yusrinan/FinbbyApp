package com.example.finbbyapp.ui

import com.example.finbbyapp.R
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finbbyapp.network.response.DataItem

class ListContentAdapter(val listContent: List<DetailContent>) : RecyclerView.Adapter<ListContentAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.banner_content)
        val title: TextView = itemView.findViewById(R.id.title_content)
        val deskripsi: TextView = itemView.findViewById(R.id.deskripsi_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_content, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (title, deskripsi, name, photo) = listContent[position]
        Glide.with(holder.itemView.context)
            .load(listContent[position].photo)
            .into(holder.imgPhoto)
        holder.title.text = title
        holder.deskripsi.text = deskripsi

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailContentActivity::class.java)
            intentDetail.putExtra("title_detail", title)
            intentDetail.putExtra("sender_detail", name)
            intentDetail.putExtra("deskripsi_detail", deskripsi)
            intentDetail.putExtra("photo_detail", photo)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    override fun getItemCount(): Int = listContent.size

}