package com.example.finbbyapp.network

import com.google.gson.annotations.SerializedName


data class SurveyRequest(
    @SerializedName("topic")
    val topic: String,
)