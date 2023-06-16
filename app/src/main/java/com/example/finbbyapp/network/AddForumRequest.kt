package com.example.finbbyapp.network

import com.google.gson.annotations.SerializedName

data class AddForumRequest(
    @SerializedName("forumname")
    val forumname: String,
    @SerializedName("forumdesc")
    val forumdesc: String,
    @SerializedName("share")
    val share: String,
    @SerializedName("topic")
    val topic: String
)
