package com.example.finbbyapp.network

import com.google.gson.annotations.SerializedName

data class AddPostingRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("topic")
    val topic: String,
    @SerializedName("content")
    val content: String
)