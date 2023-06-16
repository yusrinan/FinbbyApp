package com.example.finbbyapp.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finbbyapp.R

class ListForumAdapter(val listForum: List<DetailForum>) : RecyclerView.Adapter<ListForumAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photo: ImageView = itemView.findViewById(R.id.img_forum)
        val name: TextView = itemView.findViewById(R.id.name)
        val description: TextView = itemView.findViewById(R.id.description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_forum, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, photo, description) = listForum[position]
        holder.photo.setImageResource(photo)
        holder.name.text = name
        holder.description.text = description

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, ChatActivity::class.java)
            intentDetail.putExtra("db", name)
            holder.itemView.context.startActivity(intentDetail)
        }

//        holder.title.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("myapp://chat"))
//            holder.itemView.context.startActivity(intent)
//        }
    }

    override fun getItemCount(): Int = listForum.size


}