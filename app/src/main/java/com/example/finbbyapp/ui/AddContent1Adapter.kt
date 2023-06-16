package com.example.finbbyapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.finbbyapp.R
import com.example.finbbyapp.ui.preferences.TopicModel
import com.example.finbbyapp.ui.preferences.UserPreference

class AddContent1Adapter(var listQSurvey: ArrayList<DetailQSurvey>, var context: Context) : RecyclerView.Adapter<AddContent1Adapter.ListViewHolder>() {
    val listTopics = ArrayList<Int>()
    val up = UserPreference(context)
    val selectedTags: MutableSet<String> = mutableSetOf()

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.item_qsurvey)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_qsurvey, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        var (title) = listQSurvey[position]
        holder.title.text = title
        if(title == "") {
            title = position.toString()
        }


        holder.title.setOnClickListener {
            if (selectedTags.contains(title)) {
                selectedTags.remove(title)
                holder.title.setBackgroundResource(R.drawable.list_btn_qsurvey)
            } else {
                if (selectedTags.size < 1) {
                    selectedTags.add(title)
                    holder.title.setBackgroundResource(R.drawable.list_btn_qsurvey_checked)
                } else {
                    Toast.makeText(holder.itemView.context, "Maksimal pilih 1 topik", Toast.LENGTH_SHORT).show()
                }
            }
            if(selectedTags.size == 1 && selectedTags.elementAt(0).length > 2) {
                val up = UserPreference(context)
                val tm = TopicModel()
                tm.topic1 = selectedTags.elementAt(0)
                up.setTopic(tm)
            }
            else {
                up.deleteTopic()
            }
        }
    }

    fun updateList(newList: ArrayList<DetailQSurvey>) {
        selectedTags.clear()
        listQSurvey = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listQSurvey.size

}