package com.example.finbbyapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finbbyapp.MainActivity
import com.example.finbbyapp.R
import com.example.finbbyapp.databinding.FragmentAddContent1Binding
import com.example.finbbyapp.databinding.FragmentHomeBinding
import com.example.finbbyapp.network.ApiConfig
import com.example.finbbyapp.network.response.LoginResponse
import com.example.finbbyapp.network.response.RegisterResponse
import com.example.finbbyapp.ui.preferences.AddContent1Model
import com.example.finbbyapp.ui.preferences.SurveyPreference
import com.example.finbbyapp.ui.preferences.UserPreference
import com.google.gson.Gson
import retrofit2.Callback
import retrofit2.Response

class AddContent1Fragment : Fragment() {
    private lateinit var rvQSurvey: RecyclerView
    private var _binding: FragmentAddContent1Binding? = null
    private val binding get() = _binding!!
    private lateinit var userPreference: UserPreference
    private lateinit var addContent1Model: AddContent1Model
    private lateinit var listContentAdapter: AddContent1Adapter
    val listQSurvey = arrayListOf(
        DetailQSurvey("Sport"),
        DetailQSurvey("Book"),
        DetailQSurvey("Movie"),
        DetailQSurvey("Music"),
        DetailQSurvey("Food"),
        DetailQSurvey("Traveling"),
        DetailQSurvey("Gamer"),
        DetailQSurvey(""),
        DetailQSurvey(""),
        DetailQSurvey(""),)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddContent1Binding.inflate(inflater, container, false)
        val root: View = binding.root
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.show()
        userPreference = UserPreference(requireActivity())
        addContent1Model = AddContent1Model()
        rvQSurvey = binding.rvTopics
        listContentAdapter = AddContent1Adapter(listQSurvey, requireActivity())
        rvQSurvey.layoutManager = GridLayoutManager(requireActivity(), 3)

        setHasOptionsMenu(true)

        val editText = binding.edtSearch
        editText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val filteredList = ArrayList<DetailQSurvey>()
                for (item in listQSurvey) {
                    if (item.title.contains(editText.text.toString().trim(), ignoreCase = true)) {
                        filteredList.add(item)
                    }
                }

                listContentAdapter.updateList(filteredList)
                listContentAdapter.notifyDataSetChanged()
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editText.windowToken, 0)
                true
            } else {
                false
            }
        }
        rvQSurvey.adapter = listContentAdapter
        //showRecyclerList()

        binding.next.setOnClickListener {
            val title = binding.title.text.toString().trim()
            val content = binding.content.text.toString().trim()

            when {
                title.isEmpty() -> {
                    Toast.makeText(requireActivity(), "Terdapat inputan yg masih kosong", Toast.LENGTH_SHORT).show()
                }
                content.isEmpty() ->  {
                    Toast.makeText(requireActivity(), "Terdapat inputan yg masih kosong", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    addContent1Model.title = title
                    addContent1Model.content = content
                    addContent1Model.topic = userPreference.getTopic().topic1
                    val intent = Intent(requireActivity(), AddContent2Activity::class.java)
                    startActivity(intent)
                }
            }
        }

        return root
    }
}