package com.example.finbbyapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.finbbyapp.R
import com.example.finbbyapp.databinding.FragmentAddForumBinding
import com.example.finbbyapp.databinding.FragmentProfileBinding
import com.example.finbbyapp.ui.preferences.UserPreference

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var userPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        userPreference = UserPreference(requireActivity())
        binding.user.text = userPreference.getUser().name
        binding.textEmail.text = userPreference.getUser().email

        binding.cMaker.setOnClickListener {
            val intent = Intent(requireActivity(), ContentMakerActivity::class.java)
            startActivity(intent)
        }

        binding.logout.setOnClickListener {
           userPreference.deleteDataPreference()
            userPreference.deleteTopic()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.hide()

        return root
    }
}