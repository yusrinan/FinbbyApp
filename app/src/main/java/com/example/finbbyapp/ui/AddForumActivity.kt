package com.example.finbbyapp.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finbbyapp.MainActivity
import com.example.finbbyapp.R
import com.example.finbbyapp.databinding.ActivityAddForumBinding
import com.example.finbbyapp.helper.uriToFile
import com.example.finbbyapp.network.AddForumRequest
import com.example.finbbyapp.network.ApiConfig
import com.example.finbbyapp.network.RegisterRequest
import com.example.finbbyapp.network.response.AddForumResponse
import com.example.finbbyapp.network.response.ErrorResponse
import com.example.finbbyapp.network.response.RegisterResponse
import com.example.finbbyapp.ui.preferences.UserPreference
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddForumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddForumBinding
    private lateinit var rvTopic: RecyclerView
    private lateinit var addForumRequest: AddForumRequest
    private lateinit var userPreference: UserPreference
    private lateinit var listContentAdapter: ListQSurveyAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddForumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference(this)

        supportActionBar?.title = "New Forum"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rvTopic = binding.rvTopics
        listContentAdapter = ListQSurveyAdapter(listQSurvey, this)
        rvTopic.layoutManager = GridLayoutManager(this, 3)

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
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editText.windowToken, 0)
                true
            } else {
                false
            }
        }
        rvTopic.adapter = listContentAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.add_forum_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.add_forum -> {
                when {
                    binding.forumName.text.toString().isEmpty() -> {
                       Toast.makeText(this, "Terdapat inputan yang kosong", Toast.LENGTH_SHORT).show()
                    }
                    binding.deskripsi.text.toString().isEmpty() -> {
                        Toast.makeText(this, "Terdapat inputan yang kosong", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        binding.progressBar.visibility = View.VISIBLE
                        addForumRequest = AddForumRequest(binding.forumName.text.toString(), binding.deskripsi.text.toString(), "", userPreference.getTopic().topic1.toString())

                        val client = ApiConfig.getApiService().addForum(addForumRequest)
                        client.enqueue(object : Callback<AddForumResponse> {
                            override fun onResponse(
                                call: Call<AddForumResponse>,
                                response: Response<AddForumResponse>
                            ) {
                                if (response.isSuccessful) {
                                    binding.progressBar.visibility = View.GONE
                                    startActivity(Intent(this@AddForumActivity, MainActivity::class.java))
                                } else {
                                    val responseBody = Gson().fromJson(response.errorBody()?.charStream(), RegisterResponse::class.java)
                                    binding.progressBar.visibility = View.GONE
                                    val errorBody = response.errorBody()?.string()
                                    val contentType = response.errorBody()?.contentType()?.toString()

                                    if (contentType?.contains("text/html") == true) {
                                        // Penanganan ketika pesan error berupa HTML
                                        Toast.makeText(this@AddForumActivity, errorBody, Toast.LENGTH_LONG).show()
                                    } else {
                                        // Penanganan ketika pesan error dalam format lain atau tidak diketahui
                                        val responseBody = Gson().fromJson(errorBody, ErrorResponse::class.java)
                                        Toast.makeText(this@AddForumActivity, responseBody?.message ?: "Unknown error", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                            override fun onFailure(call: Call<AddForumResponse>, t: Throwable) {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(this@AddForumActivity, t.message.toString(), Toast.LENGTH_LONG).show()
                            }
                        })
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }
}