package com.example.finbbyapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finbbyapp.helper.Event
import com.example.finbbyapp.network.ApiConfig
import com.example.finbbyapp.network.response.DataAllForumItem
import com.example.finbbyapp.network.response.GetAllForumResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForumViewModel: ViewModel() {
    private val _listForum = MutableLiveData<List<DataAllForumItem>>()
    val listForum: LiveData<List<DataAllForumItem>> = _listForum
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _errorOnResponse = MutableLiveData<Event<String>>()
    val errorOnResponse: LiveData<Event<String>> = _errorOnResponse
    private val _errorOnFailure = MutableLiveData<Event<String>>()
    val errorOnFailure: LiveData<Event<String>> = _errorOnFailure

    init {
        getAllForum()
    }

    fun getAllForum() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllForum()
        client.enqueue(object : Callback<GetAllForumResponse> {
            override fun onResponse(
                call: Call<GetAllForumResponse>,
                response: Response<GetAllForumResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listForum.value = response.body()?.data
                } else {
                    _errorOnResponse.value = Event(response.message())
                }
            }
            override fun onFailure(call: Call<GetAllForumResponse>, t: Throwable) {
                _isLoading.value = false
                _errorOnFailure.value = Event(t.message.toString())
            }
        })
    }
}