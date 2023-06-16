package com.example.finbbyapp.ui

import android.app.Application
import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide.init
import com.example.finbbyapp.helper.Event
import com.example.finbbyapp.network.ApiConfig
import com.example.finbbyapp.network.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(): ViewModel() {
    private val _listContent = MutableLiveData<List<DataItem>>()
    val listContent: LiveData<List<DataItem>> = _listContent
    private val _listForum = MutableLiveData<List<DataItem>>()
    val listForum: LiveData<List<DataItem>> = _listForum
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _isLoading2 = MutableLiveData<Boolean>()
    val isLoading2: LiveData<Boolean> = _isLoading2
    private val _errorOnResponse = MutableLiveData<Event<String>>()
    val errorOnResponse: LiveData<Event<String>> = _errorOnResponse
    private val _errorOnFailure = MutableLiveData<Event<String>>()
    val errorOnFailure: LiveData<Event<String>> = _errorOnFailure
    private val _errorOnResponse2 = MutableLiveData<Event<String>>()
    val errorOnResponse2: LiveData<Event<String>> = _errorOnResponse2
    private val _errorOnFailure2 = MutableLiveData<Event<String>>()
    val errorOnFailure2: LiveData<Event<String>> = _errorOnFailure2

    init {
        getAllPosting()
    }

    fun getAllPosting() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllPosting()
        client.enqueue(object : Callback<GetAllPostingResponse> {
            override fun onResponse(
                call: Call<GetAllPostingResponse>,
                response: Response<GetAllPostingResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listContent.value = response.body()?.data
                } else {
                    _errorOnResponse.value = Event(response.message())
                }
            }
            override fun onFailure(call: Call<GetAllPostingResponse>, t: Throwable) {
                _isLoading.value = false
                _errorOnFailure.value = Event(t.message.toString())
            }
        })
    }

//    fun joinForum(name: String) {
//        _isLoading2.value = true
//        val client = ApiConfig.getApiService().joinForum(name)
//        client.enqueue(object : Callback<JoinForumResponse> {
//            override fun onResponse(
//                call: Call<JoinForumResponse>,
//                response: Response<JoinForumResponse>
//            ) {
//                _isLoading2.value = false
//                if (response.isSuccessful) {
//                    val intent = Intent(application, ChatActivity::class.java)
//                    intent.putExtra("db", name)
//                    ContextCompat.startActivity(intent)
//                } else {
//                    _errorOnResponse2.value = Event(response.message())
//                }
//            }
//            override fun onFailure(call: Call<JoinForumResponse>, t: Throwable) {
//                _isLoading2.value = false
//                _errorOnFailure2.value = Event(t.message.toString())
//            }
//        })
//    }
}