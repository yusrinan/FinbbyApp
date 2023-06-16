package com.example.finbbyapp.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finbbyapp.R
import com.example.finbbyapp.databinding.ActivityChatBinding
import com.example.finbbyapp.ui.preferences.UserPreference
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var rvChat: RecyclerView
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: FirebaseMessageAdapter
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.show()

        userPreference = UserPreference(this)

        db = Firebase.database

        val namaDb = if (Build.VERSION.SDK_INT >= 33) {
            intent.getStringExtra("db")
        } else {
            @Suppress("DEPRECATION")
            intent.getStringExtra("db")
        }

        supportActionBar?.title = namaDb
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val messagesRef = db.reference.child(namaDb as String)



        binding.sendButton.setOnClickListener {
            val friendlyMessage = DataMessage(
                userPreference.getUser().name,
                binding.edtChat.text.toString().trim(),
                "https://img.freepik.com/vector-premium/personaje-avatar-moda-icono-hombres-ilustracion-vector-plano-gente-alegre-feliz-marco-redondo-retratos-masculinos-grupo-equipo-adorables-chicos-aislados-sobre-fondo-blanco_275421-286.jpg?w=500",
                Date().time
            )
            messagesRef.push().setValue(friendlyMessage) { error, _ ->
                if (error != null) {
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                } else {
                }
            }
            binding.edtChat.setText("")
        }

        val manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        binding.rvChat.layoutManager = manager

        val options = FirebaseRecyclerOptions.Builder<DataMessage>()
            .setQuery(messagesRef, DataMessage::class.java)
            .build()
        adapter = FirebaseMessageAdapter(options, userPreference.getUser().name)
        binding.rvChat.adapter = adapter

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                val newItemPosition = positionStart + itemCount - 1
                binding.rvChat.scrollToPosition(newItemPosition)
            }
        })
        //showRecyclerList()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    public override fun onResume() {
        super.onResume()
        adapter.startListening()
    }

    public override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }



    override fun onBackPressed() {
        super.onBackPressed()
    }

    companion object {
        const val MESSAGES_CHILD = "messages2"
    }
}