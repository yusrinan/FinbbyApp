package com.example.finbbyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.finbbyapp.R
import com.example.finbbyapp.databinding.ActivityDetailContentBinding

class DetailContentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportActionBar()?.title = ""
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        val title = intent.getStringExtra("title_detail")
        val sender = intent.getStringExtra("sender_detail")
        val deskripsi = intent.getStringExtra("deskripsi_detail")
        val photo = intent.getStringExtra("photo_detail")

        binding.title.text = title
        binding.author.text = sender
        binding.deskripsi.text = deskripsi
        Glide.with(this)
            .load(photo)
            .into(binding.imageView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_share, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.share -> {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, binding.deskripsi.text)
                startActivity(Intent.createChooser(shareIntent, "share"))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}