package com.example.finbbyapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.finbbyapp.R
import com.example.finbbyapp.databinding.FragmentAddContent1Binding
import com.example.finbbyapp.databinding.FragmentAddForumBinding

class AddForumFragment : Fragment() {
    private var _binding: FragmentAddForumBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddForumBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.show()

        return root
    }
}