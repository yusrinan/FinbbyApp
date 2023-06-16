package com.example.finbbyapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finbbyapp.R
import com.example.finbbyapp.databinding.FragmentForumBinding
import com.example.finbbyapp.databinding.FragmentHomeBinding
import com.example.finbbyapp.network.response.DataAllForumItem
import com.example.finbbyapp.ui.preferences.UserModel
import com.example.finbbyapp.ui.preferences.UserPreference
import com.google.android.material.snackbar.Snackbar

class ForumFragment : Fragment() {
    private lateinit var rvForum: RecyclerView
    private var _binding: FragmentForumBinding? = null
    private val binding get() = _binding!!
    private val forumViewModel by viewModels<ForumViewModel>()
    private lateinit var userPreference: UserPreference
    private lateinit var userModel: UserModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForumBinding.inflate(inflater, container, false)
        val root: View = binding.root
        rvForum = binding.rvForum

        userPreference = UserPreference(requireActivity())

        userModel = userPreference.getUser()

        forumViewModel.listForum.observe(requireActivity(), { consumerUser ->
            val filter1 = consumerUser.filter {
                it.admin.contains(userModel.name.toString())
            }
            val filter2 = consumerUser.filter {
                it.members.contains(userModel.name.toString())
            }
            val combinedFilter = filter1.union(filter2)
            if(combinedFilter.isEmpty()) {
                showTeks(true)
            }
            else {
                showTeks(false)
                showRecyclerList(combinedFilter.toList())
            }
        })

        forumViewModel.isLoading.observe(requireActivity(), {
            showLoading(it)
        })



        forumViewModel.errorOnResponse.observe(requireActivity(), {
            it.getContentIfNotHandled()?.let { errorText ->
                Snackbar.make(
                    requireView(),
                    errorText,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })

        forumViewModel.errorOnFailure.observe(requireActivity(), {
            it.getContentIfNotHandled()?.let { errorText ->
                Snackbar.make(
                    requireView(),
                    errorText,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.title = "My Forum"
        activity.supportActionBar?.show()

        binding.addForum.setOnClickListener {
            val intent = Intent(requireActivity(), AddForumActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear() // Menghapus semua item menu yang ada

        when (val activity = requireActivity()) {
            is AppCompatActivity -> {
                activity.menuInflater.inflate(R.menu.forum_menu, menu) // Inflate menu untuk Fragment 1
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showRecyclerList(consumerForum: List<DataAllForumItem>) {
        val listForum = consumerForum.map {
            DetailForum(it.forumname, R.drawable.group, it.forumdesc)
        }

        rvForum.layoutManager = LinearLayoutManager(requireContext())
        val listForumAdapter = ListForumAdapter(listForum)
        rvForum.adapter = listForumAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showTeks(isNoEMpty: Boolean) {
        binding.noJoin.visibility = if (isNoEMpty) View.VISIBLE else View.GONE
    }
}