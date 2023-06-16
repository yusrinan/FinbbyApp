package com.example.finbbyapp.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.finbbyapp.R
import com.example.finbbyapp.network.ApiConfig
import com.example.finbbyapp.network.response.ErrorResponse
import com.example.finbbyapp.network.response.JoinForumResponse
import com.example.finbbyapp.network.response.RegisterResponse
import com.example.finbbyapp.ui.preferences.UserPreference
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListJoinForumAdapter(val listForum: List<DetailJoinForum>, application: Application) : RecyclerView.Adapter<ListJoinForumAdapter.ListViewHolder>() {
    val userPreference = UserPreference(application)


    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photo: ImageView = itemView.findViewById(R.id.img_forum)
        val name: TextView = itemView.findViewById(R.id.name)
        val btn: Button = itemView.findViewById(R.id.join)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_join_forum, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, photo, description, members, admin) = listForum[position]
        holder.photo.setImageResource(photo)
        holder.name.text = name

        if(members.contains(userPreference.getUser().name) || userPreference.getUser().name == admin) {
            holder.btn.visibility = View.GONE
        }
        else {
            holder.btn.visibility = View.VISIBLE
        }

        holder.btn.setOnClickListener {

//            val intentDetail = Intent(holder.itemView.context, ChatActivity::class.java)
////            intentDetail.putExtra(DetailActivity.KEY_HERO, listHero[holder.adapterPosition])
//            holder.itemView.context.startActivity(intentDetail)

            val client = ApiConfig.getApiService().joinForum(name)
            client.enqueue(object : Callback<JoinForumResponse> {
                override fun onResponse(
                    call: Call<JoinForumResponse>,
                    response: Response<JoinForumResponse>
                ) {
                    if (response.isSuccessful) {
                        //binding.progressBar.visibility = View.GONE
                        val intent = Intent(holder.itemView.context, ChatActivity::class.java)
                        intent.putExtra("db", name)
                        holder.itemView.context.startActivity(intent)
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val contentType = response.errorBody()?.contentType()?.toString()

                        if (contentType?.contains("text/html") == true) {
                            // Penanganan ketika pesan error berupa HTML
                            Toast.makeText(holder.itemView.context, errorBody, Toast.LENGTH_LONG).show()
                        } else {
                            // Penanganan ketika pesan error dalam format lain atau tidak diketahui
                            val responseBody = Gson().fromJson(errorBody, ErrorResponse::class.java)
                            Toast.makeText(holder.itemView.context, responseBody?.message ?: "Unknown error", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                override fun onFailure(call: Call<JoinForumResponse>, t: Throwable) {
                   // binding.progressBar.visibility = View.GONE
                    Toast.makeText(holder.itemView.context, t.message.toString(), Toast.LENGTH_LONG).show()
                }
            })
        }

        holder.itemView.setOnClickListener {
            if(members.contains(userPreference.getUser().name) || userPreference.getUser().name == admin) {
                val intent = Intent(holder.itemView.context, ChatActivity::class.java)
                intent.putExtra("db", name)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = listForum.size


}