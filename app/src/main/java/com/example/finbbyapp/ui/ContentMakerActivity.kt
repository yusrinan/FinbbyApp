package com.example.finbbyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finbbyapp.R
import com.example.finbbyapp.databinding.ActivityContentMakerBinding
import com.example.finbbyapp.network.response.DataAllForumItem
import com.example.finbbyapp.network.response.DataItem
import com.example.finbbyapp.ui.preferences.UserModel
import com.example.finbbyapp.ui.preferences.UserPreference
import com.google.android.material.snackbar.Snackbar

class ContentMakerActivity : AppCompatActivity() {
    private lateinit var rvForum: RecyclerView
    private lateinit var rvCMaker: RecyclerView
    private lateinit var userModel: UserModel
    private lateinit var userPreference: UserPreference
    private lateinit var binding: ActivityContentMakerBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private val forumViewModel by viewModels<ForumViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentMakerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Content Maker"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        userPreference = UserPreference(this)
        userModel = userPreference.getUser()
        rvForum = binding.rvForum
        rvCMaker = binding.rvContentMaker

        mainViewModel.listContent.observe(this, { consumerUser ->
            val filter = consumerUser.filter {
                it.sender.contains(userModel.name.toString())
            }
            if(filter.isEmpty()) {
                showTeks1(true)
            }
            else {
                showTeks1(false)
                showRecyclerList(filter)
            }
        })

//        mainViewModel.isLoading.observe(this, {
//            showLoading(it)
//        })

        mainViewModel.errorOnResponse.observe(this, {
            it.getContentIfNotHandled()?.let { errorText ->
                Snackbar.make(
                    window.decorView.rootView,
                    errorText,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })

        mainViewModel.errorOnFailure.observe(this, {
            it.getContentIfNotHandled()?.let { errorText ->
                Snackbar.make(
                    window.decorView.rootView,
                    errorText,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })

        forumViewModel.listForum.observe(this, { consumerUser ->
            val filter = consumerUser.filter {
                it.admin.contains(userModel.name.toString())
            }
            if(filter.isEmpty()) {
                showTeks2(true)
            }
            else {
                showTeks2(false)
                showRecyclerList2(filter)
            }
        })

//        forumViewModel.isLoading.observe(this, {
//            showLoading(it)
//        })



        forumViewModel.errorOnResponse.observe(this, {
            it.getContentIfNotHandled()?.let { errorText ->
                Snackbar.make(
                    window.decorView.rootView,
                    errorText,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })

        forumViewModel.errorOnFailure.observe(this, {
            it.getContentIfNotHandled()?.let { errorText ->
                Snackbar.make(
                    window.decorView.rootView,
                    errorText,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })


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

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun showRecyclerList(consumerContent: List<DataItem>) {
        val listContent = consumerContent.map {
            DetailContent(it.title,it.description,it.sender,it.attachment)
        }

        rvCMaker.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val listContentAdapter = ListCMakerAdapter(listContent)
        rvCMaker.adapter = listContentAdapter
    }

    private fun showRecyclerList2(consumerForum: List<DataAllForumItem>) {
        val listForum = consumerForum.map {
            DetailForum(it.forumname, R.drawable.group, it.forumdesc)
        }

        rvForum.layoutManager = LinearLayoutManager(this)
        val listForumAdapter = ListForumAdapter(listForum)
        rvForum.adapter = listForumAdapter
    }

    private fun showTeks1(isNoEMpty: Boolean) {
        binding.noPosting.visibility = if (isNoEMpty) View.VISIBLE else View.GONE
    }

    private fun showTeks2(isNoEMpty: Boolean) {
        binding.noJoin.visibility = if (isNoEMpty) View.VISIBLE else View.GONE
    }

//    private fun showLoading(isLoading: Boolean) {
//        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//    }
}