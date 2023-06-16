package com.example.finbbyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finbbyapp.R
import com.example.finbbyapp.databinding.ActivityJoinForumBinding
import com.example.finbbyapp.network.response.DataAllForumItem
import com.example.finbbyapp.ui.preferences.UserPreference
import com.google.android.material.snackbar.Snackbar

class JoinForumActivity : AppCompatActivity() {
    private lateinit var rvForum: RecyclerView
    private lateinit var binding: ActivityJoinForumBinding
    private lateinit var userPreference: UserPreference
    private val forumViewModel by viewModels<ForumViewModel>()
    private lateinit var data: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinForumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = intent.getStringExtra("nama_forum") as String

        userPreference = UserPreference(this)

        forumViewModel.listForum.observe(this, { consumerUser ->
            val filter = consumerUser.filter {
                it.forumname.toLowerCase().contains(data, ignoreCase = true)
            }
            if(filter.isEmpty()) {
                showTeks(true)
            }
            else {
                showTeks(false)
            }
            showRecyclerList(filter)
        })

        forumViewModel.isLoading.observe(this, {
            showLoading(it)
        })



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


        rvForum = binding.rvForum
        //showRecyclerList()
    }

    private fun showRecyclerList(consumerForum: List<DataAllForumItem>) {
        val listForum = consumerForum.map {
            DetailJoinForum(it.forumname, R.drawable.group, it.forumdesc, it.members, it.admin)
        }

        rvForum.layoutManager = LinearLayoutManager(this)
        val listForumAdapter = ListJoinForumAdapter(listForum, application)
        rvForum.adapter = listForumAdapter
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showTeks(isNoEMpty: Boolean) {
        binding.noJoin.visibility = if (isNoEMpty) View.VISIBLE else View.GONE
    }

//    override fun onResume() {
//        super.onResume()
//        forumViewModel.getAllForum()
//
//        forumViewModel.listForum.observe(this, { consumerUser ->
//            val filter = consumerUser.filter {
//                it.forumname.equals(data, ignoreCase = true)
//            }
//            if(filter.isEmpty()) {
//                showTeks(true)
//            }
//            else {
//                showTeks(false)
//            }
//            showRecyclerList(filter)
//        })
//    }
}