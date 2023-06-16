package com.example.finbbyapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finbbyapp.MainActivity
import com.example.finbbyapp.databinding.ActivitySurveyBinding
import com.example.finbbyapp.network.ApiConfig
import com.example.finbbyapp.network.SurveyRequest
import com.example.finbbyapp.network.response.AddSurveyResponse
import com.example.finbbyapp.network.response.ErrorResponse
import com.example.finbbyapp.network.response.RegisterResponse
import com.example.finbbyapp.ui.preferences.SurveyModel
import com.example.finbbyapp.ui.preferences.SurveyPreference
import com.example.finbbyapp.ui.preferences.UserPreference
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SurveyActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySurveyBinding
    private lateinit var rvQSurvey: RecyclerView
    private lateinit var listContentAdapter: ListQSurveyAdapter
    private lateinit var userPreference: UserPreference
    private lateinit var surveyPreference: SurveyPreference
    private lateinit var surveyRequest: SurveyRequest
    private lateinit var surveyModel: SurveyModel
    val listQSurvey = arrayListOf(
        DetailQSurvey("Sport"),
        DetailQSurvey("Book"),
        DetailQSurvey("Movie"),
        DetailQSurvey("Music"),
        DetailQSurvey("Food"),
        DetailQSurvey("Traveling"),
        DetailQSurvey("Gamer"),
        DetailQSurvey("Vlog"),
        DetailQSurvey(""),
        DetailQSurvey(""),
        DetailQSurvey(""),)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        userPreference = UserPreference(this)
        surveyPreference = SurveyPreference(this, userPreference.getUser().name as String)
        surveyModel = SurveyModel()
        rvQSurvey = binding.rvQsurvey
        listContentAdapter = ListQSurveyAdapter(listQSurvey, this)
        rvQSurvey.layoutManager = GridLayoutManager(this, 3)

        binding.save.setOnClickListener {
            if(userPreference.getTopic()?.topic1?.length as Int > 2 && userPreference.getTopic()?.topic1?.isNotEmpty() as Boolean) {
                binding.progressBar.visibility = View.VISIBLE
                surveyRequest = SurveyRequest(userPreference.getTopic()?.topic1?.toLowerCase() as String)
                val client = ApiConfig.getApiService().addSurvey(surveyRequest)
                client.enqueue(object : Callback<AddSurveyResponse> {
                    override fun onResponse(
                        call: Call<AddSurveyResponse>,
                        response: Response<AddSurveyResponse>
                    ) {
                        if (response.isSuccessful) {
                            surveyModel.isSurvey = true
                            surveyPreference.setSurvey(surveyModel)
                            binding.progressBar.visibility = View.GONE
                            startActivity(Intent(this@SurveyActivity, MainActivity::class.java))
                            finish()
                        } else {
                            //val responseBody = Gson().fromJson(response.errorBody()?.charStream(), RegisterResponse::class.java)
                            binding.progressBar.visibility = View.GONE
                            val errorBody = response.errorBody()
                            if (errorBody?.contentType()?.subtype == "json") {
                                val responseBody = Gson().fromJson(errorBody.charStream(), ErrorResponse::class.java)
                                Toast.makeText(this@SurveyActivity, responseBody.message.toString(), Toast.LENGTH_LONG).show()
                                // Lakukan penanganan error sesuai kebutuhan aplikasi Anda
                            } else {
                                val errorMessage = errorBody?.string()
                                Toast.makeText(this@SurveyActivity, errorMessage, Toast.LENGTH_LONG).show()
                                // Lakukan penanganan error sesuai kebutuhan aplikasi Anda
                            }
                        }
                    }
                    override fun onFailure(call: Call<AddSurveyResponse>, t: Throwable) {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@SurveyActivity, t.message.toString(), Toast.LENGTH_LONG).show()
                    }
                })
            }
            else  {
                Toast.makeText(this,"Pilih Topik dulu", Toast.LENGTH_SHORT).show()
            }
        }

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
        rvQSurvey.adapter = listContentAdapter

    }

    private fun updateList() {

    }
}