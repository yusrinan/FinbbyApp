package com.example.finbbyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finbbyapp.R
import com.example.finbbyapp.databinding.FragmentHomeBinding
import com.example.finbbyapp.network.response.DataItem
import com.example.finbbyapp.ui.DetailContent
import com.example.finbbyapp.ui.ListContentAdapter
import com.example.finbbyapp.ui.LoginActivity
import com.example.finbbyapp.ui.MainViewModel
import com.example.finbbyapp.ui.preferences.UserModel
import com.example.finbbyapp.ui.preferences.UserPreference
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {
    private lateinit var rvContent: RecyclerView
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.show()

        rvContent = binding.rvContent
        mainViewModel.listContent.observe(requireActivity(), { consumerUser ->
            showRecyclerList(consumerUser)
        })

        mainViewModel.isLoading.observe(requireActivity(), {
            showLoading(it)
        })



        mainViewModel.errorOnResponse.observe(requireActivity(), {
            it.getContentIfNotHandled()?.let { errorText ->
                Snackbar.make(
                    requireView(),
                    errorText,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })

        mainViewModel.errorOnFailure.observe(requireActivity(), {
            it.getContentIfNotHandled()?.let { errorText ->
                Snackbar.make(
                    requireView(),
                    errorText,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })
        return root
    }

    private fun showRecyclerList(consumerContent: List<DataItem>) {
        val listContent = consumerContent.map {
            DetailContent(it.title,it.description,it.sender,it.attachment)
        }

        rvContent.layoutManager = LinearLayoutManager(requireContext())
        val listContentAdapter = ListContentAdapter(listContent)
        rvContent.adapter = listContentAdapter
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}